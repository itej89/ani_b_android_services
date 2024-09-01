package FrameworkInterface.InterfaceImplementation.ServiceConnections;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.InterfaceImplementation.Services.KineticsParameterUpdatesConveyService;
import FrameworkInterface.InterfaceImplementation.Services.KineticsRemoteRequestConveyService;
import FrameworkInterface.InterfaceImplementation.Services.KineticsResponseConveyService;

public class KineticsServiceConnection implements ServiceConnection {

    IKineticsAccessAIDL service;


   public IKineticsAccessAIDL getService()
   {
       return  service;
   }

    public Boolean IsConnectedToMachine()
    {
        return service != null ? true : false;
    }


    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IKineticsAccessAIDL.Stub.asInterface((IBinder) boundService);

        Map<String, String> strKineticsConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strKineticsConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.KineticsResponseConveyService");

        try {
            KineticComms.Instance.ConnectToKineticServices(strKineticsConveyList);
        }
        catch (Exception e)
        {
            Log.e("KineticsServiceConnection", e.getMessage());
        }

//        Intent iKineticsResponseConvey = new Intent(GlobalContext.context, KineticsResponseConveyService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            GlobalContext.context.startForegroundService(iKineticsResponseConvey);
//        } else {
//            GlobalContext.context.startService(iKineticsResponseConvey);
//        }
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
        KineticComms.Instance.KineticsServiceDisconnected();
    }

}