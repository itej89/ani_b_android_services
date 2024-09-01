package FrameworkInterface;

import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.Map;

import android.os.ParcelUuid;

import Framework.DataTypes.Delegates.ServiceConnectionBindRequestConvey;
import Framework.DataTypes.Delegates.ServiceConnectionEstablishedConvey;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsParameterUpdatesConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import FrameworkInterface.PublicTypes.Machine;

public interface KineticsAccess {


        //Initializes the Communication module
        public void SetCommsDelegate(KineticsCommsConvey delegate);

        //Returns the SUB Serial convertor
        public UsbDevice GetUSBSerialDevice();



        public void InitializeComms();

        //Start scanning of machines
        public void StartScan();

        //Stops scanning for machines
        public void StopScan();

        //Connect to the given machine
        public void ConnectToMachine(Machine machine);

        //Check If Comms is in connected state
        public Boolean IsConnectedToMachine();

        //Disconnects from any connected machine
        public void DisconnectMachine();


        public void ConnectToKineticServices(Map<String, String> KineticsConveyList);

        //this will set the listener for any remote events from the machine
        public void SetMachineRemoteRequestListener(KineticsRemoteRequestConvey delegate);

        //this will set the listener of the machine responses for the previously sent request
        public void SetKineticsResposeListener(KineticsResponseConvey delegate);

        //this will set the listener for any kinetics requests sent and trigger responses
        //meant to be used for updating system state
        public void SetKineticsParameterUpdatesListener(KineticsParameterUpdatesConvey delegate);

        //this will set the listener for service connection completed status
        //this will get called after all delgate connections are established
        public void SetServiceConnectionEstablishedConveyListener(ServiceConnectionEstablishedConvey delegate);

        //this will set the listener for machine bind - unbind request
        public void SetServiceConnectionBindRequestConveyListener(ServiceConnectionBindRequestConvey delegate);

        public void ConveyBindStatus(String state);


        MachineConfig GetMachineConfig();


        //Binder Calls
        public void BindMachine(String connectionID);

        public void UnBindMachine(String connectionID);
        //End of Binder Calls


        //Handles Functionality to show explicit indication when machine has attention for the user input
        //In version 1 this will be turning on white led
        public ParcelUuid IndicateAttention(String connectionID);

        //Handles Functionality to show explicit indication when machine is not paying attention for the user input
        //In version 1 this will be turning off white led
        public ParcelUuid IndicateNoAttention(String connectionID);



        //request machines for the current angle of the given actuator
        public ParcelUuid ReadDegree(String connectionID, Actuator actuator);

        //request machine on the status of the proximity sensor
        public ParcelUuid ReadProximity(String connectionID);

        //request machine for status if, all actuators are completed moving or not
        public ParcelUuid ReadMotionState(String connectionID, Actuator actuatorType);

        //request machine for status if, the power is on to the actuator
        public ParcelUuid ReadActuatorPowerStatus(String connectionID, Actuator actuator);

        //request machine for status if, the signal is attached to the actuator
        public ParcelUuid ReadActuatorSignalStatus(String connectionID, Actuator actuator);

        //request machine for status if, the power is on to the lock
        public ParcelUuid ReadLockPowerStatus(String connectionID);

        //request machine for status if, the signal is attached to lock
        public ParcelUuid ReadLockSignalStatus(String connectionID);


        //switch on power to the actuator
        public ParcelUuid PowerOnMotor(String connectionID, Actuator actuator);

        //switch off power to the actuator
        public ParcelUuid PowerOffMotor(String connectionID, Actuator actuator);

        //Attach Signal to actuator
        public ParcelUuid AttachSignalToActuator(String connectionID, Actuator actuator);

        //Detach Signal to actuator
        public ParcelUuid DetachSignalToActuator(String connectionID, Actuator actuator);


        //switch on power to the lock motor
        public ParcelUuid PowerOnLockMotor(String connectionID);

        //switch off power to the lock motor
        public ParcelUuid PowerOffLockMotor(String connectionID);

        //Attach Signal to lock actuator
        public ParcelUuid AttachSignalToALockctuator(String connectionID);

