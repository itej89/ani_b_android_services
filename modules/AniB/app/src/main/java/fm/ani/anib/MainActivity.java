package fm.ani.anib;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ProcessLifecycleOwner;


public class MainActivity extends AppCompatActivity implements SystemInitializationUIConvey
{
    ProgressDialog dialog;
    UIMAINConvey UserEventConvey;
    View ViewMainHeadLayer;


    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);

        int rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.rotationAnimation = rotationAnimation;
        win.setAttributes(winParams);
    }


    //SystemInitializationUIConvey

        public void  MachineDisconnected()
        {
            if(dialog != null && dialog.isShowing())
                dialog.cancel();

        }

        public void  MachineConected()
        {
            if(dialog != null && dialog.isShowing())
                dialog.cancel();
        }

        public void  MachineLoadAni()
        {

            this.runOnUiThread (new Runnable() {
                @Override
                public void run() {
                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {                       // ViewMainHeadLayer.setVisibility(View.VISIBLE);

                            ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewMainHeadLayer, "alpha", 1.0f);
                            colorAnimation.setDuration(500); // milliseconds
                            colorAnimation.setInterpolator(new AccelerateInterpolator());

                            colorAnimation.addListener(new AnimatorListenerAdapter()
                            {
                                @Override
                                public void onAnimationEnd(Animator animation)
                                {

                                    Intent Interaction = new Intent();
                                    Interaction.setClass(activity, Ani.class);
                                    startActivity(Interaction);
                                }
                            });
                            colorAnimation.start();


                        }
                    });
                }
            });
        }

    //End of SystemInitializationUIConvey




    private AppStateListener lifecycleListener   = new AppStateListener();



    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        GlobalContext.context = getApplicationContext();
        activity = this;

        AudioManager manager = (AudioManager)GlobalContext.context.getSystemService(Context.AUDIO_SERVICE) ;

        int max_volume = (manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        manager.setStreamVolume(AudioManager.STREAM_MUSIC , 12 ,0);

        ViewMainHeadLayer = findViewById(R.id.ViewMainHeadLayer) ;

        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleListener);

        lifecycleListener.appDelegate = UIMAINModuleHandler.Instance.GetApplicationStateDelegate();

        UIMAINModuleHandler.Instance.notifyOnSystemInitializationUIUpdate(this);


        UserEventConvey = UIMAINModuleHandler.Instance.GetUIMainConveyListener();



    }

    public void ShutdownRequest()
    {
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ViewMainHeadLayer.setAlpha(0);
                        ViewMainHeadLayer.setVisibility(View.VISIBLE);
                        ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewMainHeadLayer, "alpha", 1.0f);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.setInterpolator(new AccelerateInterpolator());

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                try {
                                    PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                                    powerManager.shutdown(false, null, false);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        });
                        colorAnimation.start();

                    }
                });
            }
        });

    }


    private final int WRITE_REQUEST_CODE = 300;



    public Boolean RequestStoragePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case WRITE_REQUEST_CODE: {
                DO_CHECK();
                break;
            }
        }
    }





    private   enum PERMISSION_STAGES{STORAGE, OK}
    private   PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.STORAGE;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case STORAGE:
            {
                    PERMISSION_STAGE = PERMISSION_STAGES.OK;
                    DO_CHECK();
                break;
            }
            case OK:
            {

                UserEventConvey.AppStarted();
                break;
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DO_CHECK();
    }

}

