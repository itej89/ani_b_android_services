// IBotConnectAccessAIDL.aidl
package FrameworkImplementation.DataTypes.Delegates;

// Declare any non-default types here with import statements

import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

interface IBotConnectConveyAIDL {
    byte RunChoreogram(String audioFile, int StartSec, int EndSec, in List<BeatsType> beat);
    void  BotStream(in Map Data);
    void  LinkStreamStarted(String anchor);

    void  BotConnected();
    void  BotDisconnected();

    void  BotLowStorage();
    void  BotError(String Error);

    void  BrokerConenctionChanged(byte Status);
    void  LinkDiscovered(String anchor);
}
