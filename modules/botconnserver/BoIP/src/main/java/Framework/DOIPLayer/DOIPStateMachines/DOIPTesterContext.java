package Framework.DOIPLayer.DOIPStateMachines;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Framework.DOIPFrameHelpers.DOIPFrameParser;
import Framework.DOIPLayer.DOIPSession;
import Framework.DOIPLayer.DOIPStateMachines.DiagStates.AliveCheckDiagState;
import Framework.DOIPLayer.DOIPStateMachines.DiagStates.DiagnosticMessageDiagState;
import Framework.DOIPLayer.DOIPStateMachines.DiagStates.RoutingActivationDiagState;
import Framework.DOIPLayer.DOIPStateMachines.DiagStates.VehicleAnnouncementDiagState;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagState;
import Framework.DOIPLayer.DOIPStateMachines.Interface.IDiagStateForSendRecieve;
import Framework.DOIPLayer.DoIPGenericHeaderHandler;
import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.DataTypes.Extras.DOIPContextConvey;
import Framework.DataTypes.Extras.DOIPContextResultConvey;
import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.Extras.TCPTransportClientConvey;
import Framework.DataTypes.Extras.UDPTransportClientConvey;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import Framework.TransportLayer.TCPServer;
import Framework.TransportLayer.UDPListen;
import Framework.Validation.DIAGNOSTIC_STATUS;

import static java.lang.Thread.sleep;

public class DOIPTesterContext implements TCPTransportClientConvey, UDPTransportClientConvey
{
public static DOIPTesterContext Instance = new DOIPTesterContext();

Map<Request_Payload_Types.CODE, IDiagStateForSendRecieve> payloadType_DiagState = new HashMap<Request_Payload_Types.CODE, IDiagStateForSendRecieve>();

public DOIPTesterContext()
{
    TCPServer.Instance.TCPNotify = this;
    UDPListen.Instance.UDPNotify = this;

    payloadType_DiagState.put(Request_Payload_Types.CODE.PLD_VEH_IDEN_REQ, new VehicleAnnouncementDiagState());
    payloadType_DiagState.put(Request_Payload_Types.CODE.PLD_ROUTING_ACTIVATION_REQ, new RoutingActivationDiagState());
    payloadType_DiagState.put(Request_Payload_Types.CODE.PLD_ALIVE_CHECK_RES, new AliveCheckDiagState());
    payloadType_DiagState.put(Request_Payload_Types.CODE.PLD_DIAG_MESSAGE, new DiagnosticMessageDiagState());
}

ArrayList<Pair<Boolean, DOIPRequestObject>> DOIPBuffer = new ArrayList<Pair<Boolean, DOIPRequestObject>>();

Boolean IsVehicleIdentificationResponseRecieved = false;

Boolean StopDataParsingLoop = true;
Boolean DataParsingStopped = true;
Boolean DataParsingStarted = false;

IDiagStateForSendRecieve _currentState;
public IDiagState getCurrentState()
        {
        return _currentState;
        }
public void setCurrentState(IDiagStateForSendRecieve newState)
        {
        _currentState = newState;
        }

