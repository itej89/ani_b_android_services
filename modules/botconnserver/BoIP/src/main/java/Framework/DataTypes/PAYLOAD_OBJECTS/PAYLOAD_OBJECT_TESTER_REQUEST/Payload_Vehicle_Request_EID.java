package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Vehicle_Request_EID extends PayloadObject
{

    public  void initialize_PayloadItems()
    {
        Payload.put(PayloadObject.TAG.EID, new Payload_Item_Type(0, 17));
    }

    public  Payload_Vehicle_Request_EID() {
        super();
        initialize_PayloadItems();
    }

    public  Payload_Vehicle_Request_EID(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public byte[] GetEID()
    {
        return   Payload.get(PayloadObject.TAG.EID).RawData;
    }
}
