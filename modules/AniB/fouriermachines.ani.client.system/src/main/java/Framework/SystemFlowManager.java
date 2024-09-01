package Framework;

import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import Framework.DataTypes.AppletIntents;
import Framework.DataTypes.Constants.BIND_STATES;
import Framework.DataTypes.Delegates.Applet.AppletJobConvey;
import Framework.DataTypes.Delegates.Generic.GenericTypeDelegate;
import Framework.DataTypes.Delegates.QASession.QAAIServerConvey;
import Framework.DataTypes.Delegates.QASession.QAListenerJobConvey;
import Framework.DataTypes.Delegates.QASession.QAResponseJobConvey;
import Framework.DataTypes.Delegates.QASession.QASessionStartStopListener;
import Framework.DataTypes.Delegates.ServiceConnectionConvey;
import Framework.DataTypes.Delegates.ShutDownRequest;
import Framework.DataTypes.Delegates.StudioSession.StudioSessionStartStopListener;
import Framework.DataTypes.Delegates.UI.AniUIConvey;
import Framework.DataTypes.Delegates.UI.AppletUIConvey;
import Framework.DataTypes.Delegates.UI.TestTriggerConvey;
import Framework.DataTypes.Delegates.UI.UIChoreogramConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.JOBS.Applet.Nurse.DynamicBeatJob;
import Framework.JOBS.Applet.EventJob;
import Framework.JOBS.Initialization.ServiceConnectionJob;
import Framework.JOBS.StudioSession.ChoreogramJob;
import Framework.JOBS.IDLEAnimSession.IDLEAnimStatusDelegate;
import Framework.SystemEventHandlers.KineticServiceStateHandler;
import Framework.SystemEventHandlers.LinkAnchorStreamHandler;
import Framework.SystemEventHandlers.StudioSessionHandler;
import Framework.SystemEventHandlers.UIChoreogramHandler;
import FrameworkImplementation.DataTypes.BotConnectionInfo;
import FrameworkImplementation.DataTypes.Delegates.BotConnectConvey;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;
import FrameworkInterface.InterfaceImplementations.AIManager;
import FrameworkInterface.DataTypes.Delegates.ArticulationConvey;
import FrameworkInterface.DataTypes.Delegates.SynthesizerConvey;
import FrameworkInterface.Enums.IOTIntents;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.MachinePositionContext;
import FrameworkInterface.MQTTInterfaceImplementation;
import Framework.JOBS.IDLEAnimSession.IDLEAnimationJob;
import Framework.JOBS.QASession.QAIDLEAnimationJob;
import Framework.JOBS.QASession.QAListeningAnimationJob;
import Framework.JOBS.QASession.QAResponseJob;
import Framework.SystemEventHandlers.KineticRemoteRequestHandler;
import Framework.SystemEventHandlers.QASessionHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;

import static Framework.DataTypes.Constants.BIND_STATES.STATES.MACHINE_BINDED;

