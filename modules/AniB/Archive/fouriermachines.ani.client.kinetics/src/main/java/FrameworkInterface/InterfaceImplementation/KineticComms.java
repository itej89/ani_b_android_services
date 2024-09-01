package FrameworkInterface.InterfaceImplementation;

import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.ActuatorPowerStatusSymbols;
import Framework.DataTypes.Constants.ActuatorSignalStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import Framework.DataTypes.Constants.ProximityStateSymbols;
import Framework.DataTypes.Delegates.MachineCommsConvey;
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
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockSignalStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseProximityRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoDegree;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoSignalStatus;
import Framework.MachineCommunicationEngine;
import FrameworkInterface.KineticsAccess;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
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


    public void RequestSent(KineticsRequest request) {
        notify_KineticsParameterUpdates.ParameterUpdated(request);
    }

    public void ParameterTriggerSuccuss() {
        notify_KineticsParameterUpdates.ParametersSetSuccessfully();
    }



    public void KineticsResponseDataTimeout(UUID uuid, ArrayList<KineticsResponse> PartialResponse) {
        if(notify_KineticsResponse != null){
            notify_KineticsResponse.MachiResponseTimeout(PartialResponse,  uuid);
        }
    }

    public void RecievedRemoteCommand(MachineRequests event)
    {
        if(notify_KineticsRemoteRequest != null){
            notify_KineticsRemoteRequest.machineRequested( event);
        }
    }

    public void RecievedResponseData(ArrayList<KineticsResponse> responeData,  UUID _Acknowledgement) {

        if(notify_KineticsResponse != null){
            notify_KineticsResponse.MachineResponseRecieved( responeData,  _Acknowledgement);
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




    public UUID IndicateAttention() {
        final KineticsRequest request = new KineticsRequestAttentionOn();
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID IndicateNoAttention() {
        final KineticsRequest request = new KineticsRequestAttentionOff();
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }


    public UUID ReadDegree(Actuator actuator) {
        final KineticsRequest request = new KineticsRequestServoDegree(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadProximity() {
        final KineticsRequest request = new KineticsRequestProximityRead(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadMotionState(Actuator actuatorType) {
       final KineticsRequest request = new KineticsRequestServoMotionCheck(actuatorType);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadActuatorPowerStatus(Actuator actuator) {

       final KineticsRequest request = new KineticsRequestServoPowerStatus(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadActuatorSignalStatus(Actuator actuator) {

        final KineticsRequest request = new KineticsRequestServoSignalStatus(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadLockPowerStatus() {

        final KineticsRequest request = new KineticsRequestLockPowerStatus(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID ReadLockSignalStatus() {

        final KineticsRequest request = new KineticsRequestLockSignalStatus(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }


    public UUID PowerOnMotor(Actuator actuator) {

        final KineticsRequest request = new KineticsRequestConnectPower(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});

        return ACK;
    }

    public UUID PowerOffMotor(Actuator actuator) {

        final KineticsRequest request = new KineticsRequestDisconnectPower(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID AttachSignalToActuator(Actuator actuator) {

        final KineticsRequest request = new KineticsRequestAttachServo(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID DetachSignalToActuator(Actuator actuator) {

        final KineticsRequest request = new KineticsRequestDettachServo(actuator);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID PowerOnLockMotor() {

        final KineticsRequest request = new KineticsRequestPowerOnLockServo(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID PowerOffLockMotor() {

        final KineticsRequest request = new KineticsRequestPowerOffLockServo(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID AttachSignalToALockctuator() {

        final KineticsRequest request = new KineticsRequestAttachLockServo(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID DetachSignalToLockActuator() {

        final KineticsRequest request = new KineticsRequestDettachLockServo(Actuator.TILT);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID StartInstantMotion() {
        final KineticsRequest request = new KineticsRequestInstantTrigger();
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
        return ACK;
    }

    public UUID StartMotion() {
        final KineticsRequest request = new KineticsRequestTrigger();
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
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


    public UUID ReadActuatorAddress(Actuator actuator) {
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).ActuatorAddressLocationInEERPOM));
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
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

    public UUID ReadDeltaResetAngle(Actuator actuator)
    {
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).ShutdownAngleAddressLocationInEERPOM));
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
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

    public UUID ReadReferanceAngle(Actuator actuator) {
        final KineticsRequest request = new KineticsRequestEEPROMRead(new KineticsEEPROM().MEMORY_MAP.get(MachineConfig.Instance.MachineActuatorList.get(actuator).RefAngleAddressLocationInEERPOM));
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{add(request);}});
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

    public UUID SetParameters(ArrayList<KineticsRequest> parameters) {
        UUID ACK = machineComms.SendData(parameters);
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


    public void ResetCommsContext()
    {
        machineComms.ClearCurrentTransmissionContext();
    }

    public UUID TurnOffPower() {
        final KineticsRequest request = new KineticsRequestPowerOff();
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    public  UUID WriteToEEPROM(EEPROMDetails AddressDetails, Integer Data)
    {
        final KineticsRequest request = new KineticsRequestEEPROMWrite(AddressDetails, Data);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
            add(request);
        }});
        return ACK;
    }

    public  UUID ReadFromEEPROM(EEPROMDetails AddressDetails)
    {
        final KineticsRequest request = new KineticsRequestEEPROMRead(AddressDetails);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
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
    public  UUID ReadFromServoEEPROM(Actuator actuator, EEPROMDetails AddressDetails)
    {
        final KineticsRequest request = new KineticsRequestServoEEPROMRead(actuator, AddressDetails);
        UUID ACK = machineComms.SendData(new ArrayList<KineticsRequest>(){{
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

}
