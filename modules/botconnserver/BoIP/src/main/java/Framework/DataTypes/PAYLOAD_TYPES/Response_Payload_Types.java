package Framework.DataTypes.PAYLOAD_TYPES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Response_Payload_Types {
    public static Response_Payload_Types Instance = new Response_Payload_Types();

    public enum CODE
    {
         PLD_DOIP_HEADER_NAK,
         PLD_VEH_IDEN_RES,
         PLD_ROUTING_ACTIVATION_RES,
         PLD_ALIVE_CHECK_REQ,
         PLD_DOIP_ENTITY_STATUS_RES,
         PLD_DIAG_POWER_MODE_RES,
         PLD_RESERVED_ISO13400,
         PLD_DIAG_MESSAGE,
         PLD_DIAG_MESSAGE_POSITIVE_ACK,
         PLD_DIAG_MESSAGE_NEGATIVE_ACK,
         PLD_MANUFACTURER_SPECIFIC_ACK,
         DOIPTester_UNKNOWN_CODE
    }

    public Map<CODE,Short> CODE_TO_VALUE = new HashMap<CODE,Short>();

    public Response_Payload_Types()
    {
        CODE_TO_VALUE.put(CODE.PLD_DOIP_HEADER_NAK, (short) 0x0000);
        CODE_TO_VALUE.put(CODE.PLD_VEH_IDEN_RES, (short) 0x0004);
        CODE_TO_VALUE.put(CODE.PLD_ROUTING_ACTIVATION_RES, (short) 0x0006);
        CODE_TO_VALUE.put(CODE.PLD_ALIVE_CHECK_REQ, (short) 0x0007);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0x0009);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0x4000);
        CODE_TO_VALUE.put(CODE.PLD_DOIP_ENTITY_STATUS_RES, (short) 0x4002);
        CODE_TO_VALUE.put(CODE.PLD_DIAG_POWER_MODE_RES, (short) 0x4004);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0x4005);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0x8000);
        CODE_TO_VALUE.put(CODE.PLD_DIAG_MESSAGE, (short) 0x8001);
        CODE_TO_VALUE.put(CODE.PLD_DIAG_MESSAGE_POSITIVE_ACK, (short) 0x8002);
        CODE_TO_VALUE.put(CODE.PLD_DIAG_MESSAGE_NEGATIVE_ACK, (short) 0x8003);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0x8004);
        CODE_TO_VALUE.put(CODE.PLD_RESERVED_ISO13400, (short) 0xEFFF);
        CODE_TO_VALUE.put(CODE.PLD_MANUFACTURER_SPECIFIC_ACK, (short) 0xF000);
        CODE_TO_VALUE.put(CODE.PLD_MANUFACTURER_SPECIFIC_ACK, (short) 0xFFFF);
    }

    public byte[] Encode(CODE Payload_Type_Value)
    {
        byte[] Values = new byte[2];
        if(CODE_TO_VALUE.keySet().contains(Payload_Type_Value))
        {
            Values[0] = (byte)((CODE_TO_VALUE.get(Payload_Type_Value) & 0xFF00) >> 8);
            Values[1] = (byte)(CODE_TO_VALUE.get(Payload_Type_Value) & 0xFF);
        }
        return Values;
    }
}
