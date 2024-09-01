package fouriermachines.anidiag;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

import Framework.DataTypes.Delegates.UI.SystemInitializationUIConvey;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;

public class ScanActivity extends AppCompatActivity implements SystemInitializationUIConvey {


    ProgressDialog dialog;

    UIMAINConvey UserEventConvey;

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


    public void MachineLoadUI()
    {
        Intent Interaction = new Intent();
        Interaction.setClass(this, DiagActivity.class);
        startActivity(Interaction);
    }

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    private final int REQUEST_RECORD_PERMISSION = 100;

    enum InitializationStages {
        SCAN,
        CONNECT,
        NA
    }

    private FrameLayout ContextContentView;

    private FrameLayout ConnectView;


    InitializationStages InitializationStage = InitializationStages.NA;

    public InitializationStages getInitializationStage() {
        return InitializationStage;
    }

    public void setInitializationStage(InitializationStages initializationStage) {

        InitializationStage = initializationStage;
    }


    private boolean isServiceRunning(String className) {
        ActivityManager manager = (ActivityManager) GlobalContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo serviceInfo : services) {
            if (className.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        GlobalContext.context = getApplicationContext();

        isServiceRunning("fm.ani.kinetics.KineticsService");

        ContextContentView = (FrameLayout)findViewById(R.id.ContextContentView) ;

        ConnectView = (FrameLayout)findViewById(R.id.ConnectView) ;



        UIMAINModuleHandler.Instance.notifyOnSystemInitializationUIUpdate(this);


        UserEventConvey = UIMAINModuleHandler.Instance.GetUIMainConveyListener();
        DO_CHECK();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        UIMAINModuleHandler.Instance.GetUIMainConveyListener().CloseServiceConnections();
    }


    private   enum PERMISSION_STAGES{LOCATION, OK}
    private   PERMISSION_STAGES PERMISSION_STAGE = PERMISSION_STAGES.OK;
    public void DO_CHECK()
    {
        switch (PERMISSION_STAGE)
        {
            case OK:
            {
//NEW HARDWARE DEBUG
                UserEventConvey.AppStarted();
//                MachineLoadUI();
                break;
            }
        }
    }



}
