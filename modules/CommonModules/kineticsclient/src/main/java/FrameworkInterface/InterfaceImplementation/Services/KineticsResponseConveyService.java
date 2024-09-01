package FrameworkInterface.InterfaceImplementation.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.IKineticsResponseConveyAIDL;
import fouriermachines.ani.client.kineticsclient.BuildConfig;

public class KineticsResponseConveyService extends Service {

    public KineticsResponseConveyService() {

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {

        Map<String, String> strKineticsConveyList = new HashMap<>();
        String Package = GlobalContext.context.getPackageName();
        strKineticsConveyList.put(Package,
                "FrameworkInterface.InterfaceImplementation.Services.KineticsRemoteRequestConveyService");

        try {
            KineticComms.Instance.ConnectToKineticServices(strKineticsConveyList);
        }
        catch (Exception e)
        {
            Log.e("KineticsResponseConveyService", e.getMessage());
        }

        return new IKineticsResponseConveyAIDL.Stub() {
            public void  CommsLost()
            {

            }

            public void  MachiResponseTimeout(List<KineticsResponse> partialResponse, ParcelUuid _Acknowledgement)
            {
                ArrayList<KineticsResponse> arrList = new ArrayList<KineticsResponse>(partialResponse.size());
                arrList.addAll(partialResponse);
                KineticComms.Instance.KineticsResponseDataTimeout(_Acknowledgement.getUuid(), arrList);
            }

            public void  MachineResponseRecieved(List<KineticsResponse> responeData, ParcelUuid _Acknowledgement)
            {
                ArrayList<KineticsResponse> arrList = new ArrayList<KineticsResponse>(responeData.size());
                arrList.addAll(responeData);
                KineticComms.Instance.RecievedResponseData(arrList, _Acknowledgement.getUuid());
            }
        };
    }
}
