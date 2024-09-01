// IKineticsAccessAIDL.aidl
package FrameworkInterface;

import FrameworkInterface.PublicTypes.EEPROMDetails;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttentionOn;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestCELLOne;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestCELLThree;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestCELLTwo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestConnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDamp;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDettachServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDisconnectPower;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestForActuator;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestFrequency;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestInstantTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLEEPROMWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestISLWrite;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLeftLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLockSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOff;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOffLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOnLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestProximityRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestRightLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoDegree;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEEPROMRead;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoMotionCheck;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoPowerStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoSignalStatus;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestVelocity;


import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseAttachLockServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseAttachServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseAttentionOff;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseAttentionOn;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseCELLOne;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseCELLThree;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseCELLTwo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseConnectPower;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseDettachLockServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseDettachServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseDisconnectPower;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEasingInOut;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseFrequency;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseInstantTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLEEPROMWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseISLWrite;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLeftServoAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseLockSignalStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponsePowerOff;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponsePowerOffLockServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponsePowerOnLockServo;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseProximityRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseRightServoAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoDegree;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoEasing;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoEEPROMRead;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoMotionCheck;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoPowerStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseServoSignalStatus;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseTiming;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponseVelocity;

import Framework.DataTypes.ActuatorCalibration;
import FrameworkInterface.PublicTypes.Config.MachineConfig;


// Declare any non-default types here with import statements
interface IKineticsAccessAIDL {

         byte IsServiceReady();

         void BindMachine(String connectionID);

         void UnBindMachine(String connectionID);

         MachineConfig GetMachineConfig();



        //Handles Functionality to show explicit indication when machine has attention for the user input
        //In version 1 this will be turning on white led
         ParcelUuid IndicateAttention(String connectionID);

        //Handles Functionality to show explicit indication when machine is not paying attention for the user input
        //In version 1 this will be turning off white led
         ParcelUuid IndicateNoAttention(String connectionID);


        //request machines for the current angle of the given actuator
         ParcelUuid ReadDegree(String connectionID,String actuator);

        //request machine on the status of the proximity sensor
         ParcelUuid ReadProximity(String connectionID);

        //request machine for status if, all actuators are completed moving or not
         ParcelUuid ReadMotionState(String connectionID,String actuatorType);

        //request machine for status if, the power is on to the actuator
         ParcelUuid ReadActuatorPowerStatus(String connectionID,String actuator);

        //request machine for status if, the signal is attached to the actuator
         ParcelUuid ReadActuatorSignalStatus(String connectionID,String actuator);

        //request machine for status if, the power is on to the lock
         ParcelUuid ReadLockPowerStatus(String connectionID);

        //request machine for status if, the signal is attached to lock
         ParcelUuid ReadLockSignalStatus(String connectionID);


        //switch on power to the actuator
         ParcelUuid PowerOnMotor(String connectionID,String actuator);

        //switch off power to the actuator
         ParcelUuid PowerOffMotor(String connectionID,String actuator);

        //Attach Signal to actuator
         ParcelUuid AttachSignalToActuator(String connectionID,String actuator);

        //Detach Signal to actuator
         ParcelUuid DetachSignalToActuator(String connectionID,String actuator);


        //switch on power to the lock motor
         ParcelUuid PowerOnLockMotor(String connectionID);

        //switch off power to the lock motor
         ParcelUuid PowerOffLockMotor(String connectionID);

        //Attach Signal to lock actuator
         ParcelUuid AttachSignalToALockctuator(String connectionID);

        //Detach Signal to lock actuator
         ParcelUuid DetachSignalToLockActuator(String connectionID);

        //This Will Reset MachineComms Context during app deactivate or activate
         void ResetCommsContext();

        //requests machine to start motion with the previously set parameters
         ParcelUuid StartMotion(String connectionID);

        //Stops current ongoing motion and requests machine to start motion with the previously set parameters immediately
         ParcelUuid StartInstantMotion(String connectionID);

        //sets the parameters for the given actuator to be used in the next upload request
         ParcelUuid SetParameters(String connectionID, in List<KineticsRequest> parameters);

//Sends comamnd to read actuator communication address
         ParcelUuid ReadActuatorAddress(String connectionID,String actuator);

        //Extracts Actuator address from machine response and saves in config for future use
         byte SetActuatorAddress(String actuator, in List<KineticsResponse> response);

        //Sends comamnd to read actuator Delta Rese angle
         ParcelUuid ReadDeltaResetAngle(String connectionID, String actuator);