        //Detach Signal to lock actuator
        public ParcelUuid DetachSignalToLockActuator(String connectionID);

        //This Will Reset MachineComms Context during app deactivate or activate
        public void ResetCommsContext();

        //requests machine to start motion with the previously set parameters
        public ParcelUuid StartMotion(String connectionID);

        //Stops current ongoing motion and requests machine to start motion with the previously set parameters immediately
        public ParcelUuid StartInstantMotion(String connectionID);

        //sets the parameters for the given actuator to be used in the next upload request
        public ParcelUuid SetParameters(String connectionID, ArrayList<KineticsRequest> parameters);

        //Sends comamnd to read actuator communication address
        public ParcelUuid ReadActuatorAddress(String connectionID, Actuator actuator);

        //Extracts Actuator address from machine response and saves in config for future use
        public Boolean SetActuatorAddress(Actuator actuator, ArrayList<KineticsResponse> response);

        //Sends comamnd to read actuator Delta Rese angle
        public ParcelUuid ReadDeltaResetAngle(String connectionID, Actuator actuator);

        //Extracts Actuator Delta Reset angle from machine response and saves in config for future use
        public Boolean SetDeltaResetAngle(Actuator actuator,ArrayList<KineticsResponse> response);

        //Sends comamnd to read actuator referance angle
        public ParcelUuid ReadReferanceAngle(String connectionID, Actuator actuator);

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
        public ParcelUuid TurnOffPower(String connectionID);

        //writes given bytes to mainboard eeprom Address location
        public  ParcelUuid WriteToEEPROM(String connectionID, EEPROMDetails AddressDetails, Integer Data);

        //Check if EEPROM write is successful
        public  Boolean CheckWriteToEEPROM(KineticsResponse Response);

        //read given number of bytes from from mainboard eeprom Address location
        public  ParcelUuid ReadFromEEPROM(String connectionID, EEPROMDetails AddressDetails);

        //Returns the payload bytes from EEPROMReadResonse Command
        public ArrayList<Integer> ExtractBytesFromEEPROMReadResponse(KineticsResponse EEPROMReadResponse);

        //read given number of bytes from from Servo board eeprom Address location
        public  ParcelUuid ReadFromServoEEPROM(String connectionID, Actuator actuator, EEPROMDetails AddressDetails);


        //Returns the payload bytes from ServoEEPROMReadResonse Command
        ArrayList<Integer> ExtractBytesFromServoEEPROMReadResponse(KineticsResponse EEPROMReadResponse);

        //Deletes calibration Data From DataBase for the given actuator
        public  void DeleteCalibrationDataForActuator(Actuator actuator);

        //Saves Servo Degree and ADC Value
        public  void SaveActuatorCalibrationData(Actuator actuator, Integer Degree, Integer ADC );



        //writes a byte from from ISL94203 EEPROM Address location
        public  ParcelUuid WriteToISLEEPROM(String connectionID, Integer Address, Integer numberOfBytes, Integer Value);

        //read a byte from from ISL94203 RAM Address location
        public  ParcelUuid ReadFromISLEEPROM(String connectionID, Integer Address, Integer numberOfBytes);

        //writes a byte from from ISL94203 RAM Address location
        public  ParcelUuid WriteToISLRAM(String connectionID, Integer Address, Integer numberOfBytes, Integer Value);

        //read a byte from from ISL94203 eeprom Address location
        public  ParcelUuid ReadFromISLRAM(String connectionID, Integer Address, Integer numberOfBytes);

        //Returns the payload bytes from ISLEEPROMReadResonse Command
        public  ArrayList<Integer> ExtractBytesFromISLEEPROMReadResponse(KineticsResponse ISLEEPROMReadResponse);

        //Returns the payload bytes from ISLRAMReadResonse Command
        public  ArrayList<Integer> ExtractBytesFromISLRAMReadResponse(KineticsResponse ISLRAMReadResponse);

        //Check if ISLEEPROM write is successful
        public  Boolean CheckWriteToISLEEPROM(KineticsResponse Response);

        //Check if ISLRAM write is successful
        public  Boolean CheckWriteToISLRAM(KineticsResponse Response);
}
