package fm.ani.articulationserver;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import Framework.DataTypes.GlobalContext;
import FrameworkInterface.ArticulationAccess;
import FrameworkInterface.IArticulationAccessAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementation.Services.ArticulationService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//
//    private boolean isServiceRunning(String className) {
//        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
//        final List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
//        for (ActivityManager.RunningServiceInfo serviceInfo : services) {
//            String name = serviceInfo.service.getClassName();
//            if (className.equals(name)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
    public class ArticulationServiceConnection implements ServiceConnection {

        IArticulationAccessAIDL service;

        public IArticulationAccessAIDL getService()
        {
            return  service;
        }

        public Boolean IsConnectedToAi()
        {
            return service != null ? true : false;
        }

        public  void ArticulationServiceConnection(){}

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IArticulationAccessAIDL.Stub.asInterface((IBinder) boundService);
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }

    }

    Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _context  = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                isServiceRunning(ArticulationService.class.toString());

            }
        });

//        ArticulationServiceConnection connection  = new ArticulationServiceConnection();
//
//        Intent i = new Intent();
//        i.setClassName("fm.ani.articulationserver", "FrameworkInterface.InterfaceImplementation.Services.ArticulationService");
//        boolean ret = _context.bindService(i, connection, Context.BIND_AUTO_CREATE);
//
//        boolean bret = ret;


    }

}
