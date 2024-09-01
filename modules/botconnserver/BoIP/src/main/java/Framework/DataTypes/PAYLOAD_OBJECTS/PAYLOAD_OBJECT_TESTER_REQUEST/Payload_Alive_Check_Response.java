package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Alive_Check_Response extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.SOURCE_ADDRESS, new Payload_Item_Type( 0, 2));
    }
    public  Payload_Alive_Check_Response()
    {
        super();
        initialize_PayloadItems();
    }

    public  Payload_Alive_Check_Response(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public void SetSourceAddress(byte[] SA)
    {
        Payload.get(TAG.SOURCE_ADDRESS).RawData = SA;
    }
}
