package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Vehicle_Announcement extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.VIN,new Payload_Item_Type( 0, 17));
        Payload.put(TAG.LOGICAL_ADDRESS,new Payload_Item_Type( 17, 2));
        Payload.put(TAG.EID,new Payload_Item_Type( 19, 6));
        Payload.put(TAG.GID,new Payload_Item_Type( 25, 6));
        Payload.put(TAG.FURTHER_ACTION,new Payload_Item_Type( 31, 1));
        Payload.put(TAG.VIN_GID,new Payload_Item_Type( 32));
    }


    public Payload_Vehicle_Announcement()
    {
        super();
        initialize_PayloadItems();
    }

    public Payload_Vehicle_Announcement(byte[] DOIPPayload, long payload_length)
    {
        super();
        initialize_PayloadItems();
        Decode_Payload( DOIPPayload, payload_length);
    }

    public void SetVIN(byte[] VIN)
    {
         Payload.get(TAG.VIN).RawData = VIN;
    }

    public void SetlogicalAddress(byte[] LA)
    {
        Payload.get(TAG.LOGICAL_ADDRESS).RawData = LA;
    }

    public void SetEID(byte[] EID)
    {
         Payload.get(TAG.EID).RawData = EID;
    }

    public void SetGID(byte[] GID)
    {
        Payload.get(TAG.GID).RawData = GID;
    }

    public void SetFurtherAction(Byte FA)
    {
        byte[] _FA = new byte[1];
        _FA[0] = FA;
        Payload.get(TAG.FURTHER_ACTION).RawData = _FA;
    }

    public void SetVINGIDSync(Byte SVG)
    {
        byte[] _SVG = new byte[1];
        _SVG[0] = (SVG);
        Payload.get(TAG.VIN_GID).RawData = _SVG;
    }
}
