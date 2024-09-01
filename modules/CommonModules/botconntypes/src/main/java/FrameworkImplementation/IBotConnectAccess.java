package FrameworkImplementation;

import java.util.Map;

import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public interface IBotConnectAccess {

    public void ConnectToBotConnectServices(String connectionID, Map<String, String> BotConnectConveyList);

}
