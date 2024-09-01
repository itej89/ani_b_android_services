package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST;

import java.util.ArrayList;
import java.util.Map;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;

public class Payload_Routing_Activation_Request extends PayloadObject {
    public void initialize_PayloadItems()
    {
        Payload.put(TAG.SOURCE_ADDRESS, new Payload_Item_Type( 0, 2));
        Payload.put(TAG.ACTIVATION_TYPE, new Payload_Item_Type( 2, 1));
        Payload.put(TAG.ISO_RESERVED, new Payload_Item_Type( 3, 4));
        Payload.put(TAG.OEM_RESERVED, new Payload_Item_Type( 7));
    }

    public  Payload_Routing_Activation_Request() {
        super();
        initialize_PayloadItems();
    }

    public  Payload_Routing_Activation_Request(byte[] DOIPPayload, long payload_length) {
        super();
        initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public byte[] GetSourceAddress()
    {
       return Payload.get(TAG.SOURCE_ADDRESS).RawData;
    }

    public Byte GetActivationType()
    {
        return Payload.get(TAG.ACTIVATION_TYPE).RawData[0];
    }

    public byte[]  GetISOReserved()
    {
       return Payload.get(TAG.ISO_RESERVED).RawData;
    }

    public byte[]  GetOEMReserved()
    {
       return Payload.get(TAG.OEM_RESERVED).RawData;
    }

    public  long TotalNumberOfBytesInPayload() {
        long ValidData_Length = 0;

        for(Map.Entry<TAG, Payload_Item_Type> pair:Payload.entrySet())
        {
            if(pair.getValue().RawData != null)
            {
                ValidData_Length = ValidData_Length + (pair.getValue().RawData.length);
            }
        }
        return ValidData_Length;
    }

    public byte[]  Make_Payload()
    {
        long Required_Payload_Length = TotalNumberOfBytesInPayload();

        if(Required_Payload_Length != 0)
        {
            if(Payload.get(TAG.SOURCE_ADDRESS).RawData == null)
            {
                ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.NO_SOURCE_ADDRESS,  "Payload_Routing_Activation_Request:Make_Payload", ""));
            }

            byte[] DOIP_Payload = new byte[
                    Payload.get(TAG.SOURCE_ADDRESS).RawData.length +
                    Payload.get(TAG.ACTIVATION_TYPE).RawData.length +
                    Payload.get(TAG.ISO_RESERVED).RawData.length +
                    Payload.get(TAG.OEM_RESERVED).RawData.length];

            System.arraycopy(Payload.get(TAG.SOURCE_ADDRESS).RawData, 0, DOIP_Payload, Payload.get(TAG.SOURCE_ADDRESS).Position, Payload.get(TAG.SOURCE_ADDRESS).RawData.length);
            System.arraycopy(Payload.get(TAG.ACTIVATION_TYPE).RawData, 0, DOIP_Payload, Payload.get(TAG.ACTIVATION_TYPE).Position, Payload.get(TAG.SOURCE_ADDRESS).RawData.length);
            System.arraycopy(Payload.get(TAG.ISO_RESERVED).RawData, 0, DOIP_Payload, Payload.get(TAG.ISO_RESERVED).Position, Payload.get(TAG.SOURCE_ADDRESS).RawData.length);
            System.arraycopy(Payload.get(TAG.OEM_RESERVED).RawData, 0, DOIP_Payload, Payload.get(TAG.OEM_RESERVED).Position, Payload.get(TAG.SOURCE_ADDRESS).RawData.length);

            return DOIP_Payload;
        }

        ValidationErrors.Add(new ValidationRuleMessage( VALIDATION_ERROR_CODES.NO_PAYLOAD_ITEMS_FOUND,  "Payload_Routing_Activation_Request:Make_Payload", ""));

        return null;
    }
}
