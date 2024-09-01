package fm.ani.fmlauncher;

import static Framework.JOBS.Applet.EventJob.Orientaiton.PORTRAIT_OnLoad;

import android.app.Service;
import android.app.fouriermachines.inputproxy.InputEventAnchor;
import android.app.fouriermachines.inputproxy.InputEventMonitor;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.security.cert.TrustAnchor;
import java.util.Map;

import Framework.DataTypes.AppletIntents;
import Framework.DataTypes.GlobalContext;
import Framework.JOBS.Applet.EventJob;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import fm.ani.fmlauncher.DataTypes.Delegates.PermissionResultConvey;
import fm.ani.fmlauncher.DataTypes.PermissionManager;
import fm.ani.fmlauncher.DataTypes.fmOverlay;
import fm.ani.fmlaunchertypes.ILauncherServiceAIDL;
//import fm.ani.fmlaunchertypes.ILauncherServiceAIDL;


import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;

public class LauncherService extends Service implements SystemInitializationUIConvey, InputEventMonitor,  PermissionResultConvey {

    long i=0;
    public enum KEY_CODES {
        UP, DOWN, LEFT, RIGHT, ROTATE;

        public static KEY_CODES fromInteger(int x) {
            switch(x) {
                case 0: //home button event
                    return ROTATE;
                case 19:
                    return UP;
                case 20:
                    return DOWN;
                case 21:
                    return LEFT;
                case 22:
                    return RIGHT;
            }
            return null;
        }
    }

    public  void onMotionEvent(MotionEvent motionEvent){
       i++;
        Log.i("MainActivity", "motion Action : " + motionEvent.getActionMasked());
    }
    public  void onKeyEvent(KeyEvent keyEvent){
        if((keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
            Log.i("MainActivity", "key Action : " + keyEvent.getKeyCode());
            switch (KEY_CODES.fromInteger(keyEvent.getKeyCode())) {
                case UP:
                    break;
                case DOWN:
                    break;
                case LEFT:
                    UIMAINModuleHandler.Instance.GetUIMainConveyListener().GetActionRequestHandler().TurnLeft();
                    break;
                case RIGHT:
                    UIMAINModuleHandler.Instance.GetUIMainConveyListener().GetActionRequestHandler().TurnRight();
                    break;
                case ROTATE:
                    if(IsMachineReady) {
                        switch (CURRENT_ORIENTATION) {
                            case PORTRAIT_OnLoad:
                                UIMAINModuleHandler.Instance.GetUIMainConveyListener().LoadApplet(AppletIntents.Intents.GENERIC, EventJob.Orientaiton.LANDSCAPE_OnLoad);
                                onAppletPoseSet = new PotraitPoseSetHandler(EventJob.Orientaiton.LANDSCAPE_OnLoad);
                                break;
                            case LANDSCAPE_OnLoad:
                                UIMAINModuleHandler.Instance.GetUIMainConveyListener().LoadApplet(AppletIntents.Intents.GENERIC, PORTRAIT_OnLoad);
                                onAppletPoseSet = new PotraitPoseSetHandler(PORTRAIT_OnLoad);
                                break;
                        }
                    }
                    break;
            }
        }
    }

    InputEventAnchor inpoutevent_anchor;

//    TouchEmulator mTouchEmulatorRunning;
    PermissionManager _PermissionManager;
    public LauncherService() {

    }

    Boolean IsServiceReady = false;
   Boolean IsMachineReady = false;



    @Override
    public void onCreate(){
        IsServiceReady = false;

//        mTouchEmulatorRunning = new TouchEmulator(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels, this);

        _PermissionManager = new PermissionManager(this);
        _PermissionManager.DO_CHECK();

        inpoutevent_anchor = (InputEventAnchor)getSystemService(INPUT_EVENT_ANCHOR_SERVICE);
        inpoutevent_anchor.register_events(this);

    }
    EventJob.Orientaiton CURRENT_ORIENTATION = PORTRAIT_OnLoad;
    public  interface  ActionHandler
    {
        public void func();
    }

    public class LoadActionHandler implements ActionHandler
    {
        EventJob.Orientaiton orientation;

        public LoadActionHandler(EventJob.Orientaiton _orientation)
        {
            orientation = _orientation;
        }

        public void func()
        {
            CURRENT_ORIENTATION = orientation;
            IsMachineReady = Boolean.TRUE;
            UIMAINModuleHandler.Instance.GetUIMainConveyListener().LoadActionHandler(orientation);
        }
    }
    public class PotraitPoseSetHandler implements  ActionHandler
    {
        EventJob.Orientaiton orientation;

        public PotraitPoseSetHandler(EventJob.Orientaiton _orientation)
        {
            orientation = _orientation;
        }

        public void func()
        {
            CURRENT_ORIENTATION = orientation;
            IsMachineReady = Boolean.TRUE;
        }
    }

    public ActionHandler onAppletPoseSet;

    @Override
    public IBinder onBind(Intent intent) {
        return new ILauncherServiceAIDL.Stub() {
            public void LaunchApp(String connectionID, String packageName)
            {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
                if(IsMachineReady) {
                    switch (packageName) {
                        case "net.osaris.turboflydemo": {
                            UIMAINModuleHandler.Instance.GetUIMainConveyListener().LoadApplet(AppletIntents.Intents.GENERIC, EventJob.Orientaiton.LANDSCAPE_OnLoad);
                            onAppletPoseSet = new LoadActionHandler(EventJob.Orientaiton.LANDSCAPE_OnLoad);
                            break;
                        }
                        case "com.kiloo.subwaysurf":
                            break;
                        case "fouriermachines.anidiag":
                            break;
                    }
                }
            }

            public void  SetPotrait(){
                    UIMAINModuleHandler.Instance.GetUIMainConveyListener().AppStarted();

            }
        };
    }


    //Start of PermissionResultConvey
    @Override
    public void PermissionRequestFinished()
    {
        IsServiceReady = Boolean.TRUE;

        UIMAINModuleHandler.Instance.notifyOnSystemInitializationUIUpdate(this);

    }
    //End of PermissionResultConvey

    //SystemInitializationUIConvey
    public void MachineConected(){
        IsMachineReady = Boolean.TRUE;
        UIMAINModuleHandler.Instance.GetUIMainConveyListener().LoadApplet(AppletIntents.Intents.GENERIC, PORTRAIT_OnLoad);
        onAppletPoseSet = new PotraitPoseSetHandler(PORTRAIT_OnLoad);
    }





    public void AppletPoseSet(){
        Log.i("LauncherService", "AppletPoseSet");
        if(onAppletPoseSet != null)
            onAppletPoseSet.func();
    }
    //End of SystemInitializationUIConvey

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
