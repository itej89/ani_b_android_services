package Framework.DataTypes.Extras;

import Framework.DataTypes.Transports.Helpers.RecievedData;

public interface UDPTransportClientConvey {
    void UDP_DataRecieved(RecievedData recievedData);
    void UDP_Disconnected();
    void UDP_Timeout(Integer code);
}
