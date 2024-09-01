package fm.ani.fmlauncher;

import Framework.DataTypes.GlobalContext;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import Framework.SystemEventHandlers.UIMAINModuleHandler;
import fm.ani.fmlauncher.DataTypes.Delegates.AppSelectionConvey;
import fm.ani.fmlauncher.DataTypes.Delegates.MotionEventConvey;
import fm.ani.fmlauncher.DataTypes.LauncherServiceConnection;
import fm.ani.fmlaunchertypes.ILauncherServiceAIDL;

import android.app.Activity;
import android.app.fouriermachines.inputproxy.InputEventAnchor;
import android.app.fouriermachines.inputproxy.InputEventMonitor;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AppSelectionConvey {

    public boolean enableAutoRotate(){

        boolean isAutoRotate = android.provider.Settings.System
                .getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1) == 1;

        if (!isAutoRotate){d
            try {
                android.provider.Settings.System
                        .putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        isAutoRotate = android.provider.Settings.System
                .getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1) == 1;

        return isAutoRotate;
    }


    public void rotateScreen(boolean reverse){

        if (enableAutoRotate()){
            if (reverse) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);

            }

            else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }
        }
    }

   public void app_selected(String app_name){
       try {
           connectedService.LaunchApp(getPackageName(), app_name);
       }
        catch (Exception e){
            Log.e("fmLauncher", "setOnClickListener :", e);
        }
    }

    public void connecttoService(){
        try {
            _LauncherServiceConnection = new LauncherServiceConnection(){
                public void onServiceConnected(ComponentName className, IBinder service) {
                    try {
                        connectedService = ((ILauncherServiceAIDL)service);
                        connectedService.SetPotrait();
                    } catch (Exception e) {
                        Log.e("fmLauncher", "SetPotrait : " + e.getMessage());
                    }
                }

                public void onServiceDisconnected(ComponentName className) {
                }
            };
            Intent i = new Intent();
            i.setClassName("fm.ani.fmlauncher", "fm.ani.fmlauncher.LauncherService");
            boolean ret = GlobalContext.context.bindService(i, _LauncherServiceConnection, Context.BIND_AUTO_CREATE);
            if (ret)
                Log.e("fmLauncher", "LauncherServiceConnection OK");
            else
                Log.e("fmLauncher", "LauncherServiceConnection NOK");
        } catch (Exception e) {
            Log.e("fmLauncher", "LauncherServiceConnection : " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connecttoService();

        GlobalContext.context = this;
        loadFragment(new AppsDrawerFragment(this));


     }

    LauncherServiceConnection _LauncherServiceConnection;
    ILauncherServiceAIDL connectedService;
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1234);
//            }

            if(connectedService == null)
            {
                connecttoService();
            }


            try {
                connectedService.SetPotrait();
            } catch (Exception e) {

                Log.e("fmLauncher", "SetPotrait : " + e.getMessage());
            }

        }

        rotateScreen(true);



    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

//        loadFragment(new HomeScreenFragment());
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fr)
    }

}
