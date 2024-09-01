package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.IKineticsBindStatusConveyAIDL;

public class KineticsBindStatusConveyService extends Service {
    public KineticsBindStatusConveyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {


        KineticComms.Instance.KineticsServiceConnected();

        return new IKineticsBindStatusConveyAIDL.Stub() {
           public void  KineticsBindStatus(String state)
           {
                KineticComms.Instance.SetBindingStatus(state);
           }
        };
    }
}
