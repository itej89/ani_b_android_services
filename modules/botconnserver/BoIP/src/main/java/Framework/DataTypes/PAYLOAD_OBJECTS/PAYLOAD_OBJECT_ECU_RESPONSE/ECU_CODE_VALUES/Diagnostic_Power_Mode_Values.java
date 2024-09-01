package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class Diagnostic_Power_Mode_Values {

    public static Diagnostic_Power_Mode_Values Instance = new Diagnostic_Power_Mode_Values();

    public enum CODE
    {
         DIAG_POWER_MODE_NOT_READY,
         DIAG_POWER_MODE_READY,
         DIAG_POWER_MODE_NOT_SUPPORTED,
         DIAG_POWER_MODE_RESERVED_ISO13400
    }

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();

    public  Diagnostic_Power_Mode_Values() {
        CODE_TO_VALUE.put(CODE.DIAG_POWER_MODE_NOT_READY, (byte)(0x00));
        CODE_TO_VALUE.put(CODE.DIAG_POWER_MODE_READY, (byte)(0x01));
        CODE_TO_VALUE.put(CODE.DIAG_POWER_MODE_NOT_SUPPORTED, (byte)(0x02));
        CODE_TO_VALUE.put(CODE.DIAG_POWER_MODE_RESERVED_ISO13400, (byte)(0x03));
        CODE_TO_VALUE.put(CODE.DIAG_POWER_MODE_RESERVED_ISO13400, (byte)(0xFF));
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
