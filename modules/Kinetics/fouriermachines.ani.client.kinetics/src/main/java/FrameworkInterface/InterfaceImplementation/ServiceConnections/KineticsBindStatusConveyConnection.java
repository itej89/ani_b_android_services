package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import Framework.DataTypes.Delegates.ServiceConnectionEstablishedConvey;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.IKineticsBindStatusConveyAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsParameterUpdatesConveyAIDL;

public class KineticsBindStatusConveyConnection implements ServiceConnection {

    ServiceConnectionEstablishedConvey notify_ServiceConnectionEstablishedConvey;
    public KineticsBindStatusConveyConnection(ServiceConnectionEstablishedConvey delegate)
    {
        super();
        notify_ServiceConnectionEstablishedConvey = delegate;
    }
    IKineticsBindStatusConveyAIDL service;

    public IKineticsBindStatusConveyAIDL getService()
    {
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IKineticsBindStatusConveyAIDL.Stub.asInterface((IBinder) boundService);

        if(notify_ServiceConnectionEstablishedConvey != null)
            notify_ServiceConnectionEstablishedConvey.ServiceConnectionEstablished();
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}