    IDiagState _previousState;
public IDiagState getPreviousState()
        {
        return _previousState;
        }
public void setPreviousState(IDiagState oldState)
        {
        _previousState = oldState;
        }


public ArrayList<RecievedData> TCPDataBuffer = new ArrayList<RecievedData>();
public Pair<Boolean, ArrayList<RecievedData>> DOIPFrameBuffer = new Pair<Boolean, ArrayList<RecievedData>>(false, null);

public void ParseTCPData(RecievedData recData)
        {
                    while (recData.Data.length >= DOIPRequestObject.GetHeaderLength()) {
                        if(!DoIPGenericHeaderHandler.Instance.ValidateHeader(new byte[]{recData.Data[0], recData.Data[1]}))
                        {
                            byte[] leftover = new byte[recData.Data.length - 1];
                            System.arraycopy(recData.Data, 1, leftover, 0, leftover.length);
                            recData.Data = leftover;
                            continue;
                        }

                        byte[] PayloadTypeBytes = new byte[]{recData.Data[3], recData.Data[2]};

                       if( Request_Payload_Types.CODE.DOIPTester_UNKNOWN_CODE ==  Request_Payload_Types.Instance.Decode(PayloadTypeBytes))
                       {
                           byte[] leftover = new byte[recData.Data.length - 6];
                           System.arraycopy(recData.Data, 6, leftover, 0, leftover.length);
                           recData.Data = leftover;
                           continue;
                       }


                        DOIPFrameParser parser = new DOIPFrameParser();
                        DOIPRequestObject objRequest = parser.Parse((recData.Data));

                        if(objRequest != null) {
                            objRequest.EndPoint.IPAddress = recData.IPAddress;
                            objRequest.EndPoint.Port = recData.Port;
                            if (DOIPResponseObject.Number_Of_Bytes_In_Header + objRequest.GetPayloadLength() == recData.Data.length) {
                                DOIPBuffer.add(new Pair<Boolean, DOIPRequestObject>(true, objRequest));


                                if(DOIPBuffer.get(0).second.GetPayLoadType() != Request_Payload_Types.CODE.DOIPTester_UNKNOWN_CODE)

                            if(DoIPGenericHeaderHandler.Instance.ValidateHeader(DOIPBuffer.get(0).second))
                            {
                                if(DOIPBuffer.get(0).second.GetPayLoadType() == Request_Payload_Types.CODE.PLD_DIAG_MESSAGE)
                                {
                                    HandleDiagnostic(DOIPBuffer.get(0).second);
                                }
                                else
                                {
                                    HandleIncomingData( DOIPBuffer.get(0).second);

                                }
                                DOIPBuffer.remove(0);
                            }


                                TCPDataBuffer.remove(recData);
                                break;
                            } else {
                                if (DOIPResponseObject.Number_Of_Bytes_In_Header + objRequest.GetPayloadLength() < recData.Data.length) {
                                    Long FirstFrameLength = DOIPRequestObject.GetHeaderLength() + objRequest.GetPayloadLength();
                                    DOIPBuffer.add(new Pair<Boolean, DOIPRequestObject>(true, objRequest));
                                    byte[] leftover = new byte[recData.Data.length - FirstFrameLength.intValue()];
                                    System.arraycopy(recData.Data, FirstFrameLength.intValue(), leftover, 0, leftover.length);
                                    recData.Data = leftover;

                                    if(DOIPBuffer.get(0).second.GetPayLoadType() != Request_Payload_Types.CODE.DOIPTester_UNKNOWN_CODE)

                                        if(DoIPGenericHeaderHandler.Instance.ValidateHeader(DOIPBuffer.get(0).second))
                                        {
                                            if(DOIPBuffer.get(0).second.GetPayLoadType() == Request_Payload_Types.CODE.PLD_DIAG_MESSAGE)
                                            {
                                                HandleDiagnostic(DOIPBuffer.get(0).second);
                                            }
                                            else
                                            {
                                                HandleIncomingData( DOIPBuffer.get(0).second);

                                            }
                                            DOIPBuffer.remove(0);
                                        }

                                        continue;

                                } else {
                                    break;
                                }
                            }
                        } else {
                            break;
                        }
                    }
        }

public DOIPContextConvey ContextConvey;
public DOIPContextResultConvey ResultConvey;

//DOIP Communication Initializing sequence
public Integer Initialize(DOIPContextConvey _ContexTConvey, DOIPContextResultConvey _ResultConvey)
        {
        ResultConvey = _ResultConvey;
        ContextConvey = _ContexTConvey;
        UDPListen.Instance.Start();
        TCPServer.Instance.StartServer();
           // StartDataParser();
        return DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal();
        }


        public  void Uninitialize()
        {
            UDPListen.Instance.Stop();
            TCPServer.Instance.StopServer();
            StopDataParsingLoop = true;
            if(DataParsingStarted)
            {
                    while(!DataParsingStopped)
                    {
                        try {
                            sleep(10);
                        }
                        catch (Exception e){}
                    }
            }
        }

        private void HandleIncomingData(DOIPRequestObject data)
        {
            Request_Payload_Types.CODE responseCode = data.GetPayLoadType();

            Pair<Integer, ArrayList<Byte>> returnStatus = new Pair<Integer, ArrayList<Byte>>(DIAGNOSTIC_STATUS.CODE.INTERNAL_ERROR.ordinal(), null);

            if(payloadType_DiagState.keySet().contains(data.GetPayLoadType()))
            {
                returnStatus = payloadType_DiagState.get(responseCode).HandleIncomingData(data);
            }

            if(returnStatus.first != (DIAGNOSTIC_STATUS.CODE.SUCCESS.ordinal()));
            {
                if(ResultConvey != null && responseCode.equals(Request_Payload_Types.CODE.PLD_ROUTING_ACTIVATION_REQ))
                {
                    ResultConvey.InitializeResultNotify(data.EndPoint,  returnStatus.first);
                }
            }
        }

        private void HandleDiagnostic(DOIPRequestObject objRequest)
        {
            if(objRequest != null)
            {
                Pair<Integer, ArrayList<Byte>>  returnStatus = payloadType_DiagState.get(Request_Payload_Types.CODE.PLD_DIAG_MESSAGE).HandleIncomingData(objRequest);
                if(ContextConvey != null)
                {
                  ResultConvey.UDSSendResultNotify(objRequest.EndPoint ,returnStatus.first);
                }
            }
        }

        public void FianlizeDiag()
        {}

