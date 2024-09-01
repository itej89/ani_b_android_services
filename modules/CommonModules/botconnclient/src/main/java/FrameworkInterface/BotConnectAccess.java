package FrameworkInterface;
import java.util.ArrayList;
import java.util.Map;

import Framework.DataTypes.Transports.Helpers.RecievedData;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkImplementation.IBotConnectAccess;
import FrameworkInterface.DataTypes.Delegates.BotConnServerStatusConvey;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;
public interface BotConnectAccess extends IBotConnectAccess {
    public void SubscribeToBotConnServerStatus(BotConnServerStatusConvey delegate);
    public Boolean  IsServiceReady();
    public Boolean  InitServiceConnection();
    public void BotConnServiceConnected();
    public void BotConnServiceDisconnected();

    public void SubscribeToBotConnectConvey(BotConnectConvey delegate);
    public void DettachFromBotConnectEvents();


    public void SendLinkRequest(LINK_ANCHORS anchor, String Data);

    public void StartLinkStream(LINK_ANCHORS anchor, LinkTransportConvey Convey);
    public void StopLinkStream(LINK_ANCHORS anchor);

    public  boolean IsLinkAvailable(LINK_ANCHORS AnchorType);
    public String GetLinkData(LINK_ANCHORS Anchor);




    //BotConnectConvey
    public Boolean RunChoreogram(String audioFile, int StartSec, int EndSec, ArrayList<BeatsType> beat);
    public void  BotStream(Map.Entry<LINK_ANCHORS, String> Data);
    public void  LinkStreamStarted(LINK_ANCHORS anchor);

    public void  BotConnected();
    public void  BotDisconnected();

    public void  BotLowStorage();
    public void  BotError(BotConnectionInfo Error);

    public void  BrokerConenctionChanged(Boolean Status);
    public void  LinkDiscovered(LINK_ANCHORS anchor);
    //End of BotConnectConvey

    //LinkTransportConvey
    public void LinkDataReceived(LINK_ANCHORS Anchor, RecievedData Data);
    //End of LinkTransportConvey
}