public class SystemFlowManager implements ServiceConnectionConvey, UIChoreogramConvey, BotConnectConvey,
        QAListenerJobConvey, StudioSessionStartStopListener, IDLEAnimStatusDelegate,
        UIMAINConvey, AniUIConvey, QASessionStartStopListener, TestTriggerConvey,
        ArticulationConvey, QAAIServerConvey, SynthesizerConvey,ShutDownRequest,
        QAResponseJobConvey, AppletJobConvey, AppletUIConvey
        {

SystemFlowManager getOuterObject()
{
    return  SystemFlowManager.this;
}

            GenericTypeDelegate BindCompletedAction;


//StudioSessionStartStopListener
public void StartStudioSession()
{
        BotConnectImplementation.Instance.SubscribeToBotConnectConvey(this);
}
public void StopStudioSession()
{
        BotConnectImplementation.Instance.DettachFromBotConnectEvents();
}
//End of StudioSessionStartStopListener


//TestTriggerUIConvey
public void TestConnectToStudio() {
        EndCurrentJob();
//        StartStudioSession();
        }

public void TestDisconnectStudio() {
//        StopStudioSession();
        }
        //End of TestTriggerUIConvey



        ServiceConnectionJob serviceConnectionJob;

        IDLEAnimationJob animationIDLEjob;


        EventJob eventJob;
        DynamicBeatJob dynamicBeatJob;

        QAIDLEAnimationJob QAIdleJob;
        QAListeningAnimationJob QAListenJob;
        QAResponseJob QAAnswerJob;


        ChoreogramJob choreogramJob;

        BIND_STATES.STATES CurrentBindState = BIND_STATES.STATES.NA;



        void SetCurrentBindState(BIND_STATES.STATES State)
        {
                if(CurrentBindState != State)
                {
                        CurrentBindState = State;
                }
        }







        void PauseCurrentJOB()
        {
        Scheduler.SharedInstance.PauseCurrentJob();
        }

        void EndCurrentJob()
        {
                PauseCurrentJOB();
        }




        public  void BindBeforeRunJob(GenericTypeDelegate RunJob)
        {

                BindCompletedAction = RunJob;

                if(CurrentBindState == BIND_STATES.STATES.IDLE && animationIDLEjob != null)
                {
                        Scheduler.SharedInstance.KillJobWithID( animationIDLEjob.ID);
                }

                if(CurrentBindState == BIND_STATES.STATES.SLEEP)
                {
                        KineticComms.Instance.BindMachine();
                        return;
                }

                ServiceBindStatusConvey(MACHINE_BINDED.toString());

        }

        //IDLEAnimDelegate
        public void IDLEAnimationStarted()
        {
                SetCurrentBindState(BIND_STATES.STATES.IDLE);
        }
        public void IDLEAnimationFinished()
        {
                KineticComms.Instance.UnBindMachine();
        }
        //End of  IDLEAnimDelegate




        //UIMAINConvey
        public void AppStarted() {

                ResetAllSessionListeners();
                if(serviceConnectionJob != null) {
                        Scheduler.SharedInstance.notify_Finish(serviceConnectionJob.ID);
                }
                serviceConnectionJob = new ServiceConnectionJob(this);

                Scheduler.SharedInstance.AddJob(serviceConnectionJob);

                }


        public UsbDevice GetUSBMicDeviceForRegistration()
        {
                return  null;
//                return   ArticulationManager.Instance.GetUSBMicDevice();
        }
        //End of UIMAINConvey

        //ShutDownRequest
        public void ShutDownRequested()
        {}
        //End ShutDownRequest

        //Kinetics ApplicationstateChanged
        public   void WentBackground() {
                StopQASession(true);
                if(animationIDLEjob != null)
                        Scheduler.SharedInstance.KillJobWithID(animationIDLEjob.ID);
        }

        public   void CameForeground() {
        }
        //End ApplicationstateChanged


        //ServiceConnectionConvey
        public  void  ServiceConnectionJobStatus(Boolean Status)
        {
        }

        public  void  ServiceBindStatusConvey(String Status)
        {
                try {
                        BIND_STATES.STATES state = BIND_STATES.STATES.valueOf(Status);
                        switch(state) {

                                case MACHINE_BINDED: {
                                        if(CurrentBindState == BIND_STATES.STATES.NA) {

                                                ResetJobQue();
                                                EnableAllSessionListeners();
                                                UIMAINModuleHandler.Instance.InitUIHandler.MachineLoadAni();
                                                if (!StudioSessionHandler.Instance.IsRunning())
                                                        StudioSessionHandler.Instance.StudioSessionRequest();

                                                MachineConfig.Instance = KineticComms.Instance.GetMachineConfig();
                                                SetCurrentBindState(BIND_STATES.STATES.SLEEP);
                                        }
                                        else {
                                                SetCurrentBindState(state);
                                                if (BindCompletedAction != null) {
                                                        BindCompletedAction.func();
                                                }
                                        }
                                        break;
                                }
                                case WAIT_MACHINE_BIND: {
                                        if(CurrentBindState == BIND_STATES.STATES.NA) {

                                                ResetJobQue();
                                                EnableAllSessionListeners();
                                                UIMAINModuleHandler.Instance.InitUIHandler.MachineLoadAni();
                                                if (!StudioSessionHandler.Instance.IsRunning())
                                                        StudioSessionHandler.Instance.StudioSessionRequest();

                                                MachineConfig.Instance = KineticComms.Instance.GetMachineConfig();
                                        }
                                        SetCurrentBindState(BIND_STATES.STATES.SLEEP);
                                        break;
                                }
                                default:
                                        break;

                        }
                }
                catch (Exception e)
                {
                        Log.e("AniB SystemManager", e.getMessage());
                }
        }
        //End of ServiceConnectionConvey







        //QASessionStartStopListener
        public void StartQASession() {
        if(CurrentBindState == BIND_STATES.STATES.IDLE ||
                CurrentBindState == BIND_STATES.STATES.SLEEP)
                {
                        KineticComms.Instance.IndicateAttention();
                        ArticulationManager.Instance.StartListeningToUser();
                        //ArticulationManager.Instance.StartWakeWordDetection();
                        WakeWordDetected();
                        //MQTTInterfaceImplementation.Instance.ConnectToBroker();
                }
        }

        public void StopQASession(boolean IsAppInactive) {


//        if(CurrentBindState == BIND_STATES.STATES.IDLE||
//                CurrentBindState == BIND_STATES.STATES.SLEEP)
//        {
//                ArticulationManager.Instance.StopWakeWordDetection();
//        }
//        else
//                if(CurrentBindState == BIND_STATES.STATES.QA)
//                {
//                        ArticulationManager.Instance.StopRecognition();
//                }
                ArticulationManager.Instance.StopListening();
                ArticulationManager.Instance.StopRecognition();
                if(QAAnswerJob != null){
                        Scheduler.SharedInstance.KillJobWithID( QAAnswerJob.ID);
                }

                if(QAListenJob != null){
                        Scheduler.SharedInstance.KillJobWithID( QAListenJob.ID);
                }

                if(QAIdleJob != null) {
                        Scheduler.SharedInstance.KillJobWithID(QAIdleJob.ID);
                }

                SetCurrentBindState(BIND_STATES.STATES.IDLE);

                if(animationIDLEjob != null) {
                        Scheduler.SharedInstance.KillJobWithID(animationIDLEjob.ID);
                }

                if(!IsAppInactive) {
                        animationIDLEjob = new IDLEAnimationJob(this);
                        Scheduler.SharedInstance.AddJob(animationIDLEjob);
                }


                MQTTInterfaceImplementation.Instance.DisconnectFromBroker();
                KineticComms.Instance.IndicateNoAttention();
        }
//End of QASessionStartStopListener


//SynthesizerConvey
public void FinishedSynthesis() {
//        CurrentBindState = BIND_STATES.QA
//        ArticulationManager.Instance.StartListeningToUser()
        }
//End of SynthesizerConvey


//QAAIServerConvey
public void AnimatableResponse(String intent, Float Confidence, String response, String json) {

        Log.d("SPEAKER STAT", "QA AI Recieved response : "+response);
        Log.d("SPEAKER STAT", "QA AI Recieved json : "+json);
        if(CurrentBindState == BIND_STATES.STATES.QA)
        {

        if(QAAnswerJob != null)
        {
        Scheduler.SharedInstance.KillJobWithID( QAAnswerJob.ID);
        }

        String responseString = response;
        if(Confidence > 0.8)
        {
           switch (intent)
           {

                   case "DUSTERMYROOM":
                           AIManager.Instance.SendIntentRequest(IOTIntents.DUSTERMYROOM.toString(), "EMPTY");
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "DUSTERSTOP":
                           AIManager.Instance.SendIntentRequest(IOTIntents.DUSTERSTOP.toString(), "EMPTY");
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "3DPRINTERSTART":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.PRINTERSTART3D);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "3DPRINTERSTOP":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.PRINTERSTOP3D);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "3DPRINTERRESET":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.PRINTERRESET3D);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "LIGHTOFF":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.LIGHTOFF);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "LIGHTON":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.LIGHTON);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
                   case "TAGTOWAV":
                           MQTTInterfaceImplementation.Instance.PublishIntentToIOT(IOTIntents.TAGTOWAV);
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob(QAAnswerJob);
                           break;
