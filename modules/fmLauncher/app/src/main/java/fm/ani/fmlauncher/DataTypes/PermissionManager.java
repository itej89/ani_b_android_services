package fm.ani.fmlauncher.DataTypes;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.lang.reflect.Method;

import androidx.core.app.ActivityCompat;
import fm.ani.fmlauncher.DataTypes.Delegates.PermissionResultConvey;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class PermissionManager {

    private Context context = null;

    public PermissionManager(Context _Context)
    {
        context = _Context;
    }

    private final int WRITE_REQUEST_CODE = 300;

    //Permisison requireed for TouchEmulation
    private boolean RequestUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        boolean granted;
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        if(!granted)
        {
            System.out.println("PERMISSION NOK : Usage stats");

            context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        else {
            DO_CHECK();
            return true;
        }

        return  false;
    }


    public Boolean RequestStoragePermission() {
        PermissionEverywhere.getPermission(context,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_REQUEST_CODE,
                "Notification title",
                "This app needs write permission", android.R.mipmap.sym_def_app_icon)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        if(permissionResponse.getRequestCode() == WRITE_REQUEST_CODE)
                            DO_CHECK();
                    }
                });

        return  true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {


            case WRITE_REQUEST_CODE: {
                DO_CHECK();
                break;
            }
        }
    }


    private   enum PERMISSION_STAGES{STORAGE, USAGE_STATS, OK}
    private   PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.USAGE_STATS;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case STORAGE:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.USAGE_STATS;
                DO_CHECK();
                break;
            }
            case USAGE_STATS: {

                PERMISSION_STAGE = PERMISSION_STAGES.OK;
                RequestUsageStatsPermission();
                break;
            }
            case OK:
            {
                ((PermissionResultConvey)context).PermissionRequestFinished();
                break;
            }
        }
    }




}
