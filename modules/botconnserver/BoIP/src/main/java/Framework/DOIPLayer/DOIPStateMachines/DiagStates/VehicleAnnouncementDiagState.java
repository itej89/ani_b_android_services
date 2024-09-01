package Framework.DOIPLayer.DOIPStateMachines.DiagStates;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Pair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import Framework.DOIPFrameHelpers.DOIPFrameSynthesizer;
import Framework.DOIPLayer.DOIPSession;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagStateForSendRecieve;
import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE.Payload_Vehicle_Announcement;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;
import Framework.TransportLayer.TCPServer;
import Framework.TransportLayer.UDPListen;
import Framework.Validation.DIAGNOSTIC_STATUS;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class VehicleAnnouncementDiagState implements IDiagStateForSendRecieve {
    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();

    public VehicleAnnouncementDiagState()
    {
    }

    public Integer _Init(byte[] arrDataToBeSent, IPEndPoint EndPoint)  {

        Pair<Integer, byte[]> returnStatus = FormatRequest();
        if(returnStatus.first == DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal())
        {
            Integer Status = returnStatus.first;

            if(returnStatus.second != null)
            {
                Status = SendData(returnStatus.second,  EndPoint);
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
            return new Pair<Integer, ArrayList<Byte>>(_Init(null, EndPoint), bufferResponse);
        }
        else
        {
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Vehicle Identification  Request Object"));
            return new Pair<Integer, ArrayList<Byte>>(DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal(), bufferResponse);
        }
    }

    Pair<Integer, byte[]> FormatRequest()
    {
        byte[] data = null;

        DOIPFrameSynthesizer formatRequest = new DOIPFrameSynthesizer();

        Payload_Vehicle_Announcement objVehicleAnnouncementPayload = new Payload_Vehicle_Announcement();


        WifiManager wifiMan = (WifiManager)GlobalContext.context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String macAddr = wifiInf.getMacAddress();

        objVehicleAnnouncementPayload.SetVIN(("Tej's A.n.i".getBytes()));
        objVehicleAnnouncementPayload.SetEID((macAddr.getBytes()));
        objVehicleAnnouncementPayload.SetlogicalAddress((DOIPSession.Instance.LogicalAddress));
        objVehicleAnnouncementPayload.SetGID(("000000".getBytes()));
        objVehicleAnnouncementPayload.SetFurtherAction((byte)0x00);
        objVehicleAnnouncementPayload.SetVINGIDSync((byte)0x00);

        DOIPResponseObject request = formatRequest.FormHeaderForDoIPFrame( Response_Payload_Types.CODE.PLD_VEH_IDEN_RES);

        request.SetPayload( objVehicleAnnouncementPayload);

        data  = formatRequest.CreateDOIPFrame(request);

        return new Pair<Integer, byte[]>(DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal(), data);
    }

    Integer SendData(byte[] data, IPEndPoint EndPoint)
    {
        UDPListen.Instance.Send((data),
                EndPoint.IPAddress,
                EndPoint.Port);
        if(TCPServer.Instance.ValidationErrors.Messages.size() > 0)
        {
            return DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal();
        }
        else
        {
            DOIPSession.Instance.RemoteIPAddress.add(EndPoint);
            return DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal();
        }
    }
}
