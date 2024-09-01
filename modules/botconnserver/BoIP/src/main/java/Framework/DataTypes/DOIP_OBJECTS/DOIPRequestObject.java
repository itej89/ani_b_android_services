package Framework.DataTypes.DOIP_OBJECTS;

import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_TYPES.Request_Payload_Types;

public class DOIPRequestObject {
    //1 byte protocol version, 1-byte Invers protocol version
    //2 byte Payload Type, 4-bytes - Payload length

    public IPEndPoint EndPoint= new IPEndPoint();

    private static Integer Number_Of_Bytes_In_Header = 8;

    private byte ProtocolVersion = (byte)0xff;
    private byte InverseProtocolVersion = 0x00;
    private long PayloadLength = 0;

    private Request_Payload_Types.CODE Req_Payload_Type;
    private PayloadObject Payload;

    //Returns standard header length in bytes
    public static Integer GetHeaderLength()
    {
        return DOIPRequestObject.Number_Of_Bytes_In_Header;
    }

    public void SetProtocolVersion(byte _ProtocolVersion)
    {
        ProtocolVersion = _ProtocolVersion;
        SetInverseProtocolVersion((byte)(_ProtocolVersion^0xFF));
    }

    public byte GetProtocolVersion()
    {
        return ProtocolVersion;
    }

    public void SetInverseProtocolVersion(byte _InverseProtocolVersion)
    {
        InverseProtocolVersion = _InverseProtocolVersion;
    }

    public byte GetInverseProtocolVersion()
    {
        return InverseProtocolVersion;
    }

    public void SetPayloadType(Request_Payload_Types.CODE _Req_Payload_Type)
    {
         Req_Payload_Type = _Req_Payload_Type;
    }

    public Request_Payload_Types.CODE GetPayLoadType()
    {
        return Req_Payload_Type;
    }

    public void SetPayload(PayloadObject _Payload)
    {
        Payload = _Payload;
    }

    public PayloadObject GetPayload()
    {
        return Payload;
    }

    public void SetPayloadLength(long _PayloadLength)
    {
        PayloadLength = _PayloadLength;
    }

    public long GetPayloadLength()
    {
        return PayloadLength;
    }

}
