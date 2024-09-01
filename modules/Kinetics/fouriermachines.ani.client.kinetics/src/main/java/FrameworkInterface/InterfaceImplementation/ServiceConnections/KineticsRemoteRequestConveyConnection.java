package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import FrameworkInterface.PublicTypes.Delegates.IKineticsParameterUpdatesConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsRemoteRequestConveyAIDL;

public class KineticsRemoteRequestConveyConnection implements ServiceConnection {

    IKineticsRemoteRequestConveyAIDL service;

    public IKineticsRemoteRequestConveyAIDL getService()
    {
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IKineticsRemoteRequestConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}
