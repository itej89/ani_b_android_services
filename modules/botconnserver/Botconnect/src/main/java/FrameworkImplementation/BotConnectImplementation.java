package FrameworkImplementation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.BotConnectManager;
import Framework.DOIPLayer.DOIPStateMachines.DOIPTesterContext;
import Framework.DataTypes.EndPointInformation;
import Framework.DataTypes.FrameParsers.RxFrames.BIND;
import Framework.DataTypes.FrameParsers.RxFrames.REQUEST;
import Framework.DataTypes.FrameParsers.RxFrames.UNBIND;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.IBotConnectConveyAIDL;
import FrameworkImplementation.DataTypes.Delegates.ILinkTransportConveyAIDL;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkImplementation.ServiceConnections.BotConnectConveyConnection;
import FrameworkImplementation.ServiceConnections.LinkTransportConveyConnection;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

public class BotConnectImplementation extends BotConnectManager implements BotConnectAccess,
        BotConnectConvey, LinkTransportConvey {

    public static BotConnectAccess  Instance = new BotConnectImplementation();


    String Bot_Connect_Convey_ID = "";
    String Link_Transport_Convey_ID = "";

    public static final String BOT_CONNECT_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.BotConnectConveyService";
    public static final String LINK_TRANSPORT_CONVEY = "FrameworkInterface.InterfaceImplementation.Services.LinkTransportConveyService";
    public Map<String, Map<String, ServiceConnection>> BotConnectConveyConnections = new HashMap();

    public void ConnectToBotConnectServices(String connectionID, Map<String, String> BotConnectConveyList){

        for (Map.Entry<String,String> keypair: BotConnectConveyList.entrySet()) {
            Intent i = new Intent();
            i.setClassName(keypair.getKey(), keypair.getValue());

            if(BotConnectConveyConnections.containsKey(keypair.getValue())) {
                if (BotConnectConveyConnections.get(keypair.getValue()).containsKey(keypair.getKey())) {
                    try {
                        GlobalContext.context.unbindService(BotConnectConveyConnections.get(keypair.getValue()).remove(keypair.getKey()));
                    }
                    catch (Exception e)
                    {
                        Log.e("BotConnectImplementation", e.getMessage());
                    }
                    BotConnectConveyConnections.get(keypair.getValue()).remove(keypair.getKey());
                }
            }
            else
                BotConnectConveyConnections.put(keypair.getValue(), new HashMap<String, ServiceConnection>());

            switch (keypair.getValue()) {
                case BOT_CONNECT_CONVEY:
                    BotConnectConveyConnections.get(BOT_CONNECT_CONVEY).put(keypair.getKey(), new BotConnectConveyConnection());
                    break;
                case LINK_TRANSPORT_CONVEY:
                    BotConnectConveyConnections.get(LINK_TRANSPORT_CONVEY).put(keypair.getKey(), new LinkTransportConveyConnection());
                    break;
                default:
                    break;
            }

            ServiceConnection connection = BotConnectConveyConnections.get(keypair.getValue()).get(keypair.getKey());

            try {
                boolean ret = GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
            catch (Exception e)
            {
                Log.e("BotConnectImplementation", "ConnectToBotConnectServices : "+e.getMessage());
            }
        }
    }



    IBotConnectConveyAIDL notifyBotConnectConvey()
    {
        if(BotConnectConveyConnections.containsKey(BOT_CONNECT_CONVEY))
        {
            if(BotConnectConveyConnections.get(BOT_CONNECT_CONVEY).containsKey(Bot_Connect_Convey_ID))
            {
                ServiceConnection serviceConnection =   (BotConnectConveyConnections.get(BOT_CONNECT_CONVEY).get(Bot_Connect_Convey_ID));
                if(((BotConnectConveyConnection)serviceConnection).getService() != null) {
                    return ((BotConnectConveyConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }



    ILinkTransportConveyAIDL notifyLinkTransportConvey()
    {
        if(BotConnectConveyConnections.containsKey(BOT_CONNECT_CONVEY))
        {
            if(BotConnectConveyConnections.get(BOT_CONNECT_CONVEY).containsKey(Link_Transport_Convey_ID))
            {
                ServiceConnection serviceConnection =   (BotConnectConveyConnections.get(BOT_CONNECT_CONVEY).get(Link_Transport_Convey_ID));
                if(((LinkTransportConveyConnection)serviceConnection).getService() != null) {
                    return ((LinkTransportConveyConnection) serviceConnection).getService();
                }
            }
        }
        return  null;
    }

    // BotConnectAccess
    public  void BeginBOIPEngine()
    {
        if(!IsInitialized) {
            botDelegate = this;
            DOIPTesterContext.Instance.Initialize(this, this);
            IsInitialized = true;
        }
    }

    public void StopBOIOPEngine()
    {
        if(IsInitialized) {
            DOIPTesterContext.Instance.Uninitialize();
            IsInitialized = false;
        }
    }

    public void SubscribeToBotConnectConvey(String connectionID)
    {
        Bot_Connect_Convey_ID = connectionID;
    }
    //End of BotConnectAccess


    public void SendLinkRequest(String connectionID, LINK_ANCHORS anchor, String Data)
    {
        Link_Transport_Convey_ID = connectionID;
        for (Map.Entry<String, EndPointInformation> entry : EndPointData.entrySet())
        {
            if(entry.getValue().LinkData.containsKey(anchor))
            {
                Map<LINK_ANCHORS, String> RequestInformation = new HashMap<>();
                RequestInformation.put(anchor, Data);
                REQUEST txFrame = new REQUEST(ID, RequestInformation);
                txFrame.FRAME_ID = entry.getValue().LinkData.get(anchor).FrameID;

                DOIPTesterContext.Instance.SendData(entry.getValue().LinkData.get(anchor).LinkEndPoint, txFrame.Json().getBytes());
                break;
            }
        }
    }


    public void StartLinkStream(String connectionID, LINK_ANCHORS anchor)
    {
        Link_Transport_Convey_ID = connectionID;
        for (Map.Entry<String, EndPointInformation> entry : EndPointData.entrySet())
        {
            if(entry.getValue().LinkData.containsKey(anchor))
            {
                entry.getValue().LinkData.get(anchor).BindToLink(this);

                Map<LINK_ANCHORS, String> BindInformation = new HashMap<>();
                BindInformation.put(anchor, entry.getValue().LinkData.get(anchor).GetBindInformation());
                BIND txFrame = new BIND(ID, BindInformation);
                txFrame.FRAME_ID = entry.getValue().LinkData.get(anchor).FrameID;

                DOIPTesterContext.Instance.SendData(entry.getValue().LinkData.get(anchor).LinkEndPoint, txFrame.Json().getBytes());
                break;
            }
        }
    }

    public void StopLinkStream(String connectionID, LINK_ANCHORS anchor)
    {
        Link_Transport_Convey_ID = connectionID;
        for (Map.Entry<String, EndPointInformation> entry : EndPointData.entrySet())
        {
            if(entry.getValue().LinkData.containsKey(anchor))
            {
                entry.getValue().LinkData.get(anchor).UnBindToLink();

                Map<LINK_ANCHORS, String> BindInformation = new HashMap<>();
                BindInformation.put(anchor, entry.getValue().LinkData.get(anchor).GetBindInformation());
                UNBIND txFrame = new UNBIND(ID, BindInformation);
                txFrame.FRAME_ID = entry.getValue().LinkData.get(anchor).FrameID;

                DOIPTesterContext.Instance.SendData(entry.getValue().LinkData.get(anchor).LinkEndPoint, txFrame.Json().getBytes());
                break;
            }
        }
    }

    public  boolean IsLinkAvailable(String connectionID, LINK_ANCHORS AnchorType)
    {
        for(Map.Entry<String, EndPointInformation> entry : EndPointData.entrySet())
        {
            if(entry.getValue().LinkData.containsKey(AnchorType))
            {
                return true;
            }
        }
        return  false;
    }

    public  String GetLinkData(String connectionID, LINK_ANCHORS AnchorType)
    {
        String Data = "";

        for(Map.Entry<String, EndPointInformation> entry : EndPointData.entrySet())
        {
            if(entry.getValue().LinkData.containsKey(AnchorType))
            {
                return entry.getValue().LinkData.get(AnchorType).Data;
            }
        }

        return Data;
    }


    //BotConnectConvey
    public Boolean RunChoreogram(String audioFile, int StartSec, int EndSec, ArrayList<BeatsType> beat){
        try {
            if (notifyBotConnectConvey() != null)
                return notifyBotConnectConvey().RunChoreogram(audioFile, StartSec, EndSec, beat) == 0x01? true :false;
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
        return  false;
    }
    public void  BotStream(Map.Entry<LINK_ANCHORS, String> Data){
        HashMap<String, String> strData = new HashMap<>();
        strData.put(Data.getKey().toString(), Data.getValue());

        try {
            if (notifyBotConnectConvey() != null)
                 notifyBotConnectConvey().BotStream(strData);
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }
    public void  LinkStreamStarted(LINK_ANCHORS anchor){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().LinkStreamStarted(anchor.toString());
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }

    public void  BotConnected(){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().BotConnected();
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }
    public void  BotDisconnected(){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().BotDisconnected();
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }

    public void  BotLowStorage(){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().BotLowStorage();
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }
    public void  BotError(BotConnectionInfo Error){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().BotError(Error.toString());
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }

    public void  BrokerConenctionChanged(Boolean Status){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().BrokerConenctionChanged(Status ? (byte)0x01  : 0);
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }
    public void  LinkDiscovered(LINK_ANCHORS anchor){
        try {
            if (notifyBotConnectConvey() != null)
                notifyBotConnectConvey().LinkDiscovered(anchor.toString());
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }
    }
    //End of BotConnectConvey


    //LinkTransportConvey
    public void LinkDataReceived(LINK_ANCHORS Anchor, RecievedData Data){
        try {
            if (notifyLinkTransportConvey() != null)
                notifyLinkTransportConvey().LinkDataReceived(Anchor.toString(), Data);
        }catch (Exception e){
            Log.e("BotConnectImplementation", e.getMessage());
        }}
    //End of LinkTransportConvey

}
