package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Vehicle_Announce_Further_Actions {
    public enum CODE
    {
         NO_FURTHER_ACTION_REQD,
         RESERVED_ISO13400,
         ROUTING_REQD_CENTRAL_SECURITY,
         OEM_SPECIFIC
    }

    Map<Byte, CODE> VALUE_TO_CODE = new HashMap<Byte, CODE>();

    Map<CODE, Byte> CODE_TO_VALUE = new HashMap<CODE, Byte>();

    public Vehicle_Announce_Further_Actions()
    {
        VALUE_TO_CODE.put((byte)0x00, CODE.NO_FURTHER_ACTION_REQD);
        VALUE_TO_CODE.put((byte)0x01, CODE.RESERVED_ISO13400);
        VALUE_TO_CODE.put((byte)0x0F, CODE.RESERVED_ISO13400);
        VALUE_TO_CODE.put((byte)0x10, CODE.ROUTING_REQD_CENTRAL_SECURITY);
        VALUE_TO_CODE.put((byte)0x11, CODE.OEM_SPECIFIC);
        VALUE_TO_CODE.put((byte)0xFF, CODE.OEM_SPECIFIC);


        CODE_TO_VALUE.put(CODE.NO_FURTHER_ACTION_REQD, (byte)0x00);
        CODE_TO_VALUE.put(CODE.RESERVED_ISO13400, (byte)0x01);
        CODE_TO_VALUE.put(CODE.ROUTING_REQD_CENTRAL_SECURITY, (byte)0x10);
        CODE_TO_VALUE.put(CODE.OEM_SPECIFIC, (byte)0x11);
    }

    public CODE DECODE(Byte FurtherActions_Value)
    {
        if(VALUE_TO_CODE.keySet().contains(FurtherActions_Value))
        {
            return VALUE_TO_CODE.get(FurtherActions_Value);
        }
        else
        {
            return CODE.OEM_SPECIFIC;
        }
    }

    public Byte ENCODE(CODE FurtherActions_Code)
    {
        if(CODE_TO_VALUE.keySet().contains(FurtherActions_Code))
        {
            return CODE_TO_VALUE.get(FurtherActions_Code);
        }
        else
        {
            return 0x00;
        }
    }
}
