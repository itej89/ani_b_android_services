package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Diagnostic_Message_Ack extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.SOURCE_ADDRESS, new Payload_Item_Type( 0, 2));
        Payload.put(TAG.TARGET_ADDRESS, new Payload_Item_Type( 2, 2));
        Payload.put(TAG.ACK, new Payload_Item_Type( 4, 1));
        Payload.put(TAG.PREVIOUS_DIAGNOSTIC_DATA, new Payload_Item_Type( 5));
    }

    public  Payload_Diagnostic_Message_Ack(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload( DOIPPayload, payload_length);
    }

    public void SetSourceAddress(byte[] SA)
    {
         Payload.get(TAG.SOURCE_ADDRESS).RawData = SA;
    }

    public void SetTargetAddress(byte[] TA)
    {
        Payload.get(TAG.TARGET_ADDRESS).RawData = TA;
    }

    public void SetAcknowledgement(Byte ACK)
    {
        byte[] _ACK = new byte[1];
        _ACK[0] = (ACK);
        Payload.get(TAG.ACK).RawData = _ACK;
    }

    public void SetValidationData(byte[] TA)
    {
        Payload.get(TAG.PREVIOUS_DIAGNOSTIC_DATA).RawData = TA;
    }
}
