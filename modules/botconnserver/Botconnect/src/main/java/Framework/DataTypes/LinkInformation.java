package Framework.DataTypes;

import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.Delegates.TCPSocketClientConvey;
import Framework.DataTypes.Enums.BIND_TYPES;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import Framework.DataTypes.Transports.TCPSocket;
import Framework.HidConncetUtil;

public  class LinkInformation implements TCPSocketClientConvey
{


    HidConncetUtil util;

    public BIND_TYPES BINDType = BIND_TYPES.SERVER;
    public  LINK_ANCHORS LinkAnchor = LINK_ANCHORS.NA;
    public IPEndPoint LinkEndPoint;
    public String FrameID = "";
    public String Data;

    TCPSocket Socket;
    LinkTransportConvey LinkConvey;

    public void EvaluateBindType(BIND_TYPES BindType)
    {

        switch (BindType)
        {
            case BTHID: {
                util = new HidConncetUtil(GlobalContext.context);
                util.connect(util.getController(Data));
                break;
            }
            case SERVER:
                break;
            case TCP_SOCKET_CLIENT: {
                Socket = new TCPSocket(this);
                Socket.StartServer();
                break;
            }
            case NA:
                break;
        }
    }

    public LinkInformation(LINK_ANCHORS _LinkAnchor, BIND_TYPES BindType , String _Data)
    {
        try
        {
            LinkAnchor = _LinkAnchor;
            BINDType = BindType;
            Data = _Data;

            final BIND_TYPES BindRequestType = BindType;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    EvaluateBindType(BindRequestType);

                }
            });
            thread.start();

        }
        catch (Exception e) {

        }
        Data = _Data;
    }

    public void LinkExpired()
    {
        if(Socket != null)
        {
            Socket.StopServer();
        }
    }

    public String GetBindInformation()
    {
        switch (BINDType)
        {
            case NA:
                break;
            case TCP_SOCKET_CLIENT:
                if(Socket != null)
                {
                    return  String.valueOf(Socket.GetPort());
                }
                break;
        }

        return "";
    }

    public void BindToLink(LinkTransportConvey _LinkConvey)
    {
        LinkConvey = _LinkConvey;
    }

    public void UnBindToLink()
    {
        LinkConvey = null;
    }


    //Start of TCPSocketClientConveßy
    public void TCP_DataRecieved(RecievedData recievedData){
        if(LinkConvey != null)
        {
            LinkConvey.LinkDataReceived(LinkAnchor, recievedData);
        }
    }
    public void TCP_Disconnected(IPEndPoint endPoint){}
    public void TCP_Timeout(IPEndPoint endPoint, Integer code){}
    //End o TCPSocketClientConveßy

}
