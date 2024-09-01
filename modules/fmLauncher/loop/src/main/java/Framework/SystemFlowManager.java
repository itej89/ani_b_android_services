package Framework;

import android.util.Log;


import Framework.DataTypes.AppletIntents;
import Framework.DataTypes.Delegates.Applet.ActionRequestConvey;
import Framework.DataTypes.Delegates.Applet.AppletJobConvey;
import Framework.DataTypes.Delegates.ServiceConnectionConvey;

import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.JOBS.Applet.AugmentRacingJob;
import Framework.JOBS.Applet.EventJob;
import Framework.JOBS.Initialization.ServiceConnectionJob;
import Framework.SystemEventHandlers.KineticServiceStateHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import Framework.DataTypes.Delegates.Generic.GenericTypeDelegate;
import FrameworkInterface.PublicTypes.Config.MachineConfig;

public class SystemFlowManager implements  ServiceConnectionConvey, UIMAINConvey, AppletJobConvey {

        ServiceConnectionJob serviceConnectionJob;

        AugmentRacingJob augmentRacingJob;
        EventJob eventJob;
        void PauseCurrentJOB() {
                Scheduler.SharedInstance.PauseCurrentJob();
        }
        void EndCurrentJob() {
                PauseCurrentJOB();
        }

        public  void LoadApplet(AppletIntents.Intents Name, EventJob.Orientaiton orientation){
                if(eventJob != null)
                        Scheduler.SharedInstance.KillJobWithID(eventJob.ID);
                eventJob = new EventJob(Name,  orientation, this);
                Scheduler.SharedInstance.AddJob(eventJob);
        }
        public void LoadActionHandler(EventJob.Orientaiton ReferanceOrientaiton){
                if(augmentRacingJob != null)
                        Scheduler.SharedInstance.KillJobWithID(augmentRacingJob.ID);
                augmentRacingJob = new AugmentRacingJob(ReferanceOrientaiton.toString());
                Scheduler.SharedInstance.AddJob(augmentRacingJob);
        }

        public ActionRequestConvey GetActionRequestHandler(){
                return augmentRacingJob;
        }

        //AppletJobConvey
        public void EventAnimationFinished() {
                UIMAINModuleHandler.Instance.InitUIHandler.AppletPoseSet();
        }
        //End of AppletJobConvey

        //UIMAINConvey
        public void AppStarted()
        {
                if(serviceConnectionJob != null) {
                        Scheduler.SharedInstance.notify_Finish(serviceConnectionJob.ID);
                }
                serviceConnectionJob = new ServiceConnectionJob(this);

                Scheduler.SharedInstance.AddJob(serviceConnectionJob);
        }
        //End of UIMAINConvey

        public void CloseServiceConnections()
        {
                KineticComms.Instance.releaseServiceConnection();
        }

        public static SystemFlowManager Instance = new SystemFlowManager();

        private SystemFlowManager()
        {
                // DB_Local_Store Db = new DB_Local_Store();
                // Db.RemoveDB();
                //  Db.PlaceDB();
                KineticServiceStateHandler.Instance.setNotify_ServiceConnectionConvey(this);
        }

        //ServiceConnectionConvey
        public  void  ServiceConnectionJobStatus(Boolean Status)
        {
                Log.i("FLLauncher", "Kinetics Service connection established");
                KineticComms.Instance.BindMachine();
        }

        public  void  ServiceBindStatusConvey(String Status)
        {
                Log.i("FLLauncher", "ServiceBindStatusConvey : "+Status);
                if(Status.equals("MACHINE_BINDED")) {
                        MachineConfig.Instance = KineticComms.Instance.GetMachineConfig();
                        UIMAINModuleHandler.Instance.InitUIHandler.MachineConected();
                }
        }
        //End of ServiceConnectionConvey
}