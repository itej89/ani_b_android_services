package Framework.DOIPLayer.DOIPStateMachines.DiagStates;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Framework.DOIPFrameHelpers.DOIPFrameSynthesizer;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagStateForSendRecieve;
import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;
import Framework.TransportLayer.TCPServer;
import Framework.Validation.DIAGNOSTIC_STATUS;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class AliveCheckDiagState implements IDiagStateForSendRecieve
{
    public ValidationRuleMessages ValidationErrors;

    public AliveCheckDiagState()
    {
        ValidationErrors = new ValidationRuleMessages();
    }

    public Integer _Init(byte[] arrDataToBeSent, IPEndPoint EndPoint)  {

        Pair<Integer, byte[]> returnStatus =  FormatRequest();
    if(returnStatus.first == DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal())
    {
        Integer status = returnStatus.first;
        if(returnStatus.second != null)
        {
            status = SendData(EndPoint, returnStatus.second);
        }
        return status;
    }
    else
    {
        return DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal();
    }
}

    public Pair<Integer, ArrayList<Byte>> HandleIncomingData(DOIPRequestObject objRequest)
    {
        ArrayList<Byte> bufferResponse = null;
        IPEndPoint EndPoint = new IPEndPoint(objRequest.EndPoint.IPAddress, objRequest.EndPoint.Port);

        if(objRequest != null)
        {
            return new Pair<Integer, ArrayList<Byte>>(0, bufferResponse);
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Alive Check Request Object"));
            return new Pair<Integer, ArrayList<Byte>>(DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal(), bufferResponse);
        }
    }


    Pair<Integer, byte[]> FormatRequest()
    {
        byte[] data = null;

        DOIPFrameSynthesizer formatRequest = new DOIPFrameSynthesizer();

        DOIPResponseObject request = formatRequest.FormHeaderForDoIPFrame(Response_Payload_Types.CODE.PLD_ALIVE_CHECK_REQ);

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
