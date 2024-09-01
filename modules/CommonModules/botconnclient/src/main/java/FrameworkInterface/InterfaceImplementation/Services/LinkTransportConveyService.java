package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import Framework.DataTypes.Transports.Helpers.RecievedData;
import FrameworkImplementation.DataTypes.Delegates.ILinkTransportConveyAIDL;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;

public class LinkTransportConveyService extends Service {
    public LinkTransportConveyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        BotConnectImplementation.Instance.BotConnServiceConnected();

        return new ILinkTransportConveyAIDL.Stub() {
           public void LinkDataReceived(String Anchor, RecievedData Data){
               BotConnectImplementation.Instance.LinkDataReceived(LINK_ANCHORS.valueOf(Anchor), Data);
            }
        };
    }
}
