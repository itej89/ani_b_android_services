package Framework.JOBS.Initialization;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.ActuatorAttachStates;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.ServiceConnectionStates;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Delegates.ServiceConnectionConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.SystemEventHandlers.KineticServiceStateHandler;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

public class ServiceConnectionJob extends Job implements KineticsServiceStatusConvey {

    ServiceConnectionConvey notify_ServiceConnectionConvey;

    public ServiceConnectionJob(ServiceConnectionConvey connectionConvey) {
        super();
        notify_ServiceConnectionConvey = connectionConvey;
        PRIORITY = JOB_PRIORITY.SYSTEM_CRITICAL;
    }

    //KineticsServiceStatusConvey
    public void ConnectedToService(){
        DOSTEP();
    }
    public void ServiceDisconnected(){
        CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_KINETICS_SERVICE;
        DOSTEP();
    }
    //End of KineticsServiceStatusConvey

    ServiceConnectionStates.STATES CURRENT_STATE = ServiceConnectionStates.STATES.NA;
    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.setServiceStatusConvey(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_KINETICS_SERVICE;
    }
    @Override
    public void Pause() {
        KineticComms.Instance.setServiceStatusConvey(KineticServiceStateHandler.Instance);
        KineticComms.Instance.ResetCommsContext();
        super.Pause();
        super.delegate.notify_Finish( ID);
    }
    @Override
    public void Resume() {

        DOSTEP();
    }
//End Job Override


    int Wait_For_Service_Count = 0;
    int Total_Wait_For_Service_Count = 100;
    void DOSTEP()
    {
        switch(CURRENT_STATE)
        {
            case CONNECT_TO_KINETICS_SERVICE:
                Wait_For_Service_Count = 0;
                CURRENT_STATE = ServiceConnectionStates.STATES.WAIT_FOR_SERVICE;
                if(!KineticComms.Instance.initServiceConnection())
                {
                    if(notify_ServiceConnectionConvey != null)
                        notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    Pause();
                }
                break;
            case WAIT_FOR_SERVICE:
                if(KineticComms.Instance.IsServiceReady())
                {
                    CURRENT_STATE = ServiceConnectionStates.STATES.FINISH;
                    DOSTEP();
                }else
                {
                    Wait_For_Service_Count++;
                    if(Wait_For_Service_Count > Total_Wait_For_Service_Count)
                    {
                        if(notify_ServiceConnectionConvey != null)
                            notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                        Pause();
                    }
                    else {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DOSTEP();
                            }
                        }, 100);
                    }
                }
                break;
            case FINISH:
                if(notify_ServiceConnectionConvey != null)
                    notify_ServiceConnectionConvey.ServiceConnectionJobStatus(true);
                Pause();
                break;
        }
    }
}
