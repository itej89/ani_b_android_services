package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.DataTypes.Delegates.IArticulationConveyAIDL;
import FrameworkInterface.IArticulationAccessAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class ArticulationConveyServiceConnection implements ServiceConnection {

    IArticulationConveyAIDL service;

    public IArticulationConveyAIDL getService()
    {
        return  service;
    }

    public  ArticulationConveyServiceConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IArticulationConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

}