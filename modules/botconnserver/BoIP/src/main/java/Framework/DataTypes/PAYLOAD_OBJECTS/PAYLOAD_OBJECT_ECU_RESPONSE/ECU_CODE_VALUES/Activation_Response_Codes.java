package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class Activation_Response_Codes {

    public static Activation_Response_Codes Instance = new Activation_Response_Codes();

    public enum CODE
    {
         RA_RES_DENIED_UNKNOWN_SOURCE_ADDRESS,
         RA_RES_DENIED_ALL_SUPPORTED_SOCKETS_REGISTERED_AND_ACTIVE,
         RA_RES_DENIED_SOURCE_ADDRESS_MISMATCH,
         RA_RES_DENIED_SOURCE_ADDRESS_ACTIVE_OTHER_PORT,
         RA_RES_DENIED_MISSING_AUTHENTICATION,
         RA_RES_DENIED_REJECTED_CONFIRMATION,
         RA_RES_DENIED_UNSUPPORTED_ACTIVATION_TYPE,
         RA_RES_SUCCESS,
         RA_RES_CONFIRMATION_REQUIRED,
         RA_RES_RESERVED_ISO13400,
         RA_RES_VEHICLE_MANUFACTURER_SPECIFIC
    }

    Map<CODE,Byte> CODE_TO_VALUE = new HashMap<CODE,Byte>();

    public Activation_Response_Codes()
    {
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_UNKNOWN_SOURCE_ADDRESS, (byte)0x00);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_ALL_SUPPORTED_SOCKETS_REGISTERED_AND_ACTIVE, (byte)0x01);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_SOURCE_ADDRESS_MISMATCH, (byte)0x02);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_SOURCE_ADDRESS_ACTIVE_OTHER_PORT, (byte)0x03);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_MISSING_AUTHENTICATION, (byte)0x04);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_REJECTED_CONFIRMATION, (byte)0x05);
        CODE_TO_VALUE.put(CODE.RA_RES_DENIED_UNSUPPORTED_ACTIVATION_TYPE, (byte)0x06);
        CODE_TO_VALUE.put(CODE.RA_RES_RESERVED_ISO13400, (byte)0x07);
        CODE_TO_VALUE.put(CODE.RA_RES_SUCCESS, (byte)0x10);
        CODE_TO_VALUE.put(CODE.RA_RES_CONFIRMATION_REQUIRED, (byte)0x11);
        CODE_TO_VALUE.put(CODE.RA_RES_RESERVED_ISO13400, (byte)0x12);
        CODE_TO_VALUE.put(CODE.RA_RES_VEHICLE_MANUFACTURER_SPECIFIC, (byte)0xE0);
        CODE_TO_VALUE.put(CODE.RA_RES_RESERVED_ISO13400, (byte)0xFF);
    }

    public Byte ENCODE(CODE Activation_Response_Code)
    {
        if(CODE_TO_VALUE.keySet().contains(Activation_Response_Code))
        {
            return CODE_TO_VALUE.get(Activation_Response_Code);
        }
        else
        {
            return (byte)0xFF;
        }
    }
}
