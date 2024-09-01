package Framework.DataTypes;

import java.util.HashMap;
import java.util.Map;

import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public class EndPointInformation
{
    public IPEndPoint Enpoint;
    public Boolean IsBotConnected = false;



    public EndPointInformation(IPEndPoint _Enpoint)
    {
        Enpoint = _Enpoint;
    }

    public Map<LINK_ANCHORS, LinkInformation> LinkData = new HashMap<>();
}