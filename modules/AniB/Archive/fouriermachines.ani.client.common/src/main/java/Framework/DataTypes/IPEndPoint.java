package Framework.DataTypes;

public class IPEndPoint {

    public  IPEndPoint(){}
    public  IPEndPoint(String _IPAddress, Integer _Port)
    {
        IPAddress = _IPAddress;
        Port = _Port;
    }

    public  String IPAddress;
    public  Integer Port;
}

