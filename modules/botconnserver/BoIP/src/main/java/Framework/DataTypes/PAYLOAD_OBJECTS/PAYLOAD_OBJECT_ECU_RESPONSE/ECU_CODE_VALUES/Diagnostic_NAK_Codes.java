package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class Diagnostic_NAK_Codes {

    public static Diagnostic_NAK_Codes Instance = new Diagnostic_NAK_Codes();

    public enum CODE
    {
         DIAG_NAK_INVALID_SOURCE_ADDRESS,
         DIAG_NAK_UNKOWN_TARGET,
         DIAG_NAK_MESSAGE_TOO_LARGE,
         DIAG_NAK_OUT_OF_MEMORY,
         DIAG_NAK_TARGET_UNREACHABLE,
         DIAG_NAK_RESERVED_ISO13400,
         DIAG_NAK_UNKOWN_NETWORK,
         DAIG_NAK_TRANSPORT_PROTOCOL_ERROR
    }

    Map<CODE,Byte> CODE_TO_VALUE =  new HashMap<CODE,Byte>();

    public Diagnostic_NAK_Codes()
    {
        CODE_TO_VALUE.put(CODE.DIAG_NAK_RESERVED_ISO13400, (byte)0x00);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_INVALID_SOURCE_ADDRESS, (byte)0x02);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_UNKOWN_TARGET, (byte)0x03);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_MESSAGE_TOO_LARGE, (byte)0x04);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_OUT_OF_MEMORY, (byte)0x05);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_TARGET_UNREACHABLE, (byte)0x06);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_UNKOWN_NETWORK, (byte)0x07);
        CODE_TO_VALUE.put(CODE.DAIG_NAK_TRANSPORT_PROTOCOL_ERROR, (byte)0x08);
        CODE_TO_VALUE.put(CODE.DIAG_NAK_RESERVED_ISO13400, (byte)0x09);
    }

    public Byte ENCODE(CODE NAK_Code)
    {
        if(CODE_TO_VALUE.keySet().contains((NAK_Code)))
        {
            return CODE_TO_VALUE.get(NAK_Code);
        }
        else
        {
            return (byte)0xFF;
        }
    }
}
