package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkInterface.PublicTypes.Delegates.IKineticsRemoteRequestConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsResponseConveyAIDL;

public class KineticsResponseConveyConnection implements ServiceConnection {

    IKineticsResponseConveyAIDL service;

    public IKineticsResponseConveyAIDL getService()
    {
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IKineticsResponseConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}