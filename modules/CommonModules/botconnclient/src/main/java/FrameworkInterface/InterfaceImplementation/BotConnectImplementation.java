package FrameworkInterface.InterfaceImplementation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkInterface.BotConnectAccess;
import FrameworkInterface.DataTypes.Delegates.BotConnServerStatusConvey;
import FrameworkInterface.InterfaceImplementation.ServiceConnections.BotConnectServiceConnection;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

public class BotConnectImplementation implements BotConnectAccess {

    public static BotConnectAccess Instance = new BotConnectImplementation();

    BotConnectServiceConnection connection;
    BotConnectConvey notify_delegate;
    BotConnServerStatusConvey notify_BotConnServerStatusConvey;

    public void SubscribeToBotConnectConvey(BotConnectConvey delegate){
        notify_delegate = delegate;
        try {
            connection.getService().SubscribeToBotConnectConvey(GlobalContext.context.getPackageName());
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "ConnectToBotConnectServices  :"+e.getMessage());
        }
    }
    public void DettachFromBotConnectEvents(){
        notify_delegate = null;
    }


    public void SubscribeToBotConnServerStatus(BotConnServerStatusConvey delegate){
        notify_BotConnServerStatusConvey = delegate;
    }
    public Boolean  InitServiceConnection() {
        connection = new BotConnectServiceConnection();
        Intent i = new Intent();
        i.setClassName("fm.ani.botconnserver", "FrameworkImplementation.Services.BotConnectService");
        return GlobalContext.context.bindService(i, connection, Context.BIND_AUTO_CREATE);
    }
    public Boolean  IsServiceReady(){
        try {
           return connection.getService().IsServiceReady(GlobalContext.context.getPackageName()) == 0x01 ? true : false;
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "IsServiceReady  :"+e.getMessage());
        }

        return  false;
    }
    public void ConnectToBotConnectServices(String connectionID, Map<String, String> BotConnectConveyList){
        try {
            connection.getService().ConnectToBotConnectServices(GlobalContext.context.getPackageName(),BotConnectConveyList);
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "ConnectToBotConnectServices  :"+e.getMessage());
        }
    }
    public void BotConnServiceConnected(){
        if(notify_BotConnServerStatusConvey != null)
            notify_BotConnServerStatusConvey.BotConnServiceConnected();
    }
    public void BotConnServiceDisconnected(){
        if(notify_BotConnServerStatusConvey != null)
            notify_BotConnServerStatusConvey.BotConnServiceDisconnected();
    }


    HashMap<LINK_ANCHORS, LinkTransportConvey> StreamLinks = new HashMap<>();
    public void StartLinkStream(LINK_ANCHORS anchor, LinkTransportConvey Convey){
        if(StreamLinks.containsKey(anchor)) StreamLinks.remove(anchor);
        StreamLinks.put(anchor, Convey);
        try {
            connection.getService().StartLinkStream(GlobalContext.context.getPackageName(),anchor.toString());
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "StartLinkStream  :"+e.getMessage());
        }
    }
    public void StopLinkStream(LINK_ANCHORS anchor){
        try {
            connection.getService().StopLinkStream(GlobalContext.context.getPackageName(),anchor.toString());
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "StopLinkStream  :"+e.getMessage());
        }
    }


    public  boolean IsLinkAvailable(LINK_ANCHORS AnchorType){

        try {
       return connection.getService().IsLinkAvailable(GlobalContext.context.getPackageName(),AnchorType.toString()) == 1? true : false;
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "IsLinkAvailable  :"+e.getMessage());
        }
        return  false;
    }

    public String GetLinkData(LINK_ANCHORS Anchor) {
        try {
            return connection.getService().GetLinkData(GlobalContext.context.getPackageName(),Anchor.toString());
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "GetLinkData  :"+e.getMessage());
        }
        return  "";
    }

    //BotConnectConvey
    public Boolean RunChoreogram(String audioFile, int StartSec, int EndSec, ArrayList<BeatsType> beat){
        if(notify_delegate != null)
        return notify_delegate.RunChoreogram(audioFile, StartSec, EndSec, beat);

        return  false;
    }
    public void  BotStream(Map.Entry<LINK_ANCHORS, String> Data){
        if(notify_delegate != null)
            notify_delegate.BotStream(Data);
    }


    public void SendLinkRequest(LINK_ANCHORS anchor, String Data)
    {
        try {
            connection.getService().SendLinkRequest(GlobalContext.context.getPackageName(),
                    anchor.toString(), Data);
        }
        catch (Exception e)
        {
            Log.e("BotConnectImplementation", "SendLinkRequest  :"+e.getMessage());
        }
    }

    public void  LinkStreamStarted(LINK_ANCHORS anchor){
        if(notify_delegate != null)
            notify_delegate.LinkStreamStarted(anchor);
    }

    public void  BotConnected(){
        if(notify_delegate != null)
            notify_delegate.BotConnected();
    }
    public void  BotDisconnected(){
        if(notify_delegate != null)
            notify_delegate.BotDisconnected();
    }

    public void  BotLowStorage(){
        if(notify_delegate != null)
            notify_delegate.BotLowStorage();
    }
    public void  BotError(BotConnectionInfo Error){
        if(notify_delegate != null)
            notify_delegate.BotError(Error);
    }

    public void  BrokerConenctionChanged(Boolean Status){
        if(notify_delegate != null)
            notify_delegate.BrokerConenctionChanged(Status);
    }
    public void  LinkDiscovered(LINK_ANCHORS anchor){
        if(notify_delegate != null)
            notify_delegate.LinkDiscovered(anchor);
    }
    //End of BotConnectConvey

    //LinkTransportConvey
    public void LinkDataReceived(LINK_ANCHORS Anchor, RecievedData Data){
        if(StreamLinks.containsKey(Anchor) && StreamLinks.get(Anchor) != null)
            StreamLinks.get(Anchor).LinkDataReceived(Anchor, Data);
    }
    //End of LinkTransportConvey

}