package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkImplementation.IBotConnectAccessAIDL;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;

public class BotConnectServiceConnection implements ServiceConnection {
    IBotConnectAccessAIDL service;

    public IBotConnectAccessAIDL getService()
    {
        return  service;
    }

    public  BotConnectServiceConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IBotConnectAccessAIDL.Stub.asInterface((IBinder) boundService);

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.BotConnectConveyService");
        try {
            BotConnectImplementation.Instance.ConnectToBotConnectServices(GlobalContext.context.getPackageName(), strAiConveyList);
        }
        catch (Exception e)
        {}
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
        BotConnectImplementation.Instance.BotConnServiceDisconnected();
    }
}
