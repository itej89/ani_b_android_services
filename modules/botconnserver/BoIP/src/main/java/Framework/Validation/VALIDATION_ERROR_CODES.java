package Framework.Validation;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Activation_Response_Codes;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Diagnostic_NAK_Codes;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Diagnostic_Power_Mode_Values;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.NAK_Codes;

public enum VALIDATION_ERROR_CODES {

      FAIL,
     EMPTY,
     INCORRECT_LENGTH,
     INVALID_DATA,
     NO_PAYLOAD_ITEMS_FOUND,
     NETWORK_DISCONN,
     EMPTY_SOCKET,
     REMOTE_SOCKET_DISCONN,
     REMOTE_SOCKET_REFUSED_CONN,
     NO_PREVIOUS_DIAGNOSTIC_DATA,
     NO_SOURCE_ADDRESS;
    }


