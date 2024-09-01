package fm.ani.fmlauncher;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import Framework.DataTypes.GlobalContext;
import fm.ani.fmlauncher.DataTypes.LauncherServiceConnection;
import fm.ani.fmlaunchertypes.ILauncherServiceAIDL;

public class fmLauncherApp extends Application implements Application.ActivityLifecycleCallbacks {


        @Override
        public void onCreate() {
            super.onCreate();

            // Register to be notified of activity state changes
            try {
                registerActivityLifecycleCallbacks(this);
            }
            catch (Exception e){}

        }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
        public void onActivityResumed(Activity activity) {

        }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
        public void onActivityStopped(Activity activity) {
        }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}

