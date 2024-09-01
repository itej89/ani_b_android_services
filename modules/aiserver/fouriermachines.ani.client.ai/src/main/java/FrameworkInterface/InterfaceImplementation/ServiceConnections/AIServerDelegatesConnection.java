package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import Framework.DataTypes.Delegates.AIServerDelegates;
import Framework.DataTypes.Delegates.IAIServerDelegatesAIDL;
import Framework.DataTypes.ServiceConnectionEstablishedConvey;

public class AIServerDelegatesConnection implements ServiceConnection {


    IAIServerDelegatesAIDL service;

    public IAIServerDelegatesAIDL getService()
    {
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IAIServerDelegatesAIDL.Stub.asInterface((IBinder) boundService);
 }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}
