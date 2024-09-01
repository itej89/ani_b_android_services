package fm.ani.kinetics;

import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.GlobalContext;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    KineticsStartServiceReciever myReceiver;
    Button btnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("fm.ani.kinetics.kineticsstartservicereciever");

         myReceiver = new KineticsStartServiceReciever();
        registerReceiver(myReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("fm.ani.kinetics.kineticsstartservicereciever");
        sendBroadcast(intent);

        btnClick = (Button)findViewById(R.id.btnClick);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isServiceRunning("KineticsService");
            }
        });
    }


    private boolean isServiceRunning(String className) {
        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo serviceInfo : services) {
            String name = serviceInfo.service.getClassName();
            if (className.equals(name)) {
                return true;
            }
        }
        return false;
    }


}
