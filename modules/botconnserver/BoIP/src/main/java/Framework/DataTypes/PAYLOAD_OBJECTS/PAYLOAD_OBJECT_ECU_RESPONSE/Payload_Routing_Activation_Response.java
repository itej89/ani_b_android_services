package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Routing_Activation_Response extends PayloadObject {
    public void initilize_PayloadItems()
    {
        Payload.put(TAG.LOGICAL_ADDRESS_TEST_EQUIPMENT, new Payload_Item_Type( 0, 2));
        Payload.put(TAG.LOGICAL_ADDRESS_DOIP_ENTITY, new Payload_Item_Type( 2, 2));
        Payload.put(TAG.ROUTING_ACTIVATION_RESPONSE_CODE, new Payload_Item_Type( 4, 1));
        Payload.put(TAG.ISO_RESERVED, new Payload_Item_Type( 5, 4));
        Payload.put(TAG.OEM_RESERVED, new Payload_Item_Type( 9));
    }

    public Payload_Routing_Activation_Response() {
        super();
        initilize_PayloadItems();
    }

    public Payload_Routing_Activation_Response(byte[] DOIPPayload, long payload_length) {
        super();
        initilize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public void SetTestEquipmentLogicalAddress(byte[] TELA)
    {
         Payload.get(TAG.LOGICAL_ADDRESS_TEST_EQUIPMENT).RawData = TELA;
    }

    public void SetDOIPEntityLogicalAddress(byte[] DELA)
    {
         Payload.get(TAG.LOGICAL_ADDRESS_DOIP_ENTITY).RawData = DELA;
    }

    public void SetRoutingActivationResponseCode(byte[] RARC)
    {
        Payload.get(TAG.ROUTING_ACTIVATION_RESPONSE_CODE).RawData = RARC;
    }

    public void SetISOReserved(byte[] ISORes)
    {
        Payload.get(TAG.ISO_RESERVED).RawData = ISORes;
    }

    public void SetOEMReserved(byte[] OEMRes)
    {
        Payload.get(TAG.OEM_RESERVED).RawData = OEMRes;
    }


}