        public  void SendData(IPEndPoint _IPEndpoint,  byte[] Data)
        {
            setCurrentState(payloadType_DiagState.get(Request_Payload_Types.CODE.PLD_DIAG_MESSAGE));
            _currentState._Init((Data), _IPEndpoint);
        }


        boolean IsDOIPBufferLooping = false;

    //TCPTransportClientConvey
    public void TCP_DataRecieved(RecievedData recievedData){
        if(recievedData != null && recievedData.Data != null && recievedData.Data.length > 0)
        {
            boolean IsFilled = false;
            for(RecievedData recData : TCPDataBuffer)
            {
                if (recData.IPAddress.equals(recievedData.IPAddress) && recData.Port.equals(recievedData.Port)) {
                    byte[] data = new byte[recData.Data.length + recievedData.Data.length];
                    System.arraycopy(recData.Data, 0, data, 0, recData.Data.length);
                    System.arraycopy(recievedData.Data, 0, data, recData.Data.length, recievedData.Data.length);
                    recData.Data = data;
                    IsFilled = true;
                    ParseTCPData(recData);
                    break;
                }
            }
            if(!IsFilled) {
                TCPDataBuffer.add(recievedData);
                ParseTCPData(recievedData);
            }
        }
        else
        {
            ResultConvey.UDSSendResultNotify(null, DIAGNOSTIC_STATUS.CODE.INVALID_HEADER.ordinal());
        }
    }
    public void TCP_Disconnected(IPEndPoint endPoint){
        FianlizeDiag();
        if(ResultConvey != null)
        {
            ResultConvey.LinkDisconnected(endPoint);
        }
    }

    public void TCP_Timeout(IPEndPoint endPoint, Integer code){
        if(code  == DIAGNOSTIC_STATUS.CODE.DIAG_ACK_TIMEOUT.ordinal())
        {
            ResultConvey.UDSSendResultNotify(endPoint, code);
        }
        else
        {
            ResultConvey.InitializeResultNotify(endPoint, code);
        }
    }
    //TCPTransportClientConvey



    //UDPTransportClientConvey
    public void UDP_Disconnected() {

    }

    public void StartDataParser() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                 StopDataParsingLoop = false;
                 DataParsingStopped = false;
                 DataParsingStarted = true;

                 while (!StopDataParsingLoop)
                 {
                    int i = 0;
                    while(i < DOIPBuffer.size()-1) {

                        if(DOIPBuffer.get(i) == null)
                        {
                            DOIPBuffer.remove(i);
                            continue;
                        }

                        IsDOIPBufferLooping = true;

                        if(DOIPBuffer.get(i).first)
                        {
                            if(DOIPBuffer.get(i).second.GetPayLoadType() == Request_Payload_Types.CODE.DOIPTester_UNKNOWN_CODE)
                            {
                                DOIPBuffer.remove(i);
                                continue;
                            }
                            if(DoIPGenericHeaderHandler.Instance.ValidateHeader(DOIPBuffer.get(i).second))
                            {
                                if(DOIPBuffer.get(i).second.GetPayLoadType() == Request_Payload_Types.CODE.PLD_DIAG_MESSAGE)
                                {
                                    HandleDiagnostic(DOIPBuffer.get(i).second);
                                }
                                else
                                {
                                    HandleIncomingData( DOIPBuffer.get(i).second);

                                }
                                DOIPBuffer.remove(i);
                                continue;
                            }
                            else
                            {
                                DOIPBuffer.remove(i);
                                continue;
                            }
                        }
                        else
                        {
                            DOIPBuffer.remove(i);
                            continue;
                        }
                    }
                     try {
                         sleep(1);
                     }
                     catch (Exception e){}
                 }

                if(StopDataParsingLoop)
                {
                    DataParsingStopped = true;
                    DataParsingStarted = false;
                }
            }
        });
        t.start();
    }


public void UDP_DataRecieved(RecievedData recievedData) {
        if(recievedData != null && recievedData.Data != null && recievedData.Data.length > 0)
        {
            boolean IsFilled = false;
            for(RecievedData recData : TCPDataBuffer)
            {
                    if (recData.IPAddress.equals(recievedData.IPAddress) && recData.Port.equals(recievedData.Port)) {
                        byte[] data = new byte[recData.Data.length + recievedData.Data.length];
                        System.arraycopy(recData.Data, 0, data, 0, recData.Data.length);
                        System.arraycopy(recievedData.Data, 0, data, recData.Data.length, recievedData.Data.length);
                        recData.Data = data;
                        IsFilled = true;
                        ParseTCPData(recData);
                        break;
                    }
            }

            if(!IsFilled) {
                TCPDataBuffer.add(recievedData);
                ParseTCPData(recievedData);
            }
        }
        else
        {
            ResultConvey.UDSSendResultNotify(null, DIAGNOSTIC_STATUS.CODE.INVALID_HEADER.ordinal());
        }
    }



public void UDP_Timeout(Integer code) {

        }
        //End of TransportClientConvey
}
