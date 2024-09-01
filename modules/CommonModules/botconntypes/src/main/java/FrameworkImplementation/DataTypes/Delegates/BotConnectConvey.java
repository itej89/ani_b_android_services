package FrameworkImplementation.DataTypes.Delegates;

import java.util.ArrayList;
import java.util.Map;

import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

public interface BotConnectConvey {

    public Boolean RunChoreogram(String audioFile, int StartSec, int EndSec, ArrayList<BeatsType> beat);
    public void  BotStream(Map.Entry<LINK_ANCHORS, String> Data);
    public void  LinkStreamStarted(LINK_ANCHORS anchor);

    public void  BotConnected();
    public void  BotDisconnected();

    public void  BotLowStorage();
    public void  BotError(BotConnectionInfo Error);

    public void  BrokerConenctionChanged(Boolean Status);
    public void  LinkDiscovered(LINK_ANCHORS anchor);

}
