package FrameworkInterface.Delegates;

import FrameworkInterface.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import FrameworkInterface.Enums.ANSTMSG;

public interface MqttInterfaceConvey {
    public void ConnectedToBroker(Boolean Status);
    public void  SubscribedToChannel(String Channel, Boolean Status);
    public void  UnsubscribedFromChannel(String Channel, Boolean Status);
    public void  FrameSent(String Frame_ID, Boolean Status);
    public void  FrameRecieved(ANSTMSG FrameType, String Frame);
    public void  DisconnectedFromBroker(Boolean Status);
}