//                   case "LISTENOFF":
//                           ListeningIDLETimeout();
//                           break;
                   case "ZERODOC":
                           AppletIntent = AppletIntents.Intents.ZERODOC;
                           LoadApplet(AppletIntents.Intents.ZERODOC);
                           break;
                   case "KITCHENLOOP":
                           AppletIntent = AppletIntents.Intents.KITCHENLOOP;

                           Thread thread = new Thread(new Runnable() {
                           @Override
                           public void run() {
                                   try  {
                                           BotConnectImplementation.Instance.SendLinkRequest(LINK_ANCHORS.NAVIGATION, "KITCHEN LOOP");
                                   } catch (Exception e) {
                                           e.printStackTrace();
                                   }
                                }
                           });

                           thread.start();

                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob(QAAnswerJob);


                           break;
                   default:

                           Log.d("SPEAKER STAT", "QA AI Recieved null intent.");
                           QAAnswerJob = new QAResponseJob(responseString,  json, this);
                           Scheduler.SharedInstance.AddJob( QAAnswerJob);
                           break;
           }

        }
        else
        {
                Log.d("SPEAKER STAT", "QA AI Recieved null intent.");
            QAAnswerJob = new QAResponseJob(responseString,  json, this);
            Scheduler.SharedInstance.AddJob( QAAnswerJob);
        }

        }
        }
