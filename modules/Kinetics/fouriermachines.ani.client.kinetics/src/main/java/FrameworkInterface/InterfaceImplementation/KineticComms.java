package FrameworkInterface.InterfaceImplementation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.os.ParcelUuid;
import android.util.Log;

import Framework.DataTypes.Constants.ActuatorPowerStatusSymbols;
import Framework.DataTypes.Constants.ActuatorSignalStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import Framework.DataTypes.Constants.ProximityStateSymbols;
import Framework.DataTypes.Delegates.MachineCommsConvey;
import Framework.DataTypes.Delegates.ServiceConnectionBindRequestConvey;
import Framework.DataTypes.Delegates.ServiceConnectionEstablishedConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOn;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestConnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDisconnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestInstantTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOffLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOnLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestProximityRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoDegree;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoMotionCheck;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockSignalStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseProximityRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoDegree;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoSignalStatus;
import Framework.MachineCommunicationEngine;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.KineticsBindStatusConveyConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.KineticsParameterUpdatesConveyConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.KineticsRemoteRequestConveyConnection;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.KineticsResponseConveyConnection;
import FrameworkInterface.KineticsAccess;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.IKineticsBindStatusConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsParameterUpdatesConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsRemoteRequestConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsResponseConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsParameterUpdatesConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import FrameworkInterface.PublicTypes.Machine;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Machine_Position_Type;
import fm.ani.client.db.DataTypes.CommandStore.Servo_Calibration_Type;

public class KineticComms implements KineticsAccess, MachineCommsConvey {

    private KineticComms()
    {
        machineComms = new MachineCommunicationEngine();
    }

    public   static  KineticsAccess Instance = new KineticComms();

    private static final String PARAM_UPDATE_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.KineticsParameterUpdatesConveyService";
    private static final String REMOTE_REQ_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.KineticsRemoteRequestConveyService";
    private static final String RESP_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.KineticsResponseConveyService";
    private static final String BIND_STATE_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.KineticsBindStatusConveyService";


    Map<String, Map<String,ServiceConnection>> KineticsConveyConnections = new HashMap();

