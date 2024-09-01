package FrameworkImplementation;

import java.util.Map;

import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public interface BotConnectAccess extends IBotConnectAccess {

    public void SendLinkRequest(String connectionID, LINK_ANCHORS anchor, String Data);

    public void SubscribeToBotConnectConvey(String connectionID);

    public void StartLinkStream(String connectionID,LINK_ANCHORS anchor);

    public void StopLinkStream(String connectionID,LINK_ANCHORS anchor);

    public boolean IsLinkAvailable(String connectionID,LINK_ANCHORS AnchorType);

    public String GetLinkData(String connectionID,LINK_ANCHORS Anchor);

    public void BeginBOIPEngine();

    public void StopBOIOPEngine();

}