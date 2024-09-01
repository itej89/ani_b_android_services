package Framework.DataTypes.PAYLOAD_TYPE_OBJECT_MAP;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Alive_Check_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Routing_Activation_Request;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Vehicle_Request_EID;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Vehicle_Request_VIN;
import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Diagnostic_Message;
import Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types;

public class Request_Payload_Type_To_Object {
    public PayloadObject GetPayloadObjectOfType(Request_Payload_Types.CODE Payload_Type, byte[] Payload, long payload_length)
    {
        PayloadObject payloadObject = new PayloadObject();
        switch(Payload_Type) {
            case PLD_VEH_IDEN_REQ:
        break;
        case PLD_VEH_IDEN_REQ_EID:
        payloadObject = new Payload_Vehicle_Request_EID(Payload, payload_length);
        break;
        case PLD_VEH_IDEN_REQ_VIN:
        payloadObject = new Payload_Vehicle_Request_VIN(Payload, payload_length);
        break;
        case PLD_ROUTING_ACTIVATION_REQ:
        payloadObject = new Payload_Routing_Activation_Request(Payload, payload_length);
        break;
        case PLD_ALIVE_CHECK_RES:
        payloadObject = new Payload_Alive_Check_Response(Payload, payload_length);
        break;
        case PLD_DOIP_ENTITY_STATUS_REQ:
        break;
        case PLD_DIAG_POWER_MODE_REQ:
        break;
        case PLD_RESERVED_ISO13400:
        break;
        case PLD_DIAG_MESSAGE:
        payloadObject = new Payload_Diagnostic_Message(Payload, payload_length);
        break;
        case PLD_MANUFACTURER_SPECIFIC_ACK:
        break;
        case DOIPTester_UNKNOWN_CODE:
        break;
    }
        return payloadObject;
    }
}
