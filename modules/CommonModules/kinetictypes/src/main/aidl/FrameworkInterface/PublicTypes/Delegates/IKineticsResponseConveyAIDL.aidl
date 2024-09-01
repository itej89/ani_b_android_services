// IKineticsParameterUpdatesConvey.aidl
package FrameworkInterface.PublicTypes.Delegates;

import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;

import FrameworkInterface.PublicTypes.EEPROMDetails;



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
// Declare any non-default types here with import statements

interface IKineticsResponseConveyAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
       void  CommsLost();

       void  MachiResponseTimeout(in List<KineticsResponse> partialResponse,in ParcelUuid _Acknowledgement);

       void  MachineResponseRecieved(in List<KineticsResponse> responeData,in ParcelUuid _Acknowledgement);

}
