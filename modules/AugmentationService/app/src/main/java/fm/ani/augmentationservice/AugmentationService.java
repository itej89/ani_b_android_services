package fm.ani.augmentationservice;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;


public class AugmentationService extends Service {
    public AugmentationService() {
        Log.i("augmentationservice Service", "Autostart constructed");
    }

    Boolean IsServiceReady = false;

    Context _context;

    @Override
    public void onCreate(){
        IsServiceReady = false;
        _context = this;

        Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("SmartTracker Running")
                .setAutoCancel(true);
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);

        Log.i("augmentationservice Service", "onCreate");
        DO_CHECK();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean RequestUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) _context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), _context.getPackageName());
        boolean granted;
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (_context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        if(!granted)
        {
            System.out.println("PERMISSION NOK : Usage stats");

            _context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        else {
            DO_CHECK();
            return true;
        }

        return  false;
    }

    private   enum PERMISSION_STAGES{USAGE_STATS, OK}
    private   PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.USAGE_STATS;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case USAGE_STATS: {

                PERMISSION_STAGE = PERMISSION_STAGES.OK;
                RequestUsageStatsPermission();
                break;
            }
            case OK:
            {
                IsServiceReady = true;
                break;
            }
        }
    }
}