package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Vehicle_Request_VIN extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.VIN, new Payload_Item_Type(0, 17));

    }

    public  Payload_Vehicle_Request_VIN() {
        super();
        initialize_PayloadItems();
    }

    public  Payload_Vehicle_Request_VIN(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public byte[] GetVIN()
    {
       return   Payload.get(TAG.VIN).RawData;
    }
}