//End of QAAIServerConvey

//QAResponseJobConvey
public void QAAnimationFinished() {

                Scheduler.SharedInstance.ScheduleNextJOB();

                if(AppletIntent == AppletIntents.Intents.KITCHENLOOP)
                {
                        AppletIntent = AppletIntents.Intents.NA;

                        if(QASessionHandler.Instance.IsListening()) {
                                QASessionHandler.Instance.QASessionRequest();
                        }
                }
        }
//End of QAResponseJobConvey

//QAListenerJobConvey
public void QAListenerAnimationFinished()
{
        Scheduler.SharedInstance.ScheduleNextJOB();
}
//End of QAListenerJobConvey

//ArticulationConvey
public void NotifyOnSpeechDetect()
{ Log.d("SPEAKER STAT", "VOICE reached : "+CurrentBindState.toString());
        if(CurrentBindState == BIND_STATES.STATES.QA) {
                if (QAListenJob != null) {
                        Log.d("SPEAKER STAT", "QA Lsiten not null");
                        QAListenJob.requestToDoAnimation();
                }
        }
}

public void TextArticulationBegan(String data) {
        Log.d("SPEAKER STAT", "USER VOICE Began : "+CurrentBindState.toString());
        if(CurrentBindState == BIND_STATES.STATES.QA) {
                if (QAListenJob != null) {
                        Scheduler.SharedInstance.KillJobWithID(QAListenJob.ID);
                }
                QAListenJob = new QAListeningAnimationJob(this);
                Scheduler.SharedInstance.AddJob(QAListenJob);
                Log.d("SPEAKER STAT", "QA Lsiten added");
        }
        }

public void TextBeingArticulatedByUser(String data) {
        if (CurrentBindState == BIND_STATES.STATES.QA)
        {
                if (data != null && !data.equals("")) {
                        if (QAListenJob != null) {
                                QAListenJob.requestToDoAnimation();
                        }
                }
        }
        }

public void TextArticulationFinishedByUser(String data) {
        Log.d("SPEAKER STAT", "USER VOICE finished.");
        if(CurrentBindState == BIND_STATES.STATES.QA) {
                if (data != null && !data.equals("")) {
                        Log.d("SPEAKER STAT", "QA AI Request text"+data);
                        AIManager.Instance.GetAIQAObject(data);
                }
        }
        }



        //ListeningIDLETimeout();
public void ListeningIDLETimeout() {
        Log.d("SPEEXT", "Timer Timeout reached");

        ArticulationManager.Instance.StopRecognition();

        if(CurrentBindState == BIND_STATES.STATES.QA)
        {
        if(QAListenJob != null)
        {
        Scheduler.SharedInstance.KillJobWithID( QAListenJob.ID);
        }
        Scheduler.SharedInstance.KillJobWithID( QAIdleJob.ID);

        SetCurrentBindState( BIND_STATES.STATES.IDLE);
        Scheduler.SharedInstance.ScheduleNextJOB();

//        ArticulationManager.Instance.StartWakeWordDetection();
        }
        }


