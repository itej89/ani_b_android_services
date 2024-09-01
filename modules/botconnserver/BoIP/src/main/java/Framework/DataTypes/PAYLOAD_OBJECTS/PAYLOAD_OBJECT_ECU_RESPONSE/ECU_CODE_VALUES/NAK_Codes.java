package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class NAK_Codes {

    public static NAK_Codes Instance = new NAK_Codes();

    public enum CODE
    {
         NAK_INCORRECT_PATTERN,
         NAK_UNKNOWN_PAYLOAD,
         NAK_MESSAGE_TOO_LARGE,
         NAK_OUT_OF_MEMORY,
         NAK_INVALID_PAYLOAD_LENGTH,
         NAK_RESERVED_ISO13400
    }

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();

    public NAK_Codes()
    {
        CODE_TO_VALUE.put(CODE.NAK_INCORRECT_PATTERN, (byte)0x00);
        CODE_TO_VALUE.put(CODE.NAK_UNKNOWN_PAYLOAD, (byte)0x01);
        CODE_TO_VALUE.put(CODE.NAK_MESSAGE_TOO_LARGE, (byte)0x02);
        CODE_TO_VALUE.put(CODE.NAK_OUT_OF_MEMORY, (byte)0x03);
        CODE_TO_VALUE.put(CODE.NAK_INVALID_PAYLOAD_LENGTH, (byte)0x04);
        CODE_TO_VALUE.put(CODE.NAK_RESERVED_ISO13400, (byte)0x05);
        CODE_TO_VALUE.put(CODE.NAK_RESERVED_ISO13400, (byte)0xFF);
    }

    public Byte ENCODE(CODE NAK_Code)
    {
        if(CODE_TO_VALUE.keySet().contains(NAK_Code))
        {
            return CODE_TO_VALUE.get(NAK_Code);
        }
        else
        {
            return (byte)0xFF;
        }
    }
}
