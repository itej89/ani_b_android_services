package fm.ani.kinetics;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.lang.reflect.Method;

import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Machine;
import androidx.core.app.ActivityCompat;

public class KineticsStartServiceReciever extends BroadcastReceiver implements SystemInitializationUIConvey {



    UIMAINConvey UserEventConvey;

    Context _Context;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent intentService = new Intent(context,KineticsService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent);
//        } else {
//            context.startService(intentService);
//        }
//        Log.i("Autostart", "started");
        _Context = context;
        GlobalContext.context = context;
        UIMAINModuleHandler.Instance.notifyOnSystemInitializationUIUpdate(this);
        UserEventConvey = UIMAINModuleHandler.Instance.GetUIMainConveyListener();
        DO_CHECK();
    }




    enum InitializationStages {
        SCAN,
        CONNECT,
        NA
    }
    InitializationStages initializationStage = InitializationStages.NA;

    //SystemInitializationUIConvey
    public void ClearScanDeatils()
    {
    }

    public void  ResetInitializing()
    {

    }

    public void  Scanning()
    {
        initializationStage = InitializationStages.SCAN;
    }

    public void  Binding()
    {
        initializationStage = InitializationStages.CONNECT;
    }

    public void  PauseApplication()
    {

    }

    public void ShutdownApplication()
    {

    }

    public void  ResumeApplication()
    {

    }
    public void  NewMachineFound(Machine Device)
    {
        DiscoveredNewDevice(Device);
    }

    public void  MachinePoweredOff()
    {
    }

    public void  MachineDisconnected()
    {

    }

    public void  MachineConected()
    {
    }

    public void  MachineConnectionTimeout()
    {

    }

    public void  MachineIsMovingTOMountPosition()
    {

    }

    public void  MachineCheckingInitialProximity()
    {
    }

    public void  MachinWaitingForMounting()
    {

    }

    public void MachineLoadKineticsService()
    {
//        Intent intent = new Intent(_Context,KineticsService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            _Context.startForegroundService(intent);
//        } else {
//            _Context.startService(intent);
//        }
        Log.i("Ani Kinetics", "Machine Kinetics Service started");
    }

    public void ShutdownRequest()
    {
        try {
            PowerManager powerManager = (PowerManager)_Context.getSystemService(Context.POWER_SERVICE);
            powerManager.shutdown(false, null, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ShowBlankScreen()
    {
    }
    //End of SystemInitializationUIConvey



    public void DiscoveredNewDevice(final Machine Name) {
        if(initializationStage == InitializationStages.SCAN ) {
            if(Name.Name.equals("FTDI_ANI_232"))
            // if(Name.Name.equals("/dev/ttyUSB0"))
            {
                UserEventConvey.UserSelectedAMachine(Name);
            }
        }
    }



    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";


    public boolean grantAutomaticPermission(UsbDevice usbDevice)
    {
        try
        {
            Context context= _Context;
            PackageManager pkgManager=context.getPackageManager();
            ApplicationInfo appInfo=pkgManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            Class serviceManagerClass=Class.forName("android.os.ServiceManager");
            Method getServiceMethod=serviceManagerClass.getDeclaredMethod("getService",String.class);
            getServiceMethod.setAccessible(true);
            android.os.IBinder binder=(android.os.IBinder)getServiceMethod.invoke(null, Context.USB_SERVICE);

            Class iUsbManagerClass=Class.forName("android.hardware.usb.IUsbManager");
            Class stubClass=Class.forName("android.hardware.usb.IUsbManager$Stub");
            Method asInterfaceMethod=stubClass.getDeclaredMethod("asInterface", android.os.IBinder.class);
            asInterfaceMethod.setAccessible(true);
            Object iUsbManager=asInterfaceMethod.invoke(null, binder);


            System.out.println("UID : " + appInfo.uid + " " + appInfo.processName + " " + appInfo.permission);
            final Method grantDevicePermissionMethod = iUsbManagerClass.getDeclaredMethod("grantDevicePermission", UsbDevice.class,int.class);
            grantDevicePermissionMethod.setAccessible(true);
            grantDevicePermissionMethod.invoke(iUsbManager, usbDevice,appInfo.uid);


            System.out.println("PERMISSION OK : "+usbDevice.getManufacturerName());
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Error trying to assing automatic usb permission : ");
            e.printStackTrace();
            return false;
        }
    }

    private final BroadcastReceiver mUsbReceiver;
    {
        mUsbReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {

                                _Context.unregisterReceiver(mUsbReceiver);

                                DO_CHECK();
                            }
                        } else {
                            Log.d("AUDIOMODULETEster", "permission denied for device " + device);
                        }
                    }
                }
            }
        };
    }

    public boolean RequestUSBSerialPermission() {
        UsbDevice serial = UIMAINModuleHandler.Instance.GetUIMainConveyListener().GetUSBSerialDevice();
        if(!grantAutomaticPermission(serial)) {
            if (serial != null) {

                UsbManager mUsbManager = (UsbManager) _Context.getSystemService(Context.USB_SERVICE);

                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(_Context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                _Context.registerReceiver(mUsbReceiver, filter);
                mUsbManager.requestPermission(serial, mPermissionIntent);
                return true;
            }
            return false;
        }
        else
        {
            DO_CHECK();
            return  true;
        }
    }





    private final int WRITE_REQUEST_CODE = 300;

    public Boolean RequestStoragePermission() {

        PermissionEverywhere.getPermission(_Context,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_REQUEST_CODE,
                "Notification title",
                "This app needs a write permission",
                R.mipmap.ic_launcher)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        DO_CHECK();
                    }
                });

        return  true;
    }



    private   enum PERMISSION_STAGES{USBSERIAL, STORAGE, OK}
    private   PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.USBSERIAL;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case USBSERIAL:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.STORAGE;
                RequestUSBSerialPermission();
                break;
            }
            case STORAGE:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.OK;
                RequestStoragePermission();
                break;
            }
            case OK:
            {
                UserEventConvey.AppStarted();
                break;
            }
        }
    }
}
