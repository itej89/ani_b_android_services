package Framework.DOIPLayer;

import java.util.ArrayList;

import Framework.DataTypes.IPEndPoint;


public class DOIPSession {
    public static String NLOG_LOGGER_NAME = "BOIPLOGGER";

    public static DOIPSession Instance = new DOIPSession();

    public DOIPSession(){}

    private byte[][] InvalidAddresses = new byte[0][];

    public byte[][] getInvalidAddresses()
    {
        InvalidAddresses =  new byte[][]{{(byte)0xE0, 0x00}, {0x0F, (byte)0xFF}};
        return InvalidAddresses;
    }

    private ArrayList<Byte> ValidProtocolVersions = new ArrayList<Byte>();

    public ArrayList<Byte> getValidProtocolVersions()
    {
        ValidProtocolVersions.add((byte)0xFF);
        ValidProtocolVersions.add((byte)0x01);
        ValidProtocolVersions.add((byte)0x02);
        return ValidProtocolVersions;
    }

    public final Integer LOCAL_TCP_PORT = 13400;
    public final Integer LOCAL_UDP_PORT = 13400;

    public byte DefaultProtocolVersion = (byte)0xFF;
    public byte DefaultInverseProtocolVersion()
    {
        return (byte)(DefaultProtocolVersion ^ 0xFF);
    }

    public byte ProtocolVersion = (byte)0x02;
    public byte InverseProtocolVersion()
    {
        return (byte)(ProtocolVersion ^ 0xFF);
    }

    public byte[] TargetAddress = new byte[]{0x00, 0x00};
    public byte[] LogicalAddress = new byte[]{0x42, 0x4B};
    public byte ActivationType = 0x00;

    public ArrayList<IPEndPoint> RemoteIPAddress = new ArrayList<IPEndPoint>();

    public byte[] LastDiagnosticRequestData = new byte[0];

    public void ResetSession()
    {
    }
}
