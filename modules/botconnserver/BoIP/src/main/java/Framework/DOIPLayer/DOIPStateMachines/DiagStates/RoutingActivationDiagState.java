package Framework.DOIPLayer.DOIPStateMachines.DiagStates;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Pair;

import java.util.ArrayList;

import Framework.DOIPFrameHelpers.DOIPFrameSynthesizer;
import Framework.DOIPLayer.DOIPSession;
import Framework.DOIPLayer.DOIPStateMachines.DOIPTesterContext;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagStateForSendRecieve;
import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.ECU_CODE_VALUES.Activation_Response_Codes;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Routing_Activation_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Vehicle_Announcement;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Routing_Activation_Request;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;
import Framework.TransportLayer.TCPServer;
import Framework.Validation.DIAGNOSTIC_STATUS;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class RoutingActivationDiagState implements IDiagStateForSendRecieve {
    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();

    public RoutingActivationDiagState()
    {
    }

    public Integer _Init(byte[] arrDataToBeSent, IPEndPoint EndPoint)  {

        Pair<Integer, byte[]> returnStatus = FormatRequest();
        if(returnStatus.first == DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal())
        {
            Integer Status = returnStatus.first;

            if(returnStatus.second != null)
            {
                Status = SendData(EndPoint, returnStatus.second);
                if(Status == DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal())
                {
                   DOIPTesterContext.Instance.ResultConvey.LinkConnected(EndPoint);
                }
            }
            return Status;
        }
        else
        {
            return DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal();
        }
    }

    public Pair<Integer, ArrayList<Byte>> HandleIncomingData(DOIPRequestObject objRequest) {

        ArrayList<Byte> bufferResponse = null;
        IPEndPoint EndPoint = new IPEndPoint(objRequest.EndPoint.IPAddress, objRequest.EndPoint.Port);
        if(objRequest != null)
        {

            Payload_Routing_Activation_Request Payload = (Payload_Routing_Activation_Request)(objRequest.GetPayload());

            DOIPSession.Instance.TargetAddress = Payload.GetSourceAddress();
            DOIPSession.Instance.ActivationType = Payload.GetActivationType();

            return new Pair<Integer, ArrayList<Byte>>(_Init(null, EndPoint), bufferResponse);
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Routing Activation Request Object"));
            return new Pair<Integer, ArrayList<Byte>>(DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal(), bufferResponse);
        }
    }


    Pair<Integer, byte[]> FormatRequest()
    {
        byte[] data = null;

        DOIPFrameSynthesizer formatRequest = new DOIPFrameSynthesizer();

        Payload_Routing_Activation_Response objRoutingActivationResponse = new Payload_Routing_Activation_Response();

        objRoutingActivationResponse.SetTestEquipmentLogicalAddress((DOIPSession.Instance.TargetAddress));

        objRoutingActivationResponse.SetDOIPEntityLogicalAddress((DOIPSession.Instance.LogicalAddress));
        objRoutingActivationResponse.SetRoutingActivationResponseCode((new byte[]{Activation_Response_Codes.Instance.ENCODE(Activation_Response_Codes.CODE.RA_RES_SUCCESS)}));
        objRoutingActivationResponse.SetISOReserved((new byte[]{0,0,0,0}));
        objRoutingActivationResponse.SetOEMReserved((new byte[]{0}));

        DOIPResponseObject response = formatRequest.FormHeaderForDoIPFrame( Response_Payload_Types.CODE.PLD_ROUTING_ACTIVATION_RES);

        response.SetPayload( objRoutingActivationResponse);

        data  = formatRequest.CreateDOIPFrame(response);

        return new Pair<Integer, byte[]>(DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal(), data);
    }

    Integer SendData(IPEndPoint endpoint, byte[] data)
    {
        TCPServer.Instance.SendData(endpoint, data);
        if(TCPServer.Instance.ValidationErrors.Messages.size() > 0)
        {
            return DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal();
        }
        else
        {
            return DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal();
        }
    }
}
