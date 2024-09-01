package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class Diagnostic_ACK_Code {

    public static Diagnostic_ACK_Code Instance = new Diagnostic_ACK_Code();

    public enum CODE
    {
         DIAG_ACK_PASS,
        DIAG_ACK_RESERVED_ISO13400
    }

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();

    public Diagnostic_ACK_Code()
    {
        CODE_TO_VALUE.put(CODE.DIAG_ACK_PASS, (byte)0x00);
        CODE_TO_VALUE.put(CODE.DIAG_ACK_RESERVED_ISO13400, (byte)0x01);
        CODE_TO_VALUE.put(CODE.DIAG_ACK_RESERVED_ISO13400, (byte)0xFF);
    }

    public byte ENCODE(CODE ACK_Code)
    {
        if(CODE_TO_VALUE.keySet().contains(ACK_Code))
        {
            return CODE_TO_VALUE.get(ACK_Code);
        }
        else
        {
            return (byte)0xFF;
        }
    }
}
