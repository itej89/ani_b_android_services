package Framework.JOBS.Initialization;

import android.os.Handler;
import android.util.Log;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.ServiceConnectionStates;
import Framework.DataTypes.Delegates.AIServerStatusConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Delegates.ServiceConnectionConvey;
import Framework.DataTypes.Job;
import Framework.SystemEventHandlers.KineticServiceStateHandler;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;
import FrameworkInterface.DataTypes.Delegates.ArticulationServerStatusConvey;
import FrameworkInterface.DataTypes.Delegates.BotConnServerStatusConvey;
import FrameworkInterface.DataTypes.Delegates.KineticsServiceStatusConvey;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.InterfaceImplementations.AIManager;

public class ServiceConnectionJob extends Job implements KineticsServiceStatusConvey,
        ArticulationServerStatusConvey,AIServerStatusConvey, BotConnServerStatusConvey {

    String LOG_TAG  = "LOG_SERVICE_CONNECTIO_JOB";
    ServiceConnectionConvey notify_ServiceConnectionConvey;

    public ServiceConnectionJob(ServiceConnectionConvey connectionConvey) {
        super();
        notify_ServiceConnectionConvey = connectionConvey;
        PRIORITY = JOB_PRIORITY.SYSTEM_CRITICAL;
    }


    public void ConnectedToArticulationService(){DOSTEP();}
    public void ArticulationServiceDisconnected(){}

    //BotConnServerStatusConvey
    public void BotConnServiceConnected(){ DOSTEP(); }
    public void BotConnServiceDisconnected(){}
    //End of BotConnServerStatusConvey

    //AIServerStatusConvey
    public void ConnectedToAIService(){ DOSTEP(); }
    public void AIServiceDisconnected(){}
    //End of AIServerStatusConvey

    //KineticsServiceStatusConvey
    public void ConnectedToService(){ DOSTEP(); }
    public void ServiceDisconnected(){
        CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_KINETICS_SERVICE;
        DOSTEP();
    }
    //End of KineticsServiceStatusConvey

    ServiceConnectionStates.STATES CURRENT_STATE = ServiceConnectionStates.STATES.NA;
    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        AIManager.Instance.SubScribeAIServerStatus(this);
        ArticulationManager.Instance.setOnArticulationServerStatusConvey(this);
        KineticComms.Instance.setServiceStatusConvey(this);
        BotConnectImplementation.Instance.SubscribeToBotConnServerStatus(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_AI_SERVICE;
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
            case CONNECT_TO_AI_SERVICE:
                Log.d(LOG_TAG, "CONNECT_TO_AI_SERVICE");
                Wait_For_Service_Count = 0;
                CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_ARTICULATION_SERVICE;
                if(!AIManager.Instance.initServiceConnection())
                {
                    if(notify_ServiceConnectionConvey != null)
                        notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    Pause();
                }
                break;
            case CONNECT_TO_ARTICULATION_SERVICE:
                Log.d(LOG_TAG, "CONNECT_TO_ARTICULATION_SERVICE");
                Wait_For_Service_Count = 0;
                CURRENT_STATE = ServiceConnectionStates.STATES.WAIT_FOR_ARTICULATION_SERVICE;
                if(!ArticulationManager.Instance.initServiceConnection())
                {
                    if(notify_ServiceConnectionConvey != null)
                        notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    Pause();
                }
                break;
            case WAIT_FOR_ARTICULATION_SERVICE:
                Log.d(LOG_TAG, "WAIT_FOR_ARTICULATION_SERVICE");
                if(ArticulationManager.Instance.IsServiceReady())
                {
                    CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_BOT_CONN_SERVICE;
                    DOSTEP();
                }else
                {
                    Wait_For_Service_Count++;
                    if(Wait_For_Service_Count > Total_Wait_For_Service_Count)
                    {
                            if(notify_ServiceConnectionConvey != null) {
                                CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_BOT_CONN_SERVICE;
                                notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                            }
                        }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DOSTEP();
                        }
                    }, 100);
                }
                break;
            case CONNECT_TO_BOT_CONN_SERVICE:
                Log.d(LOG_TAG, "CONNECT_TO_BOT_CONN_SERVICE");
                Wait_For_Service_Count = 0;
                CURRENT_STATE = ServiceConnectionStates.STATES.WAIT_FOR_BOT_CONN_SERVICE;
                if(!BotConnectImplementation.Instance.InitServiceConnection())
                {
                    if(notify_ServiceConnectionConvey != null)
                        notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    Pause();
                }
                break;
            case WAIT_FOR_BOT_CONN_SERVICE:
                Log.d(LOG_TAG, "WAIT_FOR_BOT_CONN_SERVICE");
                if(BotConnectImplementation.Instance.IsServiceReady())
                {
                    CURRENT_STATE = ServiceConnectionStates.STATES.CONNECT_TO_KINETICS_SERVICE;
                    DOSTEP();
                }else
                {
                    Wait_For_Service_Count++;
                    if(Wait_For_Service_Count > Total_Wait_For_Service_Count)
                    {
                        if(notify_ServiceConnectionConvey != null)
                            notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DOSTEP();
                        }
                    }, 100);
                }
                break;
            case CONNECT_TO_KINETICS_SERVICE:
                Log.d(LOG_TAG, "CONNECT_TO_KINETICS_SERVICE");
                Wait_For_Service_Count = 0;
                CURRENT_STATE = ServiceConnectionStates.STATES.WAIT_FOR_KINETICS_SERVICE;
                if(!KineticComms.Instance.initServiceConnection())
                {
                    if(notify_ServiceConnectionConvey != null)
                        notify_ServiceConnectionConvey.ServiceConnectionJobStatus(false);
                    Pause();
                }
                break;
            case WAIT_FOR_KINETICS_SERVICE:
                Log.d(LOG_TAG, "WAIT_FOR_KINETICS_SERVICE");
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
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DOSTEP();
                        }
                    }, 100);
                }
                break;
            case FINISH:
                Log.d(LOG_TAG, "FINISH");
                     if(notify_ServiceConnectionConvey != null)
                    notify_ServiceConnectionConvey.ServiceConnectionJobStatus(true);
                Pause();
                break;
        }
    }
}
