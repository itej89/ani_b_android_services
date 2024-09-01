package Framework;

import android.hardware.usb.UsbDevice;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Delegates.MachineCommsConvey;
import Framework.DataTypes.Delegates.RemoteCommandToMachineRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.DataTypes.SerialCommandTxRunnable;
import FrameworkInterface.CommsAccess;
import FrameworkInterface.PublicTypes.CommsConvey;
import FrameworkInterface.PublicTypes.Constants.CommsStates;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Device;
import FrameworkInterface.PublicTypes.Machine;
//import fm.communication.bluetooth.*;

import static Framework.MachineCommunicationEngine.CommandLoopStates.SendCommand;

/**
 * Created by tej on 24/06/18.
 */

public class MachineCommunicationEngine implements CommsConvey {

    private final static String TAG = MachineCommunicationEngine.class.getSimpleName();

    Timer kineticsResponseTimeoutTimer;

    ArrayList<Device> DiscoveredDevices = new ArrayList<Device>();

    //Bluetooth Communication interface object
    CommsAccess HD_Comms;
    MachineCommsConvey notify_Machine_Comms;

    //Contains Parameters to identify the type of the data incoming from the Comms Module
     class CommsDataRecievingContext
    {
        Boolean IsWaitingForPendingRemoteRequestData = false;
        String NewDataRecieved = "";
        String RemoteRequestData = "";
    }
    CommsDataRecievingContext _commsDataRecievingContext = new CommsDataRecievingContext();


    //This contains the parameters for handling the the list of KineticRequests in the ComamndBuffer at the CommandIndex
     class CurrentCommsRequestContext
    {
        Integer CurrenCommandResponseDataCount = 0;

        String CurrentResponse = "";

        ArrayList<KineticsRequest> Data = new ArrayList<KineticsRequest>();
        ArrayList<KineticsResponse> ResponseData = new ArrayList<KineticsResponse>();

        UUID Acknowlegment_Key =  UUID.randomUUID();

        Boolean IsCurrentCommandProcessingCompleted = true;


        Integer KineticsRequestIndex = 0;
    }
    CurrentCommsRequestContext _currentCommsRequestContext = new CurrentCommsRequestContext();


    class NewCommandRequest
    {
        public UUID AcknowledgementKey;
        public  ArrayList<KineticsRequest> Command = new ArrayList<KineticsRequest>();

        public NewCommandRequest(UUID _AcknowledgementKey, ArrayList<KineticsRequest> _Command)
        {
            AcknowledgementKey = _AcknowledgementKey;
            Command = _Command;
        }
    }

    ArrayList<NewCommandRequest> CommandBuffer =  new ArrayList<NewCommandRequest>();


    Framework.MachineCommandHelper CommandHelper = new Framework.MachineCommandHelper();



    public MachineCommunicationEngine()
    {
        HD_Comms = new FrameworkInterface.InterfaceImplementation.SerialAccess();
    }

    void KineticsResponseTimeout()
    {
        _currentCommsRequestContext.IsCurrentCommandProcessingCompleted  = true;
        CurrentCommandLoopState = CommandLoopStates.NA;

        UUID Final_Ack = _currentCommsRequestContext.Acknowlegment_Key;
        ArrayList<KineticsResponse> Final_Response = _currentCommsRequestContext.ResponseData;

        if(CommandBuffer.size() > 0)
        {
            ArrayList<KineticsRequest> NextData = CommandBuffer.get(0).Command;
            UUID NextAcknowledgementKey = CommandBuffer.get(0).AcknowledgementKey;
            CommandBuffer.remove(0);
            StartNextCommandTransmission(NextData,NextAcknowledgementKey);
        }

        if(notify_Machine_Comms != null)
        notify_Machine_Comms.KineticsResponseDataTimeout(Final_Ack, Final_Response);
    }


    //Manadatory call to make the Machine Communication system ready
    public void SetCommsDelegate(MachineCommsConvey delegate)
    {
        notify_Machine_Comms = delegate;
        HD_Comms.SetCommsDelegate(this);
    }

