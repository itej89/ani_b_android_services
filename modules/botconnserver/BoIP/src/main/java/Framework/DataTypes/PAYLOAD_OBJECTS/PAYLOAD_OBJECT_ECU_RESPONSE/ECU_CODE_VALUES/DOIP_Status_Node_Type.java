package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES;

import java.util.HashMap;
import java.util.Map;

public class DOIP_Status_Node_Type {

    public static DOIP_Status_Node_Type Instance = new DOIP_Status_Node_Type();

    public enum CODE
    {
         DOIP_STATUS_DOIP_GATEWAY,
         DOIP_STATUS_DOIP_NODE,
         DOIP_STATUS_RESERVED_ISO13400
    }

    Map<CODE,Byte> CODE_TO_VALUE = new HashMap<CODE,Byte>();

    public DOIP_Status_Node_Type()
    {
        CODE_TO_VALUE.put(CODE.DOIP_STATUS_DOIP_GATEWAY, (byte)0x00);
        CODE_TO_VALUE.put(CODE.DOIP_STATUS_DOIP_GATEWAY, (byte)0x01);
        CODE_TO_VALUE.put(CODE.DOIP_STATUS_DOIP_GATEWAY, (byte)0x02);
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