public void StoppedListeningToUser() {
}

public void ListeningToUserNow() {
        }

public Boolean ShouldContinueListeningForFullSentence() {
        return true;
        }

class initQAJob implements GenericTypeDelegate {
    public void func() {
        SetCurrentBindState( BIND_STATES.STATES.QA);
        QAIdleJob = new QAIDLEAnimationJob();
        Scheduler.SharedInstance.AddJob(QAIdleJob);
        ArticulationManager.Instance.StartRecognition();
    }
}



//UIChoreogramConvey
public void ChoreogramFinished() {
        if (CurrentBindState == BIND_STATES.STATES.STUDIO_SESSION) {
                if (choreogramJob != null) {
                        Scheduler.SharedInstance.KillJobWithID(choreogramJob.ID);
                }
                SetCurrentBindState(BIND_STATES.STATES.IDLE);
                Scheduler.SharedInstance.ScheduleNextJOB();
        }
}
public void ProgressUpdated(int progress){

}
//End of UIChoreogramConvey

class initChoreogramJob implements GenericTypeDelegate {

        String audioFile;
        int StartSec;
        int EndSec;
        ArrayList<BeatsType> beats;

        public  initChoreogramJob(String _audioFile, int _StartSec, int _EndSec, ArrayList<BeatsType> _beats)
        {
                audioFile = _audioFile;
                StartSec = _StartSec;
                EndSec = _EndSec;
                beats = _beats;
        }

        public void func() {
                SetCurrentBindState( BIND_STATES.STATES.STUDIO_SESSION);
                if(choreogramJob != null)
                {
                        Scheduler.SharedInstance.KillJobWithID( choreogramJob.ID);
                }
                choreogramJob = new ChoreogramJob();
                Scheduler.SharedInstance.AddJob(choreogramJob);
                choreogramJob.requestToDoAnimation(audioFile, StartSec, EndSec, beats);
        }
}

public void   WakeWordDetected()
{
      ArticulationManager.Instance.StopWakeWordDetection();
      BindBeforeRunJob( new initQAJob());
}
//End of ArticulationConvey

    //BotConnectConvey
    public Boolean RunChoreogram(String audioFile, int StartSec, int EndSec, ArrayList<BeatsType> beat)
    {
        if(CurrentBindState != BIND_STATES.STATES.IDLE ||
                CurrentBindState != BIND_STATES.STATES.SLEEP)
                return  false;

        BindBeforeRunJob(new initChoreogramJob(audioFile, StartSec, EndSec, beat));

        return  true;
    }

    public void  BotConnected() {}
    public void  BotDisconnected() {}

    public void  BotLowStorage() {}
    public void  BotError(BotConnectionInfo Error) {}

    public void  BotStream(Map.Entry<LINK_ANCHORS, String> Data){
        LinkAnchorStreamHandler.instance.StreamData(Data);
    }

    public void LinkStreamStarted(LINK_ANCHORS anchor) {
    }

    public void  LinkDiscovered(LINK_ANCHORS anchor) {
            switch (anchor)
            {
                    case JOY: {
//                            String Data = BotConnectImplementation.Instance.GetLinkData(anchor);
//                            connect(Data, 0x1113);
                            break;
                    }
                    case HEART_MONITOR:
                            return;
                    case HEART_BEAT:
                            break;
                    case NAVIGATION:
                            break;
                    case NA:
                            return;
            }

        LinkAnchorList.add(anchor);

        if(CurrentBindState == BIND_STATES.STATES.IDLE ||
                CurrentBindState == BIND_STATES.STATES.SLEEP) {
                AppletIntent = AppletIntents.Intents.ACCESSORY_CONNECTED;
                LoadApplet(AppletIntents.Intents.ACCESSORY_CONNECTED);
        }
    }

    public void  BrokerConenctionChanged(Boolean Status)
    {}
    //BotConnectConvey



public static SystemFlowManager Instance = new SystemFlowManager();