        //Extracts Actuator Delta Reset angle from machine response and saves in config for future use
         byte SetDeltaResetAngle(String actuator,in List<KineticsResponse> response);

        //Sends comamnd to read actuator referance angle
         ParcelUuid ReadReferanceAngle(String connectionID, String actuator);

        //Extracts Actuator referance angle from machine response and saves in config for future use
         byte SetReferanceAngle(String actuator,in List<KineticsResponse> response);

        //Create KinticAngleRequest from the KineticDegreeresponse for initial actuator initial settings
         KineticsRequest GetKineticRequestAngleFromDegreeResponse(String actuator, in List<KineticsResponse> response);

        //Returns Delta Angle by substracting referance angle for the actuator
         int GetDeltaAngleFromFullAngle(int FullAngle, String actuator);

        //Returns Delta Angle by substracting referance angle for the actuator
         int GetFullAngleFromDeltaAngle(int DeltaAngle, String actuator);

        //Reads any predefined machine command that is present in db using name
         List<KineticsRequest> GetPredefinedKineticsRequestAngleByName(String Name);

        //Check if any device detected on proximty sensor from the read proximity repsonse
         byte CheckProximity(in List<KineticsResponse> response);

        //Check if power is connected to actuator from the read actuator power status repsonse
         byte CheckActuatorPowerStatus(in List<KineticsResponse> response);

        //Check if Signal is connected to actuator from the read actuator power status repsonse
         byte CheckActuatorSignalStatus(in List<KineticsResponse> response);

        //Check if power is connected to lock from the read actuator power status repsonse
         byte CheckLockPowerStatus(in List<KineticsResponse> response);

        //Check if Signal is connected to lock from the read actuator power status repsonse
         byte CheckLockSignalStatus(in List<KineticsResponse> response);

        //requests machine to start power off sequence
         ParcelUuid TurnOffPower(String connectionID);
//
//        //writes given bytes to mainboard eeprom Address location
          ParcelUuid WriteToEEPROM(String connectionID, in EEPROMDetails AddressDetails, int Data);
//
        //Check if EEPROM write is successful
          byte CheckWriteToEEPROM(in KineticsResponse Response);
//
//        //read given number of bytes from from mainboard eeprom Address location
          ParcelUuid ReadFromEEPROM(String connectionID, in EEPROMDetails AddressDetails);

        //Returns the payload bytes from EEPROMReadResonse Command
        int[] ExtractBytesFromEEPROMReadResponse(in KineticsResponse EEPROMReadResponse);
//
//        //read given number of bytes from from Servo board eeprom Address location
        ParcelUuid ReadFromServoEEPROM(String connectionID, String actuator, in EEPROMDetails AddressDetails);
//
//
        //Returns the payload bytes from ServoEEPROMReadResonse Command
        int[] ExtractBytesFromServoEEPROMReadResponse(in KineticsResponse EEPROMReadResponse);

        //Deletes calibration Data From DataBase for the given actuator
          void DeleteCalibrationDataForActuator(String actuator);

        //Saves Servo Degree and ADC Value
          void SaveActuatorCalibrationData(String actuator, int Degree, int ADC );


          void ConnectToKineticServices(in Map KineticsConveyList);


          //writes a byte from from ISL94203 EEPROM Address location
           ParcelUuid WriteToISLEEPROM(String connectionID, int Address, int numberOfBytes, int Value);

          //read a byte from from ISL94203 RAM Address location
           ParcelUuid ReadFromISLEEPROM(String connectionID, int Address, int numberOfBytes);

          //writes a byte from from ISL94203 RAM Address location
           ParcelUuid WriteToISLRAM(String connectionID, int Address, int numberOfBytes, int Value);

          //read a byte from from ISL94203 eeprom Address location
           ParcelUuid ReadFromISLRAM(String connectionID, int Address, int numberOfBytes);

          //Returns the payload bytes from ISLEEPROMReadResonse Command
           int[] ExtractBytesFromISLEEPROMReadResponse(in KineticsResponse ISLEEPROMReadResponse);

          //Returns the payload bytes from ISLRAMReadResonse Command
           int[] ExtractBytesFromISLRAMReadResponse(in KineticsResponse ISLRAMReadResponse);

          //Check if ISLEEPROM write is successful
           byte CheckWriteToISLEEPROM(in KineticsResponse Response);

          //Check if ISLRAM write is successful
           byte CheckWriteToISLRAM(in KineticsResponse Response);
}
