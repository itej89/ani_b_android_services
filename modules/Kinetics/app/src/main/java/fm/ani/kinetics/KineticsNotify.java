package fm.ani.kinetics;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.List;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.IKineticsAccessAIDL;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsRemoteRequestConvey;

public class KineticsNotify {


    public KineticsNotify() {
    }
}
