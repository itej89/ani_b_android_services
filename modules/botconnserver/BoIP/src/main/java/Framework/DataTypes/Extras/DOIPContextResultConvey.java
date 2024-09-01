package Framework.DataTypes.Extras;


import Framework.DataTypes.IPEndPoint;

public interface DOIPContextResultConvey {
    void InitializeResultNotify(IPEndPoint ipEndPoint, int result);
    void UDSSendResultNotify(IPEndPoint ipEndPoint, int result);
    void LinkConnected(IPEndPoint ipEndPoint);
    void LinkDisconnected(IPEndPoint ipEndPoint);
}
