package Framework.DataTypes.PAYLOAD_TYPES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types.CODE.PLD_RESERVED_ISO13400;

public class Request_Payload_Types {
    public static Request_Payload_Types Instance = new Request_Payload_Types();

    public enum CODE
    {
         PLD_VEH_IDEN_REQ,
         PLD_VEH_IDEN_REQ_EID,
         PLD_VEH_IDEN_REQ_VIN,
         PLD_ROUTING_ACTIVATION_REQ,
         PLD_ALIVE_CHECK_RES,
         PLD_DOIP_ENTITY_STATUS_REQ,
         PLD_DIAG_POWER_MODE_REQ,
         PLD_RESERVED_ISO13400,
         PLD_DIAG_MESSAGE,
         PLD_MANUFACTURER_SPECIFIC_ACK,
         DOIPTester_UNKNOWN_CODE
    }

    public Map<Short,CODE> VALUE_TO_CODE = new HashMap<Short,CODE>();

    public Request_Payload_Types()
    {
        VALUE_TO_CODE.put((short)0x0001, CODE.PLD_VEH_IDEN_REQ);
        VALUE_TO_CODE.put((short)0x0002, CODE.PLD_VEH_IDEN_REQ_EID);
        VALUE_TO_CODE.put((short)0x0003, CODE.PLD_VEH_IDEN_REQ_VIN);
        VALUE_TO_CODE.put((short)0x0005, CODE.PLD_ROUTING_ACTIVATION_REQ);
        VALUE_TO_CODE.put((short)0x0008, CODE.PLD_ALIVE_CHECK_RES);
        VALUE_TO_CODE.put((short)0x0009, CODE.PLD_RESERVED_ISO13400);
        VALUE_TO_CODE.put((short)0x4001, CODE.PLD_DOIP_ENTITY_STATUS_REQ);
        VALUE_TO_CODE.put((short)0x4003, CODE.PLD_DIAG_POWER_MODE_REQ);
        VALUE_TO_CODE.put((short)0x4005, CODE.PLD_RESERVED_ISO13400);
        VALUE_TO_CODE.put((short)0x8001, CODE.PLD_DIAG_MESSAGE);
        VALUE_TO_CODE.put((short)0x8004, CODE.PLD_RESERVED_ISO13400);
    }

    public CODE Decode(byte[] Paylod_Value)
    {
        short key = (short)(((Paylod_Value[1] & 0x00FF) << 8) | Paylod_Value[0]);
        if(VALUE_TO_CODE.keySet().contains(key))
        {
            return VALUE_TO_CODE.get(key);
        }
        else{
            return PLD_RESERVED_ISO13400;
        }
    }
}
