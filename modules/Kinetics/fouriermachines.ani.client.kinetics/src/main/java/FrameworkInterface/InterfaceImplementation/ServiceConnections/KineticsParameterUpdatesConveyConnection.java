package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.PublicTypes.Delegates.IKineticsParameterUpdatesConveyAIDL;

public class KineticsParameterUpdatesConveyConnection implements ServiceConnection {

    IKineticsParameterUpdatesConveyAIDL service;

    public IKineticsParameterUpdatesConveyAIDL getService() {
        return  service;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IKineticsParameterUpdatesConveyAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}
