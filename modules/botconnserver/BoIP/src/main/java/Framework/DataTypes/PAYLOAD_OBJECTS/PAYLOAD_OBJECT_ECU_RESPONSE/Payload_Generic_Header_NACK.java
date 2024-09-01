package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Generic_Header_NACK extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.HEADER_NACK, new Payload_Item_Type( 0, 1));
    }

    public Payload_Generic_Header_NACK(byte[] DOIPPayload, long payload_length)
    {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public void SetNAK(Byte NACK)
    {
        byte[] _NACK = new byte[1];
        _NACK[0] = (NACK);
        Payload.get(TAG.HEADER_NACK).RawData = _NACK;
    }
}
