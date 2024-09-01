package FrameworkImplementation.DataTypes.Delegates;

import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.Transports.Helpers.RecievedData;

public interface LinkTransportConvey {
    public void LinkDataReceived(LINK_ANCHORS Anchor, RecievedData Data);
}
