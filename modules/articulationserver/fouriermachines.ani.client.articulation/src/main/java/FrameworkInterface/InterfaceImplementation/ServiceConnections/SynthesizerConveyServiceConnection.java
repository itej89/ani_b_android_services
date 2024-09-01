package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkInterface.DataTypes.Delegates.ISynthesizerConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.ServiceConnectionEstablishedConvey;

public class SynthesizerConveyServiceConnection implements ServiceConnection {

    ServiceConnectionEstablishedConvey notify_ServiceConnectionEstablishedConvey;

    ISynthesizerConveyAIDL service;

    public ISynthesizerConveyAIDL getService()
    {
        return  service;
    }


    public  SynthesizerConveyServiceConnection(ServiceConnectionEstablishedConvey delegate){
        notify_ServiceConnectionEstablishedConvey = delegate;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = ISynthesizerConveyAIDL.Stub.asInterface((IBinder) boundService);

        if(notify_ServiceConnectionEstablishedConvey != null)
            notify_ServiceConnectionEstablishedConvey.ServiceConnectionEstablished();
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }


}
