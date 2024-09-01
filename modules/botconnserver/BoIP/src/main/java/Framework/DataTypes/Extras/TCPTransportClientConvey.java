package Framework.DataTypes.Extras;

import Framework.DataTypes.IPEndPoint;
import Framework.DataTypes.Transports.Helpers.RecievedData;

public interface TCPTransportClientConvey {
    void TCP_DataRecieved(RecievedData recievedData);
    void TCP_Disconnected(IPEndPoint endPoint);
    void TCP_Timeout(IPEndPoint endPoint, Integer code);
}
