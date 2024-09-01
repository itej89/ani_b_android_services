package FrameworkInterface;

import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import android.os.ParcelUuid;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
//import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.KineticsBindStatusConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsParameterUpdatesConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface KineticsAccess {

        public Boolean IsServiceReady();

        public Boolean initServiceConnection();

        public void releaseServiceConnection();

        public Boolean IsConnectedToMachine();

        //this will set the listener for kinetics service connection status
        public void setServiceStatusConvey(KineticsServiceStatusConvey kineticsStatusConvey);


        //this will set the listener for any remote events from the machine
        public void SetMachineRemoteRequestListener(KineticsRemoteRequestConvey delegate);

        //this will set the listener of the machine responses for the previously sent request
        public void SetKineticsResposeListener(KineticsResponseConvey delegate);


        //this will set the listener for any kinetics requests sent and trigger responses
        //meant to be used for updating system state
        public void SetKineticsParameterUpdatesListener(KineticsParameterUpdatesConvey delegate);

        //this will set the listener for any kinetics Machine Bind state
        public void SetKineticsBindStateListener(KineticsBindStatusConvey delegate);

        //Request Kinetics Service to Bind To machine
        public void BindMachine();

        //Request Kinetics Service to UnBind From Machine
        public void UnBindMachine();

        public MachineConfig GetMachineConfig();

        //Handles Functionality to show explicit indication when machine has attention for the user input
        //In version 1 this will be turning on white led
        public ParcelUuid IndicateAttention();

        //Handles Functionality to show explicit indication when machine is not paying attention for the user input
        //In version 1 this will be turning off white led
        public ParcelUuid IndicateNoAttention();

        //request machines for the current angle of the given actuator
        public ParcelUuid ReadDegree(Actuator actuator);

        //request machine on the status of the proximity sensor
        public ParcelUuid ReadProximity();

        //request machine for status if, all actuators are completed moving or not
        public ParcelUuid ReadMotionState(Actuator actuatorType);

        //request machine for status if, the power is on to the actuator
        public ParcelUuid ReadActuatorPowerStatus(Actuator actuator);

        //request machine for status if, the signal is attached to the actuator
        public ParcelUuid ReadActuatorSignalStatus(Actuator actuator);

        //request machine for status if, the power is on to the lock
        public ParcelUuid ReadLockPowerStatus();

        //request machine for status if, the signal is attached to lock
        public ParcelUuid ReadLockSignalStatus();


        //switch on power to the actuator
        public ParcelUuid PowerOnMotor(Actuator actuator);

        //switch off power to the actuator
        public ParcelUuid PowerOffMotor(Actuator actuator);

        //Attach Signal to actuator
        public ParcelUuid AttachSignalToActuator(Actuator actuator);

        //Detach Signal to actuator
        public ParcelUuid DetachSignalToActuator(Actuator actuator);


        //switch on power to the lock motor
        public ParcelUuid PowerOnLockMotor();

        //switch off power to the lock motor
        public ParcelUuid PowerOffLockMotor();

        //Attach Signal to lock actuator
        public ParcelUuid AttachSignalToALockctuator();

        //Detach Signal to lock actuator
        public ParcelUuid DetachSignalToLockActuator();

        //This Will Reset MachineComms Context during app deactivate or activate
        public void ResetCommsContext();

        //requests machine to start motion with the previously set parameters
        public ParcelUuid StartMotion();

        //Stops current ongoing motion and requests machine to start motion with the previously set parameters immediately
        public ParcelUuid StartInstantMotion();

        //sets the parameters for the given actuator to be used in the next upload request
        public ParcelUuid SetParameters(ArrayList<KineticsRequest> parameters);

        //Sends comamnd to read actuator communication address
        public ParcelUuid ReadActuatorAddress(Actuator actuator);

        //Extracts Actuator address from machine response and saves in config for future use
        public Boolean SetActuatorAddress(Actuator actuator, ArrayList<KineticsResponse> response);

        //Sends comamnd to read actuator Delta Rese angle
        public ParcelUuid ReadDeltaResetAngle(Actuator actuator);

        //Extracts Actuator Delta Reset angle from machine response and saves in config for future use
        public Boolean SetDeltaResetAngle(Actuator actuator,ArrayList<KineticsResponse> response);

        //Sends comamnd to read actuator referance angle
        public ParcelUuid ReadReferanceAngle(Actuator actuator);

        //Extracts Actuator referance angle from machine response and saves in config for future use
        public Boolean SetReferanceAngle(Actuator actuator,ArrayList<KineticsResponse> response);

        //Create KinticAngleRequest from the KineticDegreeresponse for initial actuator initial settings
        public KineticsRequestAngle GetKineticRequestAngleFromDegreeResponse(Actuator actuator, ArrayList<KineticsResponse> response);

        //Returns Delta Angle by substracting referance angle for the actuator
        public Integer GetDeltaAngleFromFullAngle(Integer FullAngle, Actuator actuator);

        //Returns Delta Angle by substracting referance angle for the actuator
        public Integer GetFullAngleFromDeltaAngle(Integer DeltaAngle, Actuator actuator);

        //Reads any predefined machine command that is present in db using name
        public ArrayList<KineticsRequest> GetPredefinedKineticsRequestAngleByName(String Name);

        //Check if any device detected on proximty sensor from the read proximity repsonse
        public Boolean CheckProximity(ArrayList<KineticsResponse> response);

        //Check if power is connected to actuator from the read actuator power status repsonse
        public Boolean CheckActuatorPowerStatus(ArrayList<KineticsResponse> response);

        //Check if Signal is connected to actuator from the read actuator power status repsonse
        public Boolean CheckActuatorSignalStatus(ArrayList<KineticsResponse> response);

        //Check if power is connected to lock from the read actuator power status repsonse
        public Boolean CheckLockPowerStatus(ArrayList<KineticsResponse> response);

        //Check if Signal is connected to lock from the read actuator power status repsonse
        public Boolean CheckLockSignalStatus(ArrayList<KineticsResponse> response);

        //requests machine to start power off sequence
        public ParcelUuid TurnOffPower();

        //writes given bytes to mainboard eeprom Address location
        public  ParcelUuid WriteToEEPROM(EEPROMDetails AddressDetails, Integer Data);

        //Check if EEPROM write is successful
        public  Boolean CheckWriteToEEPROM(KineticsResponse Response);

        //read given number of bytes from from mainboard eeprom Address location
        public  ParcelUuid ReadFromEEPROM(EEPROMDetails AddressDetails);

        //Returns the payload bytes from EEPROMReadResonse Command
        public ArrayList<Integer> ExtractBytesFromEEPROMReadResponse(KineticsResponse EEPROMReadResponse);

        //read given number of bytes from from Servo board eeprom Address location
        public  ParcelUuid ReadFromServoEEPROM(Actuator actuator, EEPROMDetails AddressDetails);


        //Returns the payload bytes from ServoEEPROMReadResonse Command
        ArrayList<Integer> ExtractBytesFromServoEEPROMReadResponse(KineticsResponse EEPROMReadResponse);

        //Deletes calibration Data From DataBase for the given actuator
        public  void DeleteCalibrationDataForActuator(Actuator actuator);

        //Saves Servo Degree and ADC Value
        public  void SaveActuatorCalibrationData(Actuator actuator, Integer Degree, Integer ADC );



        //writes a byte from from ISL94203 EEPROM Address location
        public  ParcelUuid WriteToISLEEPROM(Integer Address, Integer numberOfBytes, Integer Value);

        //read a byte from from ISL94203 RAM Address location
        public  ParcelUuid ReadFromISLEEPROM(Integer Address, Integer numberOfBytes);

        //writes a byte from from ISL94203 RAM Address location
        public  ParcelUuid WriteToISLRAM(Integer Address, Integer numberOfBytes, Integer Value);

        //read a byte from from ISL94203 eeprom Address location
        public  ParcelUuid ReadFromISLRAM(Integer Address, Integer numberOfBytes);

        //Returns the payload bytes from ISLEEPROMReadResonse Command
        public  ArrayList<Integer> ExtractBytesFromISLEEPROMReadResponse(KineticsResponse ISLEEPROMReadResponse);

        //Returns the payload bytes from ISLRAMReadResonse Command
        public  ArrayList<Integer> ExtractBytesFromISLRAMReadResponse(KineticsResponse ISLRAMReadResponse);

        //Check if ISLEEPROM write is successful
        public  Boolean CheckWriteToISLEEPROM(KineticsResponse Response);

        //Check if ISLRAM write is successful
        public  Boolean CheckWriteToISLRAM(KineticsResponse Response);



        //IKineticsParameterUpdatesConveyAIDL
        public void RequestSent(KineticsRequest request);

        public void ParameterTriggerSuccuss();


        //IKineticsResponseConveyAIDL
        public void KineticsResponseDataTimeout(UUID uuid, ArrayList<KineticsResponse> PartialResponse);

        public void RecievedResponseData(ArrayList<KineticsResponse> responeData,  UUID _Acknowledgement);


        //IKineticsRemoteRequestConveyAIDL
        public void RecievedRemoteCommand(MachineRequests event);

        public  void ConnectToKineticServices(Map<String, String> KineticsConveyList);
        public  void KineticsServiceConnected();
        public  void KineticsServiceDisconnected();


        public void SetBindingStatus(String state);
}