private SystemFlowManager()
{
//            DB_Local_Store Db = new DB_Local_Store();
   // Db.RemoveDB();
//            Db.PlaceDB();
//            Db.InitializeDB();
KineticRemoteRequestHandler.Instance.set_ShutDownConvey(this);

        MachinePositionContext.Instance.Initialize();
// ArticulationManager.Instance.InitializeArticulation(UIMAINModuleHandler.Instance.AniUIHandler.GetSpeechRequestActivity());
}

private void ResetAllSessionListeners()
{
        SetCurrentBindState(BIND_STATES.STATES.NA);
        KineticServiceStateHandler.Instance.setNotify_ServiceConnectionConvey(this);
        ArticulationManager.Instance.ResetAllSessions();
        QASessionHandler.Instance.ResetSession();
}


private void EnableAllSessionListeners()
        {
        ArticulationManager.Instance.setOnArticulationListener(this);
        ArticulationManager.Instance.setOnSynthesizerListener(this);

        QASessionHandler.Instance.notify_OnQASessionRequest(this);
        QASessionHandler.Instance.notifyOnAIServerData(this);

        StudioSessionHandler.Instance.notify_OnStudioSessionRequest(this);

        UIChoreogramHandler.Instance.setNotifyOnConvey(this);

        }

        public void ResetJobQue()
        {
                if(animationIDLEjob != null) {
                        Scheduler.SharedInstance.KillJobWithID(animationIDLEjob.ID);
                        animationIDLEjob.Views.clear();
                }
                if(QAIdleJob != null) {
                        Scheduler.SharedInstance.KillJobWithID(QAIdleJob.ID);
                        QAIdleJob.Views.clear();
                }
                if(QAAnswerJob != null){
                        Scheduler.SharedInstance.KillJobWithID(QAAnswerJob.ID);
                        QAAnswerJob.Views.clear();
                }
                if(QAListenJob != null){
                        Scheduler.SharedInstance.KillJobWithID(QAListenJob.ID);
                        QAListenJob.Views.clear();
                }
                if(choreogramJob != null){
                        Scheduler.SharedInstance.KillJobWithID(choreogramJob.ID);
                        choreogramJob.Views.clear();
                }
        }

//AniUIConvey
public void AniStarted(ArrayList<View> elements) {
        AppletIntent = AppletIntents.Intents.GENERIC;
        MachinePositionContext.Instance.removeGraphicElements();
//        if(AnimationEngine.Views != null)
//        AnimationEngine.Views.clear();
//        if(AnimationEngine.Motors != null)
//        AnimationEngine.Motors.clear();

        ResetJobQue();

        for(View view : elements)
        {
                AnimationActionCreator.instance.updateDefaultState( view);
        }

//        if(CurrentBindState == BIND_STATES.STATES.IDLE ||
//                CurrentBindState == BIND_STATES.STATES.SLEEP )
//        {
//
//                Scheduler.SharedInstance.ScheduleNextJOB();
//        }
//        else


//        if(CurrentBindState == BIND_STATES.STATES.IDLE ||
//                CurrentBindState == BIND_STATES.STATES.SLEEP ||
//                CurrentBindState == BIND_STATES.STATES.READ_CALIB_FINISH)
//        {

        animationIDLEjob = new IDLEAnimationJob(this);
        Scheduler.SharedInstance.AddJob(animationIDLEjob);


//        }
//        else
//        {
//                //Restore and Resume all pending jobs
//                Scheduler.SharedInstance.RestorePendingJobsFromStoreBuffer();
//        }


                SetCurrentBindState( BIND_STATES.STATES.IDLE);



        }


class doIdleJob implements GenericTypeDelegate {
        public void func() {
                SetCurrentBindState( BIND_STATES.STATES.IDLE);
               Scheduler.SharedInstance.ScheduleNextJOB();
        }
        }

public  void JoySnapDetected() {
       QASessionHandler.Instance.QASessionRequest();
       if(QASessionHandler.Instance.IsListening())
               WakeWordDetected();
}

public  void JoyTriggerDetected()
{
//    StopQASession();
}
//End of AniUIConvey


//AppletJobConvey
public void EventAnimationFinished() {
        UIMAINModuleHandler.Instance.AniUIHandler.MachineLoadApplet();
}
//End of AppletJobConvey

