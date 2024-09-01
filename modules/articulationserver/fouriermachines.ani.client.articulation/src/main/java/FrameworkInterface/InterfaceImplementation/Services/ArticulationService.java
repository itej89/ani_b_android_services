package FrameworkInterface.InterfaceImplementation.Services;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.lang.reflect.Method;
import java.util.Map;

import Framework.DataTypes.Delegates.AIServerStatusConvey;
import Framework.DataTypes.GlobalContext;
import FrameworkInterface.IArticulationAccessAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementations.AIManager;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class ArticulationService extends Service implements AIServerStatusConvey {
    public ArticulationService() {
    }

    //AIServerStatusConvey
    public void ConnectedToAIService(){
        DO_CHECK();}
    public void AIServiceDisconnected(){}
    //End of AIServerStatusConvey

    Boolean IsServiceReady = false;

    @Override
    public void onCreate(){
        IsServiceReady = false;
        GlobalContext.context = this;
        AIManager.Instance.SubScribeAIServerStatus(this);
        DO_CHECK();
    }



    @Override
    public IBinder onBind(Intent intent) {

        return new IArticulationAccessAIDL.Stub() {

            public byte IsServiceReady(String connectionID){
                return  IsServiceReady? (byte)1 : 0;
            }

            public void ConnectToArticulationServices(String connectionID, Map AiConveyList){
                ArticulationManager.Instance.ConnectToArticulationServices(connectionID, AiConveyList);
            }

            //Recognizer Calls
            public void StartListeningToUser(String connectionID){
                ArticulationManager.Instance.StartListeningToUser(connectionID);
            }
            public void StopListening(String connectionID){
                ArticulationManager.Instance.StopListening(connectionID);
            }

            public byte IsSoundPlayerPlaying(String connectionID){
               return ArticulationManager.Instance.IsSoundPlayerPlaying(connectionID) ? (byte)1: 0;
            }

            public void StartRecognition(String connectionID){
                ArticulationManager.Instance.StartRecognition(connectionID);
            }
            public void StopRecognition(String connectionID){
                ArticulationManager.Instance.StopRecognition(connectionID);
            }

            public void StartWakeWordDetection(String connectionID){
                ArticulationManager.Instance.StartWakeWordDetection(connectionID);
            }
            public void StopWakeWordDetection(String connectionID){
                ArticulationManager.Instance.StopWakeWordDetection(connectionID);
            }
            //End of Recognizer Calls

            //Player Calls
            public void ReadyAudioStream(String connectionID){
                ArticulationManager.Instance.ReadyAudioStream(connectionID);
            }
            public void PlayAudioStream(String connectionID, byte[] Stream){
                ArticulationManager.Instance.PlayAudioStream(connectionID, Stream);
            }
            public void CloseAudioStream(String connectionID){
                ArticulationManager.Instance.CloseAudioStream(connectionID);
            }

            public void PlaySoundSegment(String connectionID, String fileName, int StartSec, int EndSec, float Volume, float FadeDuration){
                ArticulationManager.Instance.PlaySoundSegment(connectionID, fileName, StartSec, EndSec, Volume, FadeDuration);
            }
            public void PlaySound(String connectionID, String fileName, float Volume, float FadeDuration){
                ArticulationManager.Instance.PlaySound(connectionID, fileName, Volume, FadeDuration);
            }
            public void PauseSound(String connectionID){
                ArticulationManager.Instance.PauseSound(connectionID);
            }
            public void PlayWavData(String connectionID, String WavData_UTF8, float Volume, float FadeDuration){
                ArticulationManager.Instance.PlayWavData(connectionID, WavData_UTF8, Volume, FadeDuration);
            }
            //End of Player Calls


            //Synthesizer Calls
            public void SpeakText(String connectionID, String _content, float _UtteranceRate, float _PitchMultiplier, String _language){
                ArticulationManager.Instance.SpeakText(connectionID, _content, _UtteranceRate, _PitchMultiplier, _language);
            }
            //End of Synthesizer Calls
        };
    }



    public boolean grantAutomaticPermission(UsbDevice usbDevice)
    {
        try
        {
            Context context= GlobalContext.context;
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



    private final int REQUEST_RECORD_PERMISSION = 1000;
    private final int WRITE_REQUEST_CODE = 3000;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

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
                                unregisterReceiver(mUsbReceiver);

                                DO_CHECK();
                            }
                        } else {
                            Log.d("Ani B", "USB permission denied for device " + device);
                        }
                    }
                }
            }
        };
    }

    public boolean RequestUSBMicPermission() {
        UsbDevice speakr = ArticulationManager.Instance.GetUSBMicDevice();
        if(!grantAutomaticPermission(speakr)) {
            if (speakr != null) {

                UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                registerReceiver(mUsbReceiver, filter);
                mUsbManager.requestPermission(speakr, mPermissionIntent);
                return true;
            }

        }
        else
        {
            DO_CHECK();
            return  true;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public Boolean RequestSpeechPermission(){

        PermissionEverywhere.getPermission(GlobalContext.context,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_PERMISSION,
                "Notification title",
                "This app needs record permission", android.R.mipmap.sym_def_app_icon)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        if(permissionResponse.getRequestCode() == REQUEST_RECORD_PERMISSION)
                        DO_CHECK();
                    }
                });

        return  true;
    }


    public Boolean RequestStoragePermission() {

        PermissionEverywhere.getPermission(GlobalContext.context,
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


    private   enum PERMISSION_STAGES{USBMIC, SPEECH, STORAGE , CONNECT_TO_AI_SERVICE, OK}
    private PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.USBMIC;

    int Wait_For_Service_Count = 0;
    int Total_Wait_For_Service_Count = 100;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case USBMIC:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.SPEECH;
                RequestUSBMicPermission();
                break;
            }
            case SPEECH:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.STORAGE;
               RequestSpeechPermission();
               break;
             }
            case STORAGE:
            {
                PERMISSION_STAGE = PERMISSION_STAGES.CONNECT_TO_AI_SERVICE;
                RequestStoragePermission();
                break;
            }
            case CONNECT_TO_AI_SERVICE: {
                PERMISSION_STAGE = PERMISSION_STAGES.OK;
                Wait_For_Service_Count = 0;
                AIManager.Instance.initServiceConnection();
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
