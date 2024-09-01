package Framework.DataTypes.PAYLOAD_OBJECTS.PAYLOAD_OBJECT_ECU_RESPONSE;

import java.util.ArrayList;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_OBJECTS.Payload_Item_Type;

public class Payload_Diagnostic_Status_Response extends  PayloadObject {

    public void Initialize_PayloadItems()
    {
        Payload.put(PayloadObject.TAG.NODE_TYPE, new Payload_Item_Type( 0, 1));
        Payload.put(PayloadObject.TAG.NODE_TYPE, new Payload_Item_Type( 1, 1));
        Payload.put(PayloadObject.TAG.NODE_TYPE, new Payload_Item_Type( 2, 1));
        Payload.put(PayloadObject.TAG.NODE_TYPE, new Payload_Item_Type( 3));
    }

    public  Payload_Diagnostic_Status_Response(byte[] DOIPPayload, long payload_length) {
        super();
        Initialize_PayloadItems();
        Decode_Payload(DOIPPayload, payload_length);
    }

    public void SetNodeType(Byte NT)
    {
        byte[] _NT= new byte[1];
        _NT[0] = (NT);
        Payload.get(TAG.NODE_TYPE).RawData = _NT;

    }

    public void SetMaxTCPConnections(Byte TCPConn)
    {
        byte[] _TCPConn= new byte[1];
        _TCPConn[0] = (TCPConn);
        Payload.get(TAG.MAX_CONCURRENT_TCP_SOCKETS).RawData = _TCPConn;
    }

    public void SetNumberOfCurrentOpenTCPConnections(Byte OTCPConn)
    {
        byte[] _OTCPConn= new byte[1];
        _OTCPConn[0] = (OTCPConn);
        Payload.get(TAG.CURRENT_OPEN_TCP_SOCKETS).RawData = _OTCPConn;
    }

    public void SetMaxDataSize(byte[] MAXDataSize)
    {
         Payload.get(TAG.MAX_DATA_SIZE).RawData = MAXDataSize;
    }
}
