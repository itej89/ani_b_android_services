package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.IBotConnectConveyAIDL;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

public class BotConnectConveyService extends Service {
    public BotConnectConveyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.LinkTransportConveyService");
        try {
            BotConnectImplementation.Instance.ConnectToBotConnectServices(GlobalContext.context.getPackageName(), strAiConveyList);
        }
        catch (Exception e)
        {}

        return new IBotConnectConveyAIDL.Stub() {
            public byte RunChoreogram(String audioFile, int StartSec, int EndSec, List<BeatsType> beat){
                return BotConnectImplementation.Instance.RunChoreogram(audioFile,
                        StartSec, EndSec, new ArrayList<BeatsType>(beat))? (byte)0x01 : 0;
            }
            public void  BotStream(Map Data){
                HashMap<String, String> mapData = (HashMap<String, String>)Data;

                HashMap<LINK_ANCHORS, String> anchorData = new HashMap<LINK_ANCHORS, String>();
                for(Map.Entry<String, String> entry : mapData.entrySet()){
                    anchorData.put(LINK_ANCHORS.valueOf(entry.getKey()), entry.getValue());
                }

                for(Map.Entry<LINK_ANCHORS, String> entry : anchorData.entrySet()) {
                    BotConnectImplementation.Instance.BotStream(entry);
                }
            }
            public   void  LinkStreamStarted(String anchor){
                BotConnectImplementation.Instance.LinkStreamStarted(LINK_ANCHORS.valueOf(anchor));
            }

            public   void  BotConnected(){
                BotConnectImplementation.Instance.BotConnected();
            }
            public  void  BotDisconnected(){
                BotConnectImplementation.Instance.BotDisconnected();
            }

            public  void  BotLowStorage(){
                BotConnectImplementation.Instance.BotLowStorage();
            }

            public  void  BotError(String Error){
                BotConnectImplementation.Instance.BotError(BotConnectionInfo.valueOf(Error));
            }

            public  void  BrokerConenctionChanged(byte Status){
                BotConnectImplementation.Instance.BrokerConenctionChanged(Status==1?true :false);
            }
            public  void  LinkDiscovered(String anchor){
                BotConnectImplementation.Instance.LinkDiscovered(LINK_ANCHORS.valueOf(anchor));
            }
        };

    }
}