    public void ConnectToKineticServices(Map<String, String> KineticsConveyList)
    {
        for (Map.Entry<String,String> keypair: KineticsConveyList.entrySet()) {
            Intent i = new Intent();
            i.setClassName(keypair.getKey(), keypair.getValue());


            if(KineticsConveyConnections.containsKey(keypair.getValue())) {
                if (KineticsConveyConnections.get(keypair.getValue()).containsKey(keypair.getKey())) {
                    try {
                        GlobalContext.context.unbindService(KineticsConveyConnections.get(keypair.getValue()).remove(keypair.getKey()));
                    }
                    catch (Exception e)
                    {
                        Log.e("KineticComms", e.getMessage());
                    }
                    KineticsConveyConnections.get(keypair.getValue()).remove(keypair.getKey());
                }
            }
            else
                KineticsConveyConnections.put(keypair.getValue(), new HashMap<String, ServiceConnection>());


            switch (keypair.getValue())
            {
                case RESP_CONVEY:
                    KineticsConveyConnections.get(RESP_CONVEY).put(keypair.getKey(), new KineticsResponseConveyConnection());
                    break;
                case REMOTE_REQ_CONVEY:
                    KineticsConveyConnections.get(REMOTE_REQ_CONVEY).put(keypair.getKey(), new KineticsRemoteRequestConveyConnection());
                    break;
                case PARAM_UPDATE_CONVEY:
                    KineticsConveyConnections.get(PARAM_UPDATE_CONVEY).put(keypair.getKey(), new KineticsParameterUpdatesConveyConnection());
                    break;
                case BIND_STATE_CONVEY: {
                    BIND_STATE_CONVEY_ID = keypair.getKey();
                    KineticsConveyConnections.get(BIND_STATE_CONVEY).put(keypair.getKey(), new KineticsBindStatusConveyConnection(notify_ServiceConnectionEstablishedConvey));
                    break;
                }
                    default:
                        break;
            }
            ServiceConnection connection = KineticsConveyConnections.get(keypair.getValue()).get(keypair.getKey());

            try {
                boolean ret = GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
            catch (Exception e)
            {
                Log.e("AIManager", "ConnectToAiServices : "+e.getMessage());
            }
        }
    }

    private String REMOTE_REQ_CONVEY_ID = "";
    private String RESP_CONVEY_ID = "";
    private String BIND_STATE_CONVEY_ID = "";

    IKineticsParameterUpdatesConveyAIDL notifyParameterUpdates()
    {
        if(KineticsConveyConnections.containsKey(PARAM_UPDATE_CONVEY))
        {
            if(KineticsConveyConnections.get(PARAM_UPDATE_CONVEY).containsKey(RESP_CONVEY_ID))
            {
                ServiceConnection serviceConnection =   (KineticsConveyConnections.get(PARAM_UPDATE_CONVEY).get(RESP_CONVEY_ID));
                if(((KineticsParameterUpdatesConveyConnection)serviceConnection).getService() != null) {
                    return ((KineticsParameterUpdatesConveyConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }

    IKineticsResponseConveyAIDL notifyResponseConvey()
    {
        if(KineticsConveyConnections.containsKey(RESP_CONVEY))
        {
            if(KineticsConveyConnections.get(RESP_CONVEY).containsKey(RESP_CONVEY_ID))
            {
                ServiceConnection serviceConnection =   (KineticsConveyConnections.get(RESP_CONVEY).get(RESP_CONVEY_ID));
                if(((KineticsResponseConveyConnection)serviceConnection).getService() != null) {
                    return ((KineticsResponseConveyConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }

    ArrayList<IKineticsRemoteRequestConveyAIDL> notifyRemoteResponseConvey()
    {
        ArrayList<IKineticsRemoteRequestConveyAIDL> remoteCommandConvey = new ArrayList<>();
        if(KineticsConveyConnections.containsKey(REMOTE_REQ_CONVEY))
        {
            ArrayList<String> nullKeys = new ArrayList<>();
            for(Map.Entry<String, ServiceConnection> connections : KineticsConveyConnections.get(REMOTE_REQ_CONVEY).entrySet())
            {
                if(connections.getValue() != null) {
                    ServiceConnection serviceConnection = connections.getValue();
                    if(serviceConnection != null && ((KineticsRemoteRequestConveyConnection) serviceConnection).getService() != null)
                    remoteCommandConvey.add(((KineticsRemoteRequestConveyConnection) serviceConnection).getService());
                    else
                        nullKeys.add(connections.getKey());
                }
            }

            for(String key : nullKeys)
            {
                KineticsConveyConnections.get(REMOTE_REQ_CONVEY).remove(key);
            }


        }
        return  remoteCommandConvey;
    }

    IKineticsBindStatusConveyAIDL notifyBindStatusConvey()
    {
        if(KineticsConveyConnections.containsKey(BIND_STATE_CONVEY))
        {
            if(BIND_STATE_CONVEY_ID.isEmpty() &&
                    KineticsConveyConnections.get(BIND_STATE_CONVEY).size() > 0 &&
                    KineticsConveyConnections.get(BIND_STATE_CONVEY).values().toArray()[0] != null)
            {
                if(((KineticsBindStatusConveyConnection)KineticsConveyConnections.get(BIND_STATE_CONVEY).values().toArray()[0]).getService() != null)
                {
                    return ((KineticsBindStatusConveyConnection)KineticsConveyConnections.get(BIND_STATE_CONVEY).values().toArray()[0]).getService();
                }
            }
            else
            if(KineticsConveyConnections.get(BIND_STATE_CONVEY).containsKey(BIND_STATE_CONVEY_ID))
            {
                ServiceConnection serviceConnection =   (KineticsConveyConnections.get(BIND_STATE_CONVEY).get(BIND_STATE_CONVEY_ID));
                if(((KineticsBindStatusConveyConnection)serviceConnection).getService() != null) {
                    return ((KineticsBindStatusConveyConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }



    public void RequestSent(KineticsRequest request) {
        try {
            if(notifyParameterUpdates() != null) {
                notifyParameterUpdates().ParameterUpdated(request);
            }
        }
        catch (Exception e)
        {}
    }

    public void ParameterTriggerSuccuss() {
        try {
            if(notifyParameterUpdates() != null) {
                notifyParameterUpdates().ParametersSetSuccessfully();
            }
        }
        catch (Exception e)
        {}
    }


    public void KineticsResponseDataTimeout(UUID uuid, ArrayList<KineticsResponse> PartialResponse) {

        if(notify_KineticsResponse != null)
            notify_KineticsResponse.MachiResponseTimeout(PartialResponse, uuid);

        try {
            ParcelUuid ack = ParcelUuid.fromString(uuid.toString());
            if(notifyResponseConvey() != null) {
                notifyResponseConvey().MachiResponseTimeout(PartialResponse, ack);
            }
        }
        catch (Exception e)
        {}
    }

    public void RecievedRemoteCommand(MachineRequests event)
    {
        try {
           ArrayList<IKineticsRemoteRequestConveyAIDL> list = notifyRemoteResponseConvey();
            if(notifyRemoteResponseConvey().size() > 0) {
                for(IKineticsRemoteRequestConveyAIDL delegate : list)
                    if(delegate != null)
                    delegate.machineRequested(event.toString());
            }
        }
        catch (Exception e)
        {
            Log.e("KINETIC_COMMS", e.getMessage(), e);
        }
    }

    public void RecievedResponseData(ArrayList<KineticsResponse> responeData,  UUID _Acknowledgement) {

        if(notify_KineticsResponse != null)
            notify_KineticsResponse.MachineResponseRecieved(responeData, _Acknowledgement);

        ParcelUuid ack = ParcelUuid.fromString(_Acknowledgement.toString());

        try {
            if(notifyResponseConvey() != null) {
                notifyResponseConvey().MachineResponseRecieved(responeData, ack);
            }
        }
        catch (Exception e)
        {}
    }

    public void ConveyBindStatus(String state) {
        try {
            if(notifyBindStatusConvey() != null) {
                notifyBindStatusConvey().KineticsBindStatus(state);
            }
            }
        catch (Exception e)
        {
            Log.e("KineticComms", e.getMessage());
        }
    }


    public void commsStateChanged(MachineCommsStates State) {

        if(notify_KineticsResponse != null && State != MachineCommsStates.CONNECTED)
        {
            notify_KineticsResponse.CommsLost();
        }
        if(notify_KineticsComms != null)
        {
            notify_KineticsComms.commsStateChanged(State);
        }

    }

    public void newMachineFound(Machine Device) {
        if(notify_KineticsComms != null){
            notify_KineticsComms.newMachineFound(Device);
        }
    }

    //Binder Calls
    public void BindMachine(String connectionID)
    {
        BIND_STATE_CONVEY_ID = connectionID;
        if(notify_ServiceConnectionBindRequestConvey != null)
            notify_ServiceConnectionBindRequestConvey.BindMachine();
    }

    public void UnBindMachine(String connectionID)
    {
        BIND_STATE_CONVEY_ID = connectionID;
        if(notify_ServiceConnectionBindRequestConvey != null)
            notify_ServiceConnectionBindRequestConvey.UnBindMachine();
    }
    //End of Binder Calls

    public MachineConfig GetMachineConfig()
    {
        return  MachineConfig.Instance;
    }

    public ParcelUuid IndicateAttention(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestAttentionOn();
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid IndicateNoAttention(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestAttentionOff();
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }


    public ParcelUuid ReadDegree(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestServoDegree(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadProximity(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestProximityRead(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadMotionState(String connectionID, Actuator actuatorType) {
        RESP_CONVEY_ID = connectionID;
       final KineticsRequest request = new KineticsRequestServoMotionCheck(actuatorType);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadActuatorPowerStatus(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
       final KineticsRequest request = new KineticsRequestServoPowerStatus(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadActuatorSignalStatus(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestServoSignalStatus(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadLockPowerStatus(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestLockPowerStatus(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid ReadLockSignalStatus(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestLockSignalStatus(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }


    public ParcelUuid PowerOnMotor(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestConnectPower(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});

        return ACK;
    }

    public ParcelUuid PowerOffMotor(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestDisconnectPower(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid AttachSignalToActuator(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestAttachServo(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid DetachSignalToActuator(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestDettachServo(actuator);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid PowerOnLockMotor(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestPowerOnLockServo(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid PowerOffLockMotor(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestPowerOffLockServo(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid AttachSignalToALockctuator(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestAttachLockServo(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid DetachSignalToLockActuator(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestDettachLockServo(Actuator.TILT);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid StartInstantMotion(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestInstantTrigger();
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public ParcelUuid StartMotion(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestTrigger();
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public Boolean CheckProximity(ArrayList<KineticsResponse> response)
    {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.PRX)
        {
            KineticsResponseProximityRead ProximityResponse = ((KineticsResponseProximityRead)response.get(0));

            if(ProximityResponse != null && ProximityResponse.MountState == ProximityStateSymbols.MOUNTED)
            {
                return true;
            }
        }
        return false;
    }

    public Boolean CheckActuatorPowerStatus(ArrayList<KineticsResponse> response) {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.SPW)
        {
            KineticsResponseServoPowerStatus PowerStatusResponse  = ((KineticsResponseServoPowerStatus)response.get(0));

            if(PowerStatusResponse != null && PowerStatusResponse.PowerState == ActuatorPowerStatusSymbols.ON)
            {
                return true;
            }
        }
        return false;
    }

    public Boolean CheckActuatorSignalStatus(ArrayList<KineticsResponse> response) {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.SAT)
        {
            KineticsResponseServoSignalStatus SignalStatusResponse  = ((KineticsResponseServoSignalStatus)response.get(0));

            if(SignalStatusResponse != null && SignalStatusResponse.SignalState == ActuatorSignalStatusSymbols.ATTACHED)
            {
                return true;
            }
        }
        return false;
    }

    public Boolean CheckLockPowerStatus(ArrayList<KineticsResponse> response) {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.LPW)
        {
            KineticsResponseLockPowerStatus LockPowerStatusResponse = ((KineticsResponseLockPowerStatus)response.get(0));

            if(LockPowerStatusResponse != null && LockPowerStatusResponse.PowerState == ActuatorPowerStatusSymbols.ON)
            {
                return true;
            }
        }
        return false;
    }

    public Boolean CheckLockSignalStatus(ArrayList<KineticsResponse> response) {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.LAT)
        {
            KineticsResponseLockSignalStatus LockSignalStatusResponse  = ((KineticsResponseLockSignalStatus)response.get(0));

            if(LockSignalStatusResponse != null && LockSignalStatusResponse.SignalState == ActuatorSignalStatusSymbols.ATTACHED)
            {
                return true;
            }
        }
        return false;
    }


    public ParcelUuid ReadActuatorAddress(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).ActuatorAddressLocationInEERPOM));
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public Boolean SetActuatorAddress(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.EEPR)
        {
            KineticsResponseEEPROMRead EEPROMRead  = ((KineticsResponseEEPROMRead)response.get(0));
            if(EEPROMRead != null && EEPROMRead.ResponseType == CommandLabels.CommandTypes.EEPR && EEPROMRead.Data.size() > 0)
            {
                MachineConfig.Instance.MachineActuatorList.get(actuator).Address = EEPROMRead.Data.get(0);
                return true;
            }
        }

        return false;

    }

    public ParcelUuid ReadDeltaResetAngle(String connectionID, Actuator actuator){
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).ShutdownAngleAddressLocationInEERPOM));
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public Boolean SetDeltaResetAngle(Actuator actuator,ArrayList<KineticsResponse> response)
    {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.EEPR) {
            KineticsResponseEEPROMRead EEPROMRead = ((KineticsResponseEEPROMRead) response.get(0));
            if (EEPROMRead != null && EEPROMRead.ResponseType == CommandLabels.CommandTypes.EEPR && EEPROMRead.Data.size() > 0) {
                MachineConfig.Instance.MachineActuatorList.get(actuator).ShutdownDeltaAngle = EEPROMRead.Data.get(0);
                if(EEPROMRead.Data.get(1) == 1)
                {
                    MachineConfig.Instance.MachineActuatorList.get(actuator).ShutdownDeltaAngle = MachineConfig.Instance.MachineActuatorList.get(actuator).ShutdownDeltaAngle * -1;
                }
                return true;
            }
        }
        return false;
    }

    public ParcelUuid ReadReferanceAngle(String connectionID, Actuator actuator) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).RefAngleAddressLocationInEERPOM));
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public Boolean SetReferanceAngle(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.EEPR)
        {
            KineticsResponseEEPROMRead EEPROMRead = ((KineticsResponseEEPROMRead)response.get(0));
            if(EEPROMRead != null && EEPROMRead.ResponseType == CommandLabels.CommandTypes.EEPR && EEPROMRead.Data.size() > 0)
            {
                MachineConfig.Instance.MachineActuatorList.get(actuator).RefPosition = EEPROMRead.Data.get(0);
                return true;
            }
        }

        return false;
    }

    public KineticsRequestAngle GetKineticRequestAngleFromDegreeResponse(Actuator actuator, ArrayList<KineticsResponse> response)
    {
        DB_Local_Store db = new DB_Local_Store();
        KineticsRequestAngle request;
        if(response.size() == 1 && response.get(0).ResponseType == CommandLabels.CommandTypes.DEG)
        {
            KineticsResponseServoDegree ADCRead = ((KineticsResponseServoDegree)(response.get(0)));
            if(ADCRead != null)
            {
                Integer Degree = db.GetServoDegreeFromADC((MachineConfig.Instance.MachineActuatorList.get(actuator).CalibrationStoreName), ADCRead.ADC);
                if(Degree == -1)
                {
                    return null;
                }
                request = new KineticsRequestAngle((Degree), actuator);
                return request;
            }
        }

        return null;
    }

    public Integer GetDeltaAngleFromFullAngle(Integer FullAngle, Actuator actuator)
    {
        return (FullAngle - (MachineConfig.Instance.MachineActuatorList.get(actuator).RefPosition));
    }


    public Integer GetFullAngleFromDeltaAngle(Integer DeltaAngle, Actuator actuator)
    {
        return (DeltaAngle + (MachineConfig.Instance.MachineActuatorList.get(actuator).RefPosition));
    }

    public ArrayList<KineticsRequest> GetPredefinedKineticsRequestAngleByName(String Name)
    {    DB_Local_Store db = new DB_Local_Store();
        Machine_Position_Type Command = db.ReadMachinePositionByName( Name);
        ArrayList<KineticsRequest> kineticsRequests = new ArrayList<KineticsRequest>();
        if(Command.Name.equals(Name))
        {
            if(Command.TURN != null)
            {
                Integer FullTurnAngle = (Command.TURN) + (MachineConfig.Instance.MachineActuatorList.get(Actuator.TURN).RefPosition);
                kineticsRequests.add(new KineticsRequestAngle(FullTurnAngle, Actuator.TURN));
            }

            if(Command.LIFT != null)
            {
                Integer FullLiftAngle = (Command.LIFT) + (MachineConfig.Instance.MachineActuatorList.get(Actuator.LIFT).RefPosition);
                kineticsRequests.add(new KineticsRequestAngle(FullLiftAngle, Actuator.LIFT));
            }

            if(Command.LEAN != null)
            {
                Integer FullLeanAngle = (Command.LEAN) + (MachineConfig.Instance.MachineActuatorList.get(Actuator.LEAN).RefPosition);
                kineticsRequests.add(new KineticsRequestAngle(FullLeanAngle, Actuator.LEAN));
            }

            if(Command.TILT != null)
            {
                Integer FullTiltAngle = (Command.TILT) + (MachineConfig.Instance.MachineActuatorList.get(Actuator.TILT).RefPosition);
                kineticsRequests.add(new KineticsRequestAngle(FullTiltAngle, Actuator.TILT));
            }
        }
        return kineticsRequests;

    }

    public ParcelUuid SetParameters(String connectionID, ArrayList<KineticsRequest> parameters) {
        RESP_CONVEY_ID = connectionID;
        ParcelUuid ACK = machineComms.SendData(parameters);
        return ACK;
    }




    MachineCommunicationEngine machineComms;


    //Machine Communication Delegate
    KineticsCommsConvey notify_KineticsComms;

    //Machine remote events Delegate
    KineticsRemoteRequestConvey notify_KineticsRemoteRequest;


    //Machine response Delegate
    KineticsResponseConvey notify_KineticsResponse;

    //Machine Parameter Updates Delegate
    KineticsParameterUpdatesConvey notify_KineticsParameterUpdates;

    ServiceConnectionEstablishedConvey notify_ServiceConnectionEstablishedConvey;

    ServiceConnectionBindRequestConvey notify_ServiceConnectionBindRequestConvey;





    //This variable will be used to initialize Comms delegates first only
    public Boolean IsCommsDelegatesInitialized = false;

    //Manadatory call to make the Machine Communication system ready
    public void SetCommsDelegate(KineticsCommsConvey delegate)
    {
        notify_KineticsComms = delegate;

        //Set BT comms Delegates first time this method is called
        //This will be set only once, every time the is application run
        if(!IsCommsDelegatesInitialized)
        {
            IsCommsDelegatesInitialized = true;
            machineComms.SetCommsDelegate(this);
        }
    }
    public void InitializeComms()
    {
        machineComms.InitializeComms();
    }

    //Returns the SUB Serial convertor
    public UsbDevice GetUSBSerialDevice() { return machineComms.GetUSBSerialDevice(); }

    //Start scanning of machines
    public void StartScan()
    {
        machineComms.StartScan();
    }

    //Stops scanning for machines
    public void StopScan()
    {
        machineComms.StopScan();
    }




    public void ConnectToMachine(Machine machine)
    {
        machineComms.ConnectToMachine(machine);
    }

    public Boolean IsConnectedToMachine()
    {
        return machineComms.IsConnectedToMachine();
    }

    public void DisconnectMachine()
    {
        machineComms.DisconnectMachine();
    }


    public void SetMachineRemoteRequestListener(KineticsRemoteRequestConvey delegate)
    {
        notify_KineticsRemoteRequest = delegate;
    }

    public void SetKineticsResposeListener(KineticsResponseConvey delegate)
    {

        notify_KineticsResponse = delegate;
    }

    public void SetKineticsParameterUpdatesListener(KineticsParameterUpdatesConvey delegate)
    {
        notify_KineticsParameterUpdates = delegate;
    }


    public void SetServiceConnectionEstablishedConveyListener(ServiceConnectionEstablishedConvey delegate)
    {
        notify_ServiceConnectionEstablishedConvey = delegate;
    }


    public void SetServiceConnectionBindRequestConveyListener(ServiceConnectionBindRequestConvey delegate)
    {
        notify_ServiceConnectionBindRequestConvey = delegate;
    }


    public void ResetCommsContext()
    {
        machineComms.ClearCurrentTransmissionContext();
    }

    public ParcelUuid TurnOffPower(String connectionID) {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestPowerOff();
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    public  ParcelUuid WriteToEEPROM(String connectionID, EEPROMDetails AddressDetails, Integer Data)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestEEPROMWrite(AddressDetails, Data);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    public  ParcelUuid ReadFromEEPROM(String connectionID, EEPROMDetails AddressDetails)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestEEPROMRead(AddressDetails);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    public  ArrayList<Integer> ExtractBytesFromEEPROMReadResponse(KineticsResponse EEPROMReadResponse)
    {
        if(EEPROMReadResponse.ResponseType == CommandLabels.CommandTypes.EEPR) {
            KineticsResponseEEPROMRead readResponse = (KineticsResponseEEPROMRead) EEPROMReadResponse;
            if (readResponse != null)
                return readResponse.Data;
            else
                return null;
        }
        return null;
    }

    public  Boolean CheckWriteToEEPROM(KineticsResponse Response)
    {
        if(Response.ResponseType == CommandLabels.CommandTypes.EEPW)
        {
            KineticsResponseEEPROMWrite EEPROMWrite = ((KineticsResponseEEPROMWrite)Response);
            if(EEPROMWrite != null && EEPROMWrite.ResponseType == CommandLabels.CommandTypes.EEPW && EEPROMWrite.RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                return true;
            }
        }

        return false;
    }


    //read given number of bytes from from Servo board eeprom Address location
    public  ParcelUuid ReadFromServoEEPROM(String connectionID, Actuator actuator, EEPROMDetails AddressDetails)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestServoEEPROMRead(actuator, AddressDetails);
        ParcelUuid ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    //Returns the payload bytes from ServoEEPROMReadResonse Command
    public ArrayList<Integer> ExtractBytesFromServoEEPROMReadResponse(KineticsResponse EEPROMReadResponse)
    {
        if(EEPROMReadResponse.ResponseType == CommandLabels.CommandTypes.SEPR) {
            KineticsResponseServoEEPROMRead readResponse = (KineticsResponseServoEEPROMRead) EEPROMReadResponse;
            if (readResponse != null)
                return readResponse.Data;
            else
                return null;
        }
        return null;
    }

    public  void DeleteCalibrationDataForActuator(Actuator actuator)
    {
        DB_Local_Store db = new DB_Local_Store();
        db._EmptyTable(MachineConfig.Instance.MachineActuatorList.get(actuator).CalibrationStoreName.toString());
    }

    public  void SaveActuatorCalibrationData(Actuator actuator, Integer Degree, Integer ADC )
    {
        Servo_Calibration_Type calibType = new Servo_Calibration_Type();
        calibType.Name = (MachineConfig.Instance.MachineActuatorList.get(actuator).CalibrationStoreName).toString();
        calibType.Degree = Degree;
        calibType.ADC = ADC;
        DB_Local_Store db = new DB_Local_Store();
        db.saveCalibrationData(calibType);
    }




    public  ParcelUuid WriteToISLEEPROM(String connectionID, Integer Address,Integer numberOfBytes, Integer Value)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestISLEEPROMWrite(numberOfBytes, Address, Value);
        return machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
    }

    public  ParcelUuid ReadFromISLEEPROM(String connectionID, Integer Address, Integer numberOfBytes)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestISLEEPROMRead(numberOfBytes, Address);
        return machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
    }

    public  ParcelUuid WriteToISLRAM(String connectionID, Integer Address,Integer numberOfBytes, Integer Value)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestISLWrite(numberOfBytes, Address, Value);
        return machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
    }

    public  ParcelUuid ReadFromISLRAM(String connectionID, Integer Address, Integer numberOfBytes)
    {
        RESP_CONVEY_ID = connectionID;
        final KineticsRequest request = new KineticsRequestISLRead(numberOfBytes, Address);
        return machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
    }


    //Returns the payload bytes from ISLEEPROMReadResonse Command
    public  ArrayList<Integer> ExtractBytesFromISLEEPROMReadResponse(KineticsResponse ISLEEPROMReadResponse)
    {
        if(ISLEEPROMReadResponse.ResponseType == CommandLabels.CommandTypes.ISLER) {
            KineticsResponseISLEEPROMRead readResponse = (KineticsResponseISLEEPROMRead) ISLEEPROMReadResponse;
            if (readResponse != null)
                return readResponse.Data;
            else
                return null;
        }
        return null;
    }


    //Returns the payload bytes from ISLRAMReadResonse Command
    public  ArrayList<Integer> ExtractBytesFromISLRAMReadResponse(KineticsResponse ISLRAMReadResponse)
    {
        if(ISLRAMReadResponse.ResponseType == CommandLabels.CommandTypes.ISLR) {
            KineticsResponseISLRead readResponse = (KineticsResponseISLRead) ISLRAMReadResponse;
            if (readResponse != null)
                return readResponse.Data;
            else
                return null;
        }
        return null;
    }



    public  Boolean CheckWriteToISLEEPROM(KineticsResponse Response)
    {
        if(Response.ResponseType == CommandLabels.CommandTypes.ISLEW)
        {
            KineticsResponseISLEEPROMWrite EEPROMWrite = ((KineticsResponseISLEEPROMWrite)Response);
            if(EEPROMWrite != null && EEPROMWrite.ResponseType == CommandLabels.CommandTypes.ISLEW && EEPROMWrite.RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                return true;
            }
        }

        return false;
    }

    public  Boolean CheckWriteToISLRAM(KineticsResponse Response)
    {
        if(Response.ResponseType == CommandLabels.CommandTypes.ISLW)
        {
            KineticsResponseISLWrite EEPROMWrite = ((KineticsResponseISLWrite)Response);
            if(EEPROMWrite != null && EEPROMWrite.ResponseType == CommandLabels.CommandTypes.ISLW && EEPROMWrite.RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                return true;
            }
        }

        return false;
    }
}