    public void InitializeComms()
    {
        HD_Comms.InitializeComms();
    }


    public void StartScan()
    {
        DiscoveredDevices.clear();
        HD_Comms.StartScan();
    }

    public void StopScan()
    {
        HD_Comms.StopScan();

    }

    public void ConnectToMachine(Machine Machine)
    {
        for(Device peripehral : DiscoveredDevices)
        {
            if(Machine.Name == peripehral.Name && Machine.ID == peripehral.UUID)
            {
                HD_Comms.ConnectToDevice( peripehral);
                break;
            }
        }

    }

    public Boolean IsConnectedToMachine()
    {
        return HD_Comms.IsConnectedToPeripheral();
    }

    public void DisconnectMachine()
    {
        HD_Comms.DisconnectDevice();
    }



    public void serialStataChanged(CommsStates State) {
        switch (State)
        {
            case READY_TO_SCAN:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.READY_TO_SCAN);
                break;
            case CONNECTED:
                DiscoveredDevices.clear();
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.CONNECTED);
                break;
            case CONNECTING:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.CONNECTING);
                break;
            case CONNECTION_TIMEOUT:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.CONNECTION_TIMEOUT);
                break;
            case DISCONNECTING:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.DISCONNECTING);
                break;
            case DISCONNECTED:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.DISCONNECTED);
                break;
            case POWERED_OFF:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.POWERED_OFF);
                break;
            case POWERED_ON:
                notify_Machine_Comms.commsStateChanged(MachineCommsStates.POWERED_ON);
                break;
            default :
                break;
        }
    }

    public void newDeviceDiscovered(Device Device) {

        DiscoveredDevices.add(Device);

        Machine NewMachine = new Machine();
        NewMachine.Name = Device.Name;
        NewMachine.ID = Device.UUID;

        notify_Machine_Comms.newMachineFound( NewMachine);
    }






    //Used when  data send by remote device for a sent command
    void runCommandLoop()
    {
        if(CurrentCommandLoopState != CommandLoopStates.NA){
            CommandLoog_Trigger();
        }
    }

    enum CommandLoopStates {
         NA,
         SendCommand,
         RecieveData,
         AppendResponse
    }

    CommandLoopStates CurrentCommandLoopState = CommandLoopStates.NA;

    void CommandLoog_Trigger()  {

        switch(CurrentCommandLoopState)
        {

            case SendCommand:

                if(_currentCommsRequestContext.Data.size() > _currentCommsRequestContext.KineticsRequestIndex)
                {
                    _currentCommsRequestContext.KineticsRequestIndex = _currentCommsRequestContext.KineticsRequestIndex + 1;
                    CurrentCommandLoopState = CommandLoopStates.RecieveData;
                    _currentCommsRequestContext.CurrentResponse = "";
                    CommandLabels.CommandTypes ComamndType = _currentCommsRequestContext.Data.get(_currentCommsRequestContext.KineticsRequestIndex - 1).RequestType;


                    _currentCommsRequestContext.CurrenCommandResponseDataCount = CommandHelper.GetResponseCountForCommand(ComamndType);

                    if(_currentCommsRequestContext.CurrenCommandResponseDataCount > 0)
                    {
                        if(kineticsResponseTimeoutTimer != null)
                        {
                            kineticsResponseTimeoutTimer.cancel();
                        }

                        kineticsResponseTimeoutTimer = new Timer();
                        kineticsResponseTimeoutTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                _currentCommsRequestContext.IsCurrentCommandProcessingCompleted  = true;
                                CurrentCommandLoopState = CommandLoopStates.NA;

                                if(CommandBuffer.size() > 0)
                                {
                                    ArrayList<KineticsRequest> NextData = CommandBuffer.get(0).Command;
                                    UUID NextAcknowledgementKey = CommandBuffer.get(0).AcknowledgementKey;
                                    CommandBuffer.remove(0);
                                    StartNextCommandTransmission(NextData,  NextAcknowledgementKey);
                                }

                                KineticsResponseTimeout();
                            }
                        }, 4000);
                    }

                   // Log("Tx : "+_currentCommsRequestContext.Data[_currentCommsRequestContext.KineticsRequestIndex - 1].Request)

                    KineticsRequest request = _currentCommsRequestContext.Data.get(_currentCommsRequestContext.KineticsRequestIndex - 1);
                    Log.d(TAG, String.format("Tx: %s", new String(request.Request)));
                    HD_Comms.SendString(request.Request);

                    notify_Machine_Comms.RequestSent(request);
                }
                else
                {

                    if(kineticsResponseTimeoutTimer != null)
                        {
                                kineticsResponseTimeoutTimer.cancel();
                        }


                    UUID Final_Ack = _currentCommsRequestContext.Acknowlegment_Key;
                    ArrayList<KineticsResponse> Final_Response = _currentCommsRequestContext.ResponseData;

                    _currentCommsRequestContext.IsCurrentCommandProcessingCompleted  = true;
                    CurrentCommandLoopState = CommandLoopStates.NA;

                    if(CommandBuffer.size() > 0)
                    {
                        ArrayList<KineticsRequest> NextData = CommandBuffer.get(0).Command;
                        UUID NextAcknowledgementKey = CommandBuffer.get(0).AcknowledgementKey;
                        CommandBuffer.remove(0);
                        StartNextCommandTransmission(NextData,  NextAcknowledgementKey);
                    }


                    Log.e("MachineComms", "Sending recieved data");
                    notify_Machine_Comms.RecievedResponseData(Final_Response, Final_Ack);
                    Log.e("MachineComms", "Sent recieved data");
                }
                break;

            case RecieveData:

                _currentCommsRequestContext.CurrentResponse = _currentCommsRequestContext.CurrentResponse + _commsDataRecievingContext.NewDataRecieved;
                _commsDataRecievingContext.NewDataRecieved = "";

                if(_currentCommsRequestContext.CurrentResponse.endsWith(":"))
                {
                    if(kineticsResponseTimeoutTimer != null)
                    {
                            kineticsResponseTimeoutTimer.cancel();
                    }

                    String[] Responses = _currentCommsRequestContext.CurrentResponse.replaceAll("^:", "").split(":");
                    Integer NumberOfResponses = Responses.length;


                    if(NumberOfResponses == _currentCommsRequestContext.CurrenCommandResponseDataCount)
                    {
                        //print("Rx : "+_currentCommsRequestContext.CurrentResponse)
                        CurrentCommandLoopState = CommandLoopStates.AppendResponse;

                        CommandLoog_Trigger();
                    }
                    else if (NumberOfResponses < _currentCommsRequestContext.CurrenCommandResponseDataCount)
                    {
                        CurrentCommandLoopState = CommandLoopStates.RecieveData;
                    }
                }

                break;

            case AppendResponse:

                KineticsResponse kineticsReponse = KineticsResponse.GetResponseObject(_currentCommsRequestContext.CurrentResponse);
                Log.d(TAG, String.format("Response  : %s, Response Type : %s", new String(_currentCommsRequestContext.CurrentResponse), kineticsReponse.ResponseType.toString()));
                _currentCommsRequestContext.ResponseData.add(kineticsReponse);

                if(kineticsReponse.ResponseType == CommandLabels.CommandTypes.TRG || kineticsReponse.ResponseType == CommandLabels.CommandTypes.ITRG)
                {
                    notify_Machine_Comms.ParameterTriggerSuccuss();
                }

                CurrentCommandLoopState = SendCommand;

                CommandLoog_Trigger();

                break;

            default:
                break;

        }


    }

    long LastCommandSentTime = System.currentTimeMillis();
    //Command is a set of KineticsRequests that will be sent in sequence and whose ackknowledgements will be packed together and sent back
    void StartNextCommandTransmission(ArrayList<KineticsRequest> _Command, UUID _Acknowledgement)
    {

        Log.e("MachineComms", "StartNextCommandTransmission Ack : "+_Acknowledgement.toString());
        LastCommandSentTime = System.currentTimeMillis();
        _currentCommsRequestContext.IsCurrentCommandProcessingCompleted  = false;

        _currentCommsRequestContext.Data =  _Command;
        _currentCommsRequestContext.ResponseData = new ArrayList<KineticsResponse>();
        _currentCommsRequestContext.CurrenCommandResponseDataCount = 0;
        _currentCommsRequestContext.CurrentResponse   = "";

        _currentCommsRequestContext.KineticsRequestIndex = 0;
        CurrentCommandLoopState = SendCommand;
        _currentCommsRequestContext.Acknowlegment_Key = _Acknowledgement;
        CommandLoog_Trigger();
    }

  public   void ClearCurrentTransmissionContext()
    {
        _currentCommsRequestContext.IsCurrentCommandProcessingCompleted = true;
        CommandBuffer.clear();
    }


  public ParcelUuid SendData(ArrayList<KineticsRequest> _Command)
    {
        UUID _Acknowledgement = UUID.randomUUID();
        CommandBuffer.add(new NewCommandRequest( _Acknowledgement,  _Command));
        if(_currentCommsRequestContext.IsCurrentCommandProcessingCompleted == true ||  (LastCommandSentTime - System.currentTimeMillis() > 3000))
        {
            if(CommandBuffer.size()  > 2)
            CommandBuffer.remove(0);

            Thread thread = new Thread(new SerialCommandTxRunnable(_Command, _Acknowledgement) {
                @Override
                public void run() {
                    RemoteCommandToMachineRequest convertor = new RemoteCommandToMachineRequest();

                    StartNextCommandTransmission( Command,  Acknowledgement);

                }
            });


            thread.start();

            return new ParcelUuid(_Acknowledgement);
        }
        else
        {
            return new ParcelUuid(_Acknowledgement);
        }
    }



    public void stringRecieved(String Data) {
        String RecievedData = Data;

        if(RecievedData.contains("~") || _commsDataRecievingContext.IsWaitingForPendingRemoteRequestData)
        {
            for(char c : RecievedData.toCharArray())
            {
                if(c == '~')
                {
                    _commsDataRecievingContext.IsWaitingForPendingRemoteRequestData  =  true;
                }
                else
                if(c == ':' && _commsDataRecievingContext.IsWaitingForPendingRemoteRequestData)
                {
                    _commsDataRecievingContext.IsWaitingForPendingRemoteRequestData  =  false;

                  final String request = _commsDataRecievingContext.RemoteRequestData;

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                    RemoteCommandToMachineRequest convertor = new RemoteCommandToMachineRequest();
                                    if(notify_Machine_Comms != null) {
                                        try {
                                            notify_Machine_Comms.RecievedRemoteCommand(convertor.Convert(request));
                                        }
                                        catch (Exception e)
                                        {
                                            //Pass if there is no client reciever for the remote command
                                            // or reciever error
                                        }
                                    }
                        }
                    });

                    thread.start();

                    _commsDataRecievingContext.RemoteRequestData = "";
                }
                else
                if(_commsDataRecievingContext.IsWaitingForPendingRemoteRequestData)
                {
                    _commsDataRecievingContext.RemoteRequestData = _commsDataRecievingContext.RemoteRequestData + (c);
                }
                else
                {
                    _commsDataRecievingContext.NewDataRecieved = _commsDataRecievingContext.NewDataRecieved + (c);
                }
            }

            if(_commsDataRecievingContext.IsWaitingForPendingRemoteRequestData == false)
            {
                if(_commsDataRecievingContext.NewDataRecieved != "")
                {
                    runCommandLoop();
                }
            }
        }
        else
        {
            _commsDataRecievingContext.NewDataRecieved = RecievedData;
            runCommandLoop();

        }
    }

    public UsbDevice GetUSBSerialDevice()
    {
       return HD_Comms.GetUSBSerialDevice();
    }
}
