package FrameworkInterface.InterfaceImplementation.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineRequests;
import FrameworkInterface.PublicTypes.Delegates.IKineticsRemoteRequestConveyAIDL;

public class KineticsRemoteRequestConveyService extends Service {
    public KineticsRemoteRequestConveyService() {

    }


    @Override
    public IBinder onBind(Intent intent) {


        Map<String, String> strKineticsConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strKineticsConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.KineticsParameterUpdatesConveyService");

        try {
            KineticComms.Instance.ConnectToKineticServices(strKineticsConveyList);
        }
        catch (Exception e)
        {
            Log.e("KineticsRemoteRequestConveyService", e.getMessage());
        }



        return new IKineticsRemoteRequestConveyAIDL.Stub() {
           public  void  machineRequested(String Request)
           {
               KineticComms.Instance.RecievedRemoteCommand(MachineRequests.valueOf(Request));
           }
        };
    }
}
