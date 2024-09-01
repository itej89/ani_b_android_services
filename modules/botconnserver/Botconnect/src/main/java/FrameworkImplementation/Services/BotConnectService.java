package FrameworkImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkImplementation.BotConnectImplementation;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkImplementation.IBotConnectAccessAIDL;

public class BotConnectService extends Service {
    public BotConnectService() {
    }

    Boolean IsServiceReady = false;

    @Override
    public void onCreate(){
        IsServiceReady = false;
        GlobalContext.context = this;
        BotConnectImplementation.Instance.BeginBOIPEngine();
        IsServiceReady = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            return new IBotConnectAccessAIDL.Stub() {

                public byte IsServiceReady(String connectionID){
                    return  IsServiceReady? (byte)1 : 0;
                }

                public void SendLinkRequest(String connectionID, String anchor, String Data){
                    BotConnectImplementation.Instance.SendLinkRequest(connectionID, LINK_ANCHORS.valueOf(anchor), Data);
                }

                public void ConnectToBotConnectServices(String connectionID, Map BotConnectConveyList){
                    BotConnectImplementation.Instance.ConnectToBotConnectServices(connectionID, BotConnectConveyList);
                }

                public void SubscribeToBotConnectConvey(String connectionID){
                    BotConnectImplementation.Instance.SubscribeToBotConnectConvey(connectionID);
                }

                public void StartLinkStream(String connectionID, String anchor){
                    BotConnectImplementation.Instance.StartLinkStream(connectionID, LINK_ANCHORS.valueOf(anchor));
                }

                public void StopLinkStream(String connectionID, String anchor){
                    BotConnectImplementation.Instance.StopLinkStream(connectionID, LINK_ANCHORS.valueOf(anchor));
                }

                public byte IsLinkAvailable(String connectionID, String AnchorType){
                    return BotConnectImplementation.Instance.IsLinkAvailable(connectionID, LINK_ANCHORS.valueOf(AnchorType)) ? (byte)1 : 0;
                }

                public String GetLinkData(String connectionID, String Anchor){
                  return   BotConnectImplementation.Instance.GetLinkData(connectionID, LINK_ANCHORS.valueOf(Anchor));
                }

            };

        }
}
