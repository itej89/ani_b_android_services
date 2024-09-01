package fm.ani.fmlauncher.DataTypes;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import fm.ani.fmlaunchertypes.ILauncherServiceAIDL;

public class LauncherServiceConnection implements ServiceConnection {

    ILauncherServiceAIDL service;

    public ILauncherServiceAIDL getService() {
        return  service;
    }


    public void onServiceConnected(ComponentName name, IBinder boundService) {
        Log.e("fmLauncher", "Launcher ServiceConnected");
        service = ILauncherServiceAIDL.Stub.asInterface((IBinder) boundService);
    }

    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

}
