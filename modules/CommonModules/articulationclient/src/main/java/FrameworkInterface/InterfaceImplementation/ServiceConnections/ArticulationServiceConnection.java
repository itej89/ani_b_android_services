package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.IArticulationAccessAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class ArticulationServiceConnection implements ServiceConnection {

    IArticulationAccessAIDL service;

    public IArticulationAccessAIDL getService()
    {
        return  service;
    }

    public Boolean IsConnectedToAi()
    {
        return service != null ? true : false;
    }

    public  void ArticulationServiceConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IArticulationAccessAIDL.Stub.asInterface((IBinder) boundService);

        Map<String, String> strAiConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strAiConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.ArticulationConveyService");
        try {
            ArticulationManager.Instance.ConnectToArticulationServices(strAiConveyList);
        }
        catch (Exception e)
        {

        }
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
        ArticulationManager.Instance.ArticulationServiceDisconnected();
    }

}