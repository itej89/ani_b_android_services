package FrameworkInterface.InterfaceImplementation.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.IKineticsParameterUpdatesConveyAIDL;

public class KineticsParameterUpdatesConveyService extends Service {
    public KineticsParameterUpdatesConveyService() {

    }


    @Override
    public IBinder onBind(Intent intent) {


        Map<String, String> strKineticsConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strKineticsConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.KineticsBindStatusConveyService");

        try {
            KineticComms.Instance.ConnectToKineticServices(strKineticsConveyList);
        }
        catch (Exception e)
        {
            Log.e("KineticsParameterUpdatesConveyService", e.getMessage());
        }

        return new IKineticsParameterUpdatesConveyAIDL.Stub() {
           public void  ParameterUpdated(KineticsRequest request)
           {
               KineticComms.Instance.RequestSent(request);
           }
            public void  ParametersSetSuccessfully()
            {
                KineticComms.Instance.ParameterTriggerSuccuss();
            }
        };
    }
}
