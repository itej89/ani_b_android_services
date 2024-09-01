package Framework.DataTypes.PAYLOAD_TYPE_OBJECT_MAP;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Diagnostic_Message_Ack;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Diagnostic_Message_Nack;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Diagnostic_Power_Mode_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Diagnostic_Status_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Generic_Header_NACK;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Routing_Activation_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Vehicle_Announcement;
import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Diagnostic_Message;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;

import static Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types.CODE.PLD_VEH_IDEN_RES;

public class Response_Payload_Type_To_Object {

    public PayloadObject GetPayloadObjectOfType(Response_Payload_Types.CODE Payload_Type, byte[] Payload, long payloadLength)
    {
        PayloadObject payloadObject = new PayloadObject();
        switch(Payload_Type) {
        case PLD_VEH_IDEN_RES:
        payloadObject = new Payload_Vehicle_Announcement(Payload, payloadLength);
        break;
        case PLD_ROUTING_ACTIVATION_RES:
        payloadObject = new Payload_Routing_Activation_Response(Payload, payloadLength);
        break;
        case PLD_DOIP_ENTITY_STATUS_RES:
        payloadObject = new Payload_Diagnostic_Status_Response(Payload, payloadLength);
        break;
        case PLD_DIAG_POWER_MODE_RES:
        payloadObject = new Payload_Diagnostic_Power_Mode_Response(Payload, payloadLength);
        break;
        case PLD_DOIP_HEADER_NAK: payloadObject = new Payload_Generic_Header_NACK(Payload, payloadLength);
        break;
        case PLD_DIAG_MESSAGE_POSITIVE_ACK:
        payloadObject = new Payload_Diagnostic_Message_Ack(Payload, payloadLength);
        break;
        case PLD_DIAG_MESSAGE_NEGATIVE_ACK:
        payloadObject = new Payload_Diagnostic_Message_Nack(Payload, payloadLength);
        break;
        case PLD_DIAG_MESSAGE:
        payloadObject = new Payload_Diagnostic_Message(Payload, payloadLength);
        break;
        case PLD_ALIVE_CHECK_REQ:
        break;
        case PLD_MANUFACTURER_SPECIFIC_ACK:
        break;
        case PLD_RESERVED_ISO13400:
        break;
        case DOIPTester_UNKNOWN_CODE:
        break;
    }

        if(payloadObject.ValidationErrors.Messages.size() > 0)
        {
            return null;
        }
        else
        {
            return payloadObject;
        }
    }
}
