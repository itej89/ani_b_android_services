package Framework.SystemEventHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.Delegates.Generic.GenericTypeStreamDelegate;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;

public class LinkAnchorStreamHandler {

    public  static  LinkAnchorStreamHandler instance = new LinkAnchorStreamHandler();

    HashMap<LINK_ANCHORS, ArrayList<GenericTypeStreamDelegate>> LinkStreamSubscribers = new HashMap<>();

    public  void SubscribeToStream(LINK_ANCHORS AnchorType, GenericTypeStreamDelegate delegate)
    {
        if(LinkStreamSubscribers.containsKey(AnchorType))
        {
            LinkStreamSubscribers.get(AnchorType).add(delegate);
        }
        else
        {
            LinkStreamSubscribers.put(AnchorType, new ArrayList<GenericTypeStreamDelegate>());
            LinkStreamSubscribers.get(AnchorType).add(delegate);
        }
    }

    public void UnSubscribeStream(LINK_ANCHORS Anchor ,UUID ID)
    {

        if(LinkStreamSubscribers.containsKey(Anchor) && LinkStreamSubscribers.get(Anchor) != null)
        {
            for(GenericTypeStreamDelegate delegate : LinkStreamSubscribers.get(Anchor))
            {
                if(delegate.StreamDelegateID.equals(ID))
                {
                    LinkStreamSubscribers.get(Anchor).remove(delegate);

                    if(LinkStreamSubscribers.get(Anchor).isEmpty())
                    {
                        LinkStreamSubscribers.remove(Anchor);
                    }
                    break;
                }
            }
        }

    }

    public  void  StreamData(Map.Entry<LINK_ANCHORS, String> Data)
    {
        if(LinkStreamSubscribers.containsKey(Data.getKey()) && LinkStreamSubscribers.get(Data.getKey()) != null) {
            for(GenericTypeStreamDelegate delegate : LinkStreamSubscribers.get(Data.getKey())) {
                if(delegate != null)
                    delegate.notifyStream(Data);
            }
        }
    }

}
