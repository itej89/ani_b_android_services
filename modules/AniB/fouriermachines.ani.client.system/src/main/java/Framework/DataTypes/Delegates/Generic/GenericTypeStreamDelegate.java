package Framework.DataTypes.Delegates.Generic;

import java.util.Map;
import java.util.UUID;

import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public interface GenericTypeStreamDelegate {
    public UUID StreamDelegateID = UUID.randomUUID();
    public void notifyStream(Map.Entry<LINK_ANCHORS, String> LinkData);
}
