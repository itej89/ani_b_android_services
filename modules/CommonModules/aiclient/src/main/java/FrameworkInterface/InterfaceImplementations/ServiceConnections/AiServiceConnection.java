package FrameworkInterface.InterfaceImplementations.ServiceConnections;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Delegates.IAIServerDelegatesAIDL;
import Framework.DataTypes.GlobalContext;
import FrameworkInterface.IAIAccessAIDL;
import FrameworkInterface.InterfaceImplementations.AIManager;
import FrameworkInterface.InterfaceImplementations.Services.AIServerConveyService;

public class AiServiceConnection implements ServiceConnection {

    IAIAccessAIDL service;




    public IAIAccessAIDL getService()
    {
        return  service;
    }

    public Boolean IsConnectedToAi()
    {
        return service != null ? true : false;
    }

    public  void AiServiceConnected(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IAIAccessAIDL.Stub.asInterface((IBinder) boundService);

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementations.Services.AIServerConveyService");
        try {
            AIManager.Instance.ConnectToAiServices(strAiConveyList);
        }
        catch (Exception e)
        {

        }

    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
        AIManager.Instance.AiServiceDisconnected();
    }

}