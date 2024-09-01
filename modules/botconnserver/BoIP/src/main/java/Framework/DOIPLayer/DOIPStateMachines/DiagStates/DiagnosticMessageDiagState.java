package Framework.DOIPLayer.DOIPStateMachines.DiagStates;

import android.util.Pair;

import java.util.ArrayList;

import Framework.DOIPFrameHelpers.DOIPFrameSynthesizer;
import Framework.DOIPLayer.DOIPSession;
import Framework.DOIPLayer.DOIPStateMachines.DOIPTesterContext;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagStateForSendRecieve;
import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.Extras.DOIPContextConvey;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Routing_Activation_Response;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_TESTER_REQUEST.Payload_Routing_Activation_Request;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Diagnostic_Message;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;
import Framework.TransportLayer.TCPServer;
import Framework.Validation.DIAGNOSTIC_STATUS;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class DiagnosticMessageDiagState implements IDiagStateForSendRecieve {
    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();

    public DiagnosticMessageDiagState()
    {
    }

    public Integer _Init(byte[] arrDataToBeSent, IPEndPoint EndPoint)  {

        Pair<Integer, byte[]> returnStatus = FormatRequest(arrDataToBeSent);
        if(returnStatus.first == DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal())
        {
            Integer Status = returnStatus.first;

            if(returnStatus.second != null)
            {
                Status = SendData(EndPoint, returnStatus.second);
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

            Payload_Diagnostic_Message Payload = (Payload_Diagnostic_Message)(objRequest.GetPayload());

            DOIPSession.Instance.LastDiagnosticRequestData = Payload.GetUserData();
            DOIPTesterContext.Instance.ContextConvey.UDSResponseRecieved(objRequest.EndPoint, DOIPSession.Instance.LastDiagnosticRequestData);

            return new Pair<Integer, ArrayList<Byte>>(0, bufferResponse);
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Routing Activation Request Object"));
            return new Pair<Integer, ArrayList<Byte>>(DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal(), bufferResponse);
        }
    }


    Pair<Integer, byte[]> FormatRequest(byte[] arrDataToBeSent)
    {
        byte[] data = null;

        DOIPFrameSynthesizer formatRequest = new DOIPFrameSynthesizer();

        Payload_Diagnostic_Message objPayload_Diagnostic_Message = new Payload_Diagnostic_Message();

        objPayload_Diagnostic_Message.SetSourceAddress(DOIPSession.Instance.LogicalAddress);
        objPayload_Diagnostic_Message.SetTargetAddress(DOIPSession.Instance.TargetAddress);
        objPayload_Diagnostic_Message.SetUserData(arrDataToBeSent);

        DOIPResponseObject request = formatRequest.FormHeaderForDoIPFrame( Response_Payload_Types.CODE.PLD_DIAG_MESSAGE);

        request.SetPayload( objPayload_Diagnostic_Message);

        data  = formatRequest.CreateDOIPFrame(request);

        return new Pair<Integer, byte[]>(DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal(), data);
    }

    Integer SendData(IPEndPoint EndPoint, byte[] data)
    {
        TCPServer.Instance.SendData(EndPoint, data);
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
