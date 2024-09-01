package Framework.Validation;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Activation_Response_Codes;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Diagnostic_NAK_Codes;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Diagnostic_Power_Mode_Values;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.NAK_Codes;

public class DIAGNOSTIC_STATUS
{
    public enum CODE
    {
         COMPLETE,
         INTERNAL_ERROR,
         UDP_TIMEOUT,
         TCP_TIMEOUT,
         SUCCESS,
         DIAG_ACK_TIMEOUT,
         ACTIVATIOMN_CONFIRMATION_TIMEOUT,
         INVALID_HEADER,
         RESERVED_ISO13400,

        //Routing activation resposnse codes
         RA_RES_DENIED_UNKNOWN_SOURCE_ADDRESS,
         RA_RES_DENIED_ALL_SUPPORTED_SOCKETS_REGISTERED_AND_ACTIVE,
         RA_RES_DENIED_SOURCE_ADDRESS_MISMATCH,
         RA_RES_DENIED_SOURCE_ADDRESS_ACTIVE_OTHER_PORT,
         RA_RES_DENIED_MISSING_AUTHENTICATION,
         RA_RES_DENIED_REJECTED_CONFIRMATION,
         RA_RES_DENIED_UNSUPPORTED_ACTIVATION_TYPE,
         RA_RES_VEHICLE_MANUFACTURER_SPECIFIC,

        //generic NAK (header+diagnostic)
         MESSAGE_TOO_LARGE,
         OUT_OF_MEMORY,

        //generic header NACK codes
         NAK_INCORRECT_PATTERN,
         NAK_UNKNOWN_PAYLOAD,
         NAK_INVALID_PAYLOAD_LENGTH,

        //diagnostic message negative ackowledgement codes
         DIAG_NAK_UNKOWN_NETWORK,
         DIAG_NAK_INVALID_SOURCE_ADDRESS,
         DIAG_NAK_TARGET_UNREACHABLE,
         DIAG_NAK_TRANSPORT_PROTOCOL_ERROR,
         DIAG_NAK_UNKOWN_TARGET,

        //diagnostic power mode information codes
         DIAG_POWER_MODE_NOT_READY,
         DIAG_POWER_MODE_NOT_SUPPORTED,

        //uds status codes
         DIAG_RESPONSE_TIMEOUT,
         DIAG_RECONN_TIMEOUT,

         NO_RESPONSE,

         DIAG_PROGRESS;


    }

    public  DIAGNOSTIC_STATUS()
    {

            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_ALL_SUPPORTED_SOCKETS_REGISTERED_AND_ACTIVE, CODE.RA_RES_DENIED_ALL_SUPPORTED_SOCKETS_REGISTERED_AND_ACTIVE.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_MISSING_AUTHENTICATION,CODE.RA_RES_DENIED_MISSING_AUTHENTICATION.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_REJECTED_CONFIRMATION,CODE.RA_RES_DENIED_REJECTED_CONFIRMATION.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_SOURCE_ADDRESS_ACTIVE_OTHER_PORT,CODE.RA_RES_DENIED_SOURCE_ADDRESS_ACTIVE_OTHER_PORT.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_SOURCE_ADDRESS_MISMATCH,CODE.RA_RES_DENIED_SOURCE_ADDRESS_MISMATCH.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_UNKNOWN_SOURCE_ADDRESS,CODE.RA_RES_DENIED_UNKNOWN_SOURCE_ADDRESS.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_DENIED_UNSUPPORTED_ACTIVATION_TYPE,CODE.RA_RES_DENIED_UNSUPPORTED_ACTIVATION_TYPE.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_RESERVED_ISO13400,CODE.RESERVED_ISO13400.ordinal());
            ResponseCode_ErrorCode.put(Activation_Response_Codes.CODE.RA_RES_VEHICLE_MANUFACTURER_SPECIFIC,CODE.RA_RES_VEHICLE_MANUFACTURER_SPECIFIC.ordinal());

        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_INCORRECT_PATTERN,CODE.NAK_INCORRECT_PATTERN.ordinal());
        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_INVALID_PAYLOAD_LENGTH,CODE.NAK_INVALID_PAYLOAD_LENGTH.ordinal());
        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_MESSAGE_TOO_LARGE,CODE.MESSAGE_TOO_LARGE.ordinal());
        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_OUT_OF_MEMORY,CODE.OUT_OF_MEMORY.ordinal());
        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_RESERVED_ISO13400,CODE.RESERVED_ISO13400.ordinal());
        HeaderNACKCode_ErrorCode.put(NAK_Codes.CODE.NAK_UNKNOWN_PAYLOAD,CODE.NAK_UNKNOWN_PAYLOAD.ordinal());

        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_RESERVED_ISO13400,CODE.RESERVED_ISO13400.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_INVALID_SOURCE_ADDRESS,CODE.DIAG_NAK_INVALID_SOURCE_ADDRESS.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_UNKOWN_TARGET,CODE.DIAG_NAK_UNKOWN_TARGET.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_MESSAGE_TOO_LARGE,CODE.MESSAGE_TOO_LARGE.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_OUT_OF_MEMORY,CODE.OUT_OF_MEMORY.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_TARGET_UNREACHABLE,CODE.DIAG_NAK_TARGET_UNREACHABLE.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DIAG_NAK_UNKOWN_NETWORK,CODE.DIAG_NAK_UNKOWN_NETWORK.ordinal());
        DiagNACKCode_ErrorCode.put(Diagnostic_NAK_Codes.CODE.DAIG_NAK_TRANSPORT_PROTOCOL_ERROR,CODE.DIAG_NAK_TRANSPORT_PROTOCOL_ERROR.ordinal());

        DiagPowerModeCode_ErrorCode.put(Diagnostic_Power_Mode_Values.CODE.DIAG_POWER_MODE_NOT_READY,CODE.DIAG_POWER_MODE_NOT_READY.ordinal());
        DiagPowerModeCode_ErrorCode.put(Diagnostic_Power_Mode_Values.CODE.DIAG_POWER_MODE_NOT_SUPPORTED,CODE.DIAG_POWER_MODE_NOT_SUPPORTED.ordinal());
        DiagPowerModeCode_ErrorCode.put(Diagnostic_Power_Mode_Values.CODE.DIAG_POWER_MODE_RESERVED_ISO13400,CODE.RESERVED_ISO13400.ordinal());


    }

    public Map<Activation_Response_Codes.CODE,Integer> ResponseCode_ErrorCode = new HashMap<Activation_Response_Codes.CODE,Integer>();
    public Map<NAK_Codes.CODE,Integer> HeaderNACKCode_ErrorCode = new HashMap<NAK_Codes.CODE,Integer>();
    public Map<Diagnostic_NAK_Codes.CODE,Integer> DiagNACKCode_ErrorCode = new HashMap<Diagnostic_NAK_Codes.CODE,Integer>();
    public Map<Diagnostic_Power_Mode_Values.CODE,Integer> DiagPowerModeCode_ErrorCode= new HashMap<Diagnostic_Power_Mode_Values.CODE,Integer>();


}