//AppletUIConvey
public  void AppletLoaded()
{
        switch (AppletIntent)
        {
                case ZERODOC: {

                        Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                        try  {
                                                BotConnectImplementation.Instance.StartLinkStream(LINK_ANCHORS.HEART_MONITOR, null);
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                        }
                                }
                        });

                        thread.start();

                        if (eventJob != null) {
                                Scheduler.SharedInstance.KillJobWithID(eventJob.ID);
                                eventJob = null;
                        }
                        CurrentBindState = BIND_STATES.STATES.IDLE;
                        BindBeforeRunJob(new initDynamicBeatJob("ZERODOC_Dyn_Beat", null));

                        break;
                }
                case ACCESSORY_CONNECTED: {
                        UIMAINModuleHandler.Instance.AppletUIHandler.AccessoryConnected(LinkAnchorList);
                        LinkAnchorList.clear();
                        break;
                }
        }

}
public  void UserRaisedTriggerEvent()
{
        if(CurrentBindState == BIND_STATES.STATES.APPLET) {
                switch (AppletIntent)
                {
                        case ZERODOC: {
                                if(dynamicBeatJob != null)
                                {
                                        dynamicBeatJob.UserTriggerNotify();
                                }
                                break;
                        }
                        default:
                                break;
                }
        }
}
public  void CloseAppletRequested()
{
        if(CurrentBindState == BIND_STATES.STATES.APPLET) {

                if (eventJob != null) {
                        Scheduler.SharedInstance.KillJobWithID(eventJob.ID);
                }

                switch (AppletIntent) {
                        case ZERODOC: {
                                Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                                try  {
                                                        BotConnectImplementation.Instance.StopLinkStream(LINK_ANCHORS.HEART_MONITOR);
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                });

                                thread.start();

                                if (dynamicBeatJob != null) {
                                        dynamicBeatJob.StopConsultationRecording();
                                        Scheduler.SharedInstance.KillJobWithID(dynamicBeatJob.ID);
                                }
                                break;
                        }
                }

                UIMAINModuleHandler.Instance.AppletUIHandler.CloseApplet();

        }
}

public  void AppletLoaded(AppletIntents Intent)
{

}


public  void AppletLoadedURL(String URL)
{
        if(!URL.equals("about:blank")) {

        }
}

public  void TriggerEventDetected()
{
        
}
//End of AppletUIConvey

class initDynamicBeatJob implements GenericTypeDelegate {

        public String AppletName = "APPLET";
        public AppletJobConvey appletJobConvey;

        public initDynamicBeatJob(String Name, AppletJobConvey JobConvey) {
                AppletName = Name;
                appletJobConvey = JobConvey;
        }

        public void func() {
                SetCurrentBindState( BIND_STATES.STATES.APPLET);
                dynamicBeatJob = new DynamicBeatJob(EventJob.Events.PORTRAIT_OnLoad.name());
                Scheduler.SharedInstance.AddJob(dynamicBeatJob);
        }
}

class initAppletJob implements GenericTypeDelegate {

        public AppletIntents.Intents AppletName = AppletIntents.Intents.GENERIC;
        public AppletJobConvey appletJobConvey;

        public initAppletJob(AppletIntents.Intents Name, AppletJobConvey JobConvey) {
                AppletName = Name;
                appletJobConvey = JobConvey;
        }

        public void func() {
                SetCurrentBindState( BIND_STATES.STATES.APPLET);
                eventJob = new EventJob(AppletName, EventJob.Events.PORTRAIT_OnLoad, appletJobConvey);
                Scheduler.SharedInstance.AddJob(eventJob);
        }
}

AppletIntents.Intents AppletIntent = AppletIntents.Intents.GENERIC;
ArrayList<LINK_ANCHORS> LinkAnchorList = new ArrayList<LINK_ANCHORS>();

public  AppletIntents.Intents GetAppletIntent()
{
      return  AppletIntent;
}

public  void LoadApplet(AppletIntents.Intents Name)
{
        if(QASessionHandler.Instance.IsListening()) {
                QASessionHandler.Instance.QASessionRequest();
        }
        UIMAINModuleHandler.Instance.AniUIHandler.ShowBlankScreen();
        BindBeforeRunJob(new initAppletJob(Name,  this));
}

        }


