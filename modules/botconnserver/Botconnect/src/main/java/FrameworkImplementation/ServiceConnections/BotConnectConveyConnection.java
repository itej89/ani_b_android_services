package FrameworkImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkImplementation.DataTypes.Delegates.IBotConnectConveyAIDL;

public class BotConnectConveyConnection implements ServiceConnection {

    IBotConnectConveyAIDL service;

    public IBotConnectConveyAIDL getService()
    {
        return  service;
    }

    public  BotConnectConveyConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IBotConnectConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

}
