package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkInterface.DataTypes.Delegates.IArticulationConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.IPlayerConveyAIDL;

public class PlayerConveyServiceConnection implements ServiceConnection {

    IPlayerConveyAIDL service;

    public IPlayerConveyAIDL getService()
    {
        return  service;
    }


    public  PlayerConveyServiceConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IPlayerConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }


}
