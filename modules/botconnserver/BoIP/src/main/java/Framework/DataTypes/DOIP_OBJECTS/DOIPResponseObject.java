package Framework.DataTypes.DOIP_OBJECTS;

import Framework.DataTypes.PAYLOAD_OBJECTS.PayloadObject;
import Framework.DataTypes.PAYLOAD_TYPES.Response_Payload_Types;

public class DOIPResponseObject {
    //1 byte protocol version, 1-byte Invers protocol version
    //2 byte Payload Type, 4-bytes - Payload length

    public static Integer Number_Of_Bytes_In_Header = 8;

    private byte ProtocolVersion = (byte)0xff;
    private byte InverseProtocolVersion = 0x00;

    private Response_Payload_Types.CODE Res_Payload_Type;
    private PayloadObject Payload;
    private long PayloadLength = 0;

    //Returns standard header length in bytes
    public Integer GetHeaderLength()
    {
        return DOIPResponseObject.Number_Of_Bytes_In_Header;
    }

    public void SetProtocolVersion(byte _ProtocolVersion)
    {
        ProtocolVersion = _ProtocolVersion;
        SetInverseProtocolVersion((byte)(_ProtocolVersion ^ 0xFF));
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

    public void SetPayloadType(Response_Payload_Types.CODE _Res_Payload_Type)
    {
         Res_Payload_Type = _Res_Payload_Type;
    }

    public  Response_Payload_Types.CODE GetPayLoadType()
    {
        return Res_Payload_Type;
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
