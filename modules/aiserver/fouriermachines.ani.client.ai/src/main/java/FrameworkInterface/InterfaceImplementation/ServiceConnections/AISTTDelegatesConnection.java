package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import Framework.DataTypes.Delegates.IAISTTDelegatesAIDL;
import Framework.DataTypes.Delegates.IAIServerDelegatesAIDL;
import Framework.DataTypes.ServiceConnectionEstablishedConvey;

public class AISTTDelegatesConnection implements ServiceConnection {
    ServiceConnectionEstablishedConvey notify_ServiceConnectionEstablishedConvey;

    public AISTTDelegatesConnection(ServiceConnectionEstablishedConvey delegate)
    {
        super();
        notify_ServiceConnectionEstablishedConvey = delegate;
    }

    IAISTTDelegatesAIDL service;

    public IAISTTDelegatesAIDL getService(){
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IAISTTDelegatesAIDL.Stub.asInterface((IBinder) boundService);

        if(notify_ServiceConnectionEstablishedConvey != null)
            notify_ServiceConnectionEstablishedConvey.ServiceConnectionEstablished();
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}


