package Framework.DataTypes;

public class BufferDataPacket
{
    public String FrameID;
    public String data;
    public String toChannel;

        public BufferDataPacket(String _FrameID, String _data, String _toChannel)
        {
            FrameID = _FrameID;
            data = _data;
            toChannel = _toChannel;
        }
}
