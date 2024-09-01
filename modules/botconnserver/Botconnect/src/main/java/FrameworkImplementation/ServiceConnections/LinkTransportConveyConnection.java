package FrameworkImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkImplementation.DataTypes.Delegates.IBotConnectConveyAIDL;
import FrameworkImplementation.DataTypes.Delegates.ILinkTransportConveyAIDL;

public class LinkTransportConveyConnection implements ServiceConnection {

    ILinkTransportConveyAIDL service;

    public ILinkTransportConveyAIDL getService()
    {
        return  service;
    }

    public  LinkTransportConveyConnection(){}

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = ILinkTransportConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

}
