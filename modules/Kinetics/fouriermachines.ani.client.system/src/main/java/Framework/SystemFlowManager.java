package Framework;

import android.hardware.usb.UsbDevice;

import Framework.DataTypes.Constants.APP_STATE;
import Framework.DataTypes.Delegates.CommsStatusConvey;
import Framework.DataTypes.Delegates.Generic.GenericTypeDelegate;
import Framework.DataTypes.Delegates.ServiceConnectionBindRequestConvey;
import Framework.DataTypes.Delegates.ServiceConnectionEstablishedConvey;
import Framework.DataTypes.Delegates.ShutDownRequest;
import Framework.DataTypes.Delegates.UI.TestTriggerConvey;
import Framework.DataTypes.Delegates.UI.UIMAINConvey;
import Framework.JOBS.Initialization.MachineReadCalibJOB.MachineReadCalibJOB;
import Framework.JOBS.Initialization.MachineReadCalibJOB.MachineReadCalibStatusDelegate;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Machine;
import Framework.JOBS.Initialization.MachineBindJOB.BIND_STATES;
import Framework.JOBS.Initialization.MachineBindJOB.MachineBindJOB;
import Framework.JOBS.Initialization.MachineBindJOB.MachineBindStatusDelegate;
import Framework.JOBS.Initialization.MachineConnectJOB;
import Framework.JOBS.Initialization.MachineScanJOB;
import Framework.JOBS.PowerOffJob;
import Framework.SystemEventHandlers.ApplicationStateHandler;
import Framework.SystemEventHandlers.KineticCommsStateHandler;
import Framework.SystemEventHandlers.KineticRemoteRequestHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;

public class SystemFlowManager implements
        CommsStatusConvey, MachineBindStatusDelegate, MachineReadCalibStatusDelegate,
        UIMAINConvey, TestTriggerConvey, ShutDownRequest, ServiceConnectionEstablishedConvey, ServiceConnectionBindRequestConvey
        {

SystemFlowManager getOuterObject()
{
    return  SystemFlowManager.this;
}

            GenericTypeDelegate BindCompletedAction;



//TestTriggerUIConvey
public void TestConnectToStudio() {
        EndCurrentJob();
//        StartStudioSession();
        }

public void TestDisconnectStudio() {
//        StopStudioSession();
        }
        //End of TestTriggerUIConvey


        MachineScanJOB Scanjob;
        MachineConnectJOB Connectjob;
        MachineReadCalibJOB ReadMachineCalibJOB;
        MachineBindJOB Bindjob;

        PowerOffJob ShutdownJob;

        BIND_STATES.STATES CurrentBindState = BIND_STATES.STATES.NA;

        Boolean IsPowerOffRequested = false;


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
        UIMAINModuleHandler.Instance.InitUIHandler.ClearScanDeatils();
        }

        void RestartInitialization()
        {

                IsPowerOffRequested = false;

                //Stop listening to any Session events for Ani eg: QA Session, Studio session
                ResetAllSessionListeners();

                UIMAINModuleHandler.Instance.InitUIHandler.ResetInitializing();

                if(KineticComms.Instance.IsConnectedToMachine()){
                      StartReadCalibrationJOB();
                }
                else
                {
                StartScanJOB();
                }
        }



        void StartScanJOB()
        {
                if(Scanjob != null && Scheduler.SharedInstance.FindJobByID(Scanjob.ID) != null)
                {
                        Scheduler.SharedInstance.KillJobWithID(Scanjob.ID);
                }

                Scanjob  = new MachineScanJOB(this);
        if(Scheduler.SharedInstance.AddJob( Scanjob)){
        UIMAINModuleHandler.Instance.InitUIHandler.Scanning();
        }
        }

        void StartConnectJOB(Machine machine)
        {
                if(Connectjob != null && Scheduler.SharedInstance.FindJobByID(Connectjob.ID) != null)
                {
                        Scheduler.SharedInstance.KillJobWithID(Connectjob.ID);
                }

        PauseCurrentJOB();
        Connectjob  = new MachineConnectJOB(machine, this);
        Scheduler.SharedInstance.AddJob(Connectjob);
        }

        void StartReadCalibrationJOB()
        {
                if(Bindjob != null && Scheduler.SharedInstance.FindJobByID(Bindjob.ID) != null)
                {
                        Scheduler.SharedInstance.KillJobWithID(Bindjob.ID);
                }

                if(ReadMachineCalibJOB != null && Scheduler.SharedInstance.FindJobByID(ReadMachineCalibJOB.ID) != null)
                {
                        Scheduler.SharedInstance.KillJobWithID(ReadMachineCalibJOB.ID);
                }

                //The "MachineConnectJOB" makes the KineticCommsStateHandler to listen for comms state changes
                //So initilizer screen needs to listen for any comms state notifications from KineticCommsStateHandler
                KineticCommsStateHandler.Instance.SetCommsConvey(this);

                UIMAINModuleHandler.Instance.InitUIHandler.ClearScanDeatils();
                ReadMachineCalibJOB  = new MachineReadCalibJOB(this);
                if(Scheduler.SharedInstance.AddJob(ReadMachineCalibJOB)) {
                        UIMAINModuleHandler.Instance.InitUIHandler.Binding();
                }
        }



        void StartBindJOB()
        {
                if(Bindjob != null && Scheduler.SharedInstance.FindJobByID(Bindjob.ID) != null)
                {
                        Scheduler.SharedInstance.KillJobWithID(Bindjob.ID);
                }
                Bindjob  = new MachineBindJOB(this);
                Scheduler.SharedInstance.AddJob(Bindjob);
        }

        public boolean IsInitialized()
        {
                if(CurrentBindState == BIND_STATES.STATES.WAIT_MACHINE_BIND ||
                CurrentBindState == BIND_STATES.STATES.MACHINE_BINDED)
                        return true;
                else
                        return false;
        }

        //ServiceConnectionBindRequestConvey
        public void BindMachine()
        {
                if(Bindjob != null && Scheduler.SharedInstance.FindJobByID(Bindjob.ID) != null){
                        Bindjob.BindActuatorSignal();
                        return;
                }
        }

        public void UnBindMachine()
        {
                StartBindJOB();
        }
        //End of ServiceConnectionBindRequestConvey


        //IDLEAnimDelegate
        public void IDLEAnimationStarted()
        {
                SetCurrentBindState(BIND_STATES.STATES.IDLE);
        }
        public void IDLEAnimationFinished()
        {
                StartBindJOB();
        }
        //End of  IDLEAnimDelegate

        public void ServiceConnectionEstablished()
        {
                if(IsInitialized())
                {
                        UIMAINModuleHandler.Instance.InitUIHandler.MachineLoadKineticsService();
                        KineticComms.Instance.ConveyBindStatus(CurrentBindState.toString());
                        return;
                }
                RestartInitialization();
        }


        //UIMAINConvey
        public void AppStarted() {

        }

        public void UserTurnedOnBluetooth() {
                RestartInitialization();
                }

        public void UserSelectedAMachine(Machine Device)
        {
          StartConnectJOB( Device);
        }

        public  UsbDevice GetUSBSerialDevice()
        {
                return KineticComms.Instance.GetUSBSerialDevice();
        }
        //End of UIMAINConvey





        //Kinetics ApplicationstateChanged
      public   void WentBackground() {

//        SetCurrentBindState( BIND_STATES.STATES.NA);

        //Stop listening to any Session events for Ani eg: QA Session, Studio session
//        ResetAllSessionListeners();

        // EndCurrentJob()

        //Push All pending Jobs to store buffer
//        Scheduler.SharedInstance.MoveAllJobsToStoreBuffer();

//        SetCurrentBindState( BIND_STATES.STATES.NA);

//        if(StartSystemPauseJOB())
//        {
//        UIMAINModuleHandler.Instance.InitUIHandler.PauseApplication();
//        }
        }



//Shutdown command recieved from Machine
//ShutDownRequest
public void ShutDownRequested() {

        }
        //End of ShutDownRequest

      public   void CameForeground() {
        EndCurrentJob();
        UIMAINModuleHandler.Instance.InitUIHandler.ResumeApplication();
        RestartInitialization();
        }
//End ApplicationstateChanged



//Kinetics COmmansConvey
public void commsStateChanged(MachineCommsStates State) {
        if(ApplicationStateHandler.Instance.CURRENT_SATE == APP_STATE.STATE.ACTIVE)
        {
        switch(State) {
        case POWERED_OFF:
        EndCurrentJob();
        UIMAINModuleHandler.Instance.InitUIHandler.MachinePoweredOff();
        break;
        case DISCONNECTED:
        UIMAINModuleHandler.Instance.InitUIHandler.MachineDisconnected();
        EndCurrentJob();
        RestartInitialization();
        break;
        case CONNECTION_TIMEOUT:
        UIMAINModuleHandler.Instance.InitUIHandler.MachineConnectionTimeout();
        EndCurrentJob();
        RestartInitialization();
        break;
        case CONNECTED:

        UIMAINModuleHandler.Instance.InitUIHandler.MachineConected();
                StartReadCalibrationJOB();
        break;
default:
        break;
        }
        }
        }


public void newMachineFound(Machine Device) {
        UIMAINModuleHandler.Instance.InitUIHandler.NewMachineFound( Device);
        }
//End kinetics COmmansConvey


//MachineReadCalibStatusDelegate
public void ReadCalibStateChanged(Framework.JOBS.Initialization.MachineBindJOB.BIND_STATES.STATES _BindState){
        switch(_BindState) {
                case READ_CALIB_FINISH:

                        SetCurrentBindState( _BindState);

                        EnableAllSessionListeners();

                        StartBindJOB();

                        break;
        }

}
//End //MachineReadCalibStatusDelegate

//MachineBindStatus changed
public void BindStateChanged(BIND_STATES.STATES _BindState)
        {

        switch(_BindState) {
        case MACHINE_BINDED:
                SetCurrentBindState( _BindState);
                KineticComms.Instance.ConveyBindStatus(_BindState.toString());
                if(BindCompletedAction != null)
                {
                    BindCompletedAction.func();
                }
                break;
        case WAIT_MACHINE_BIND:
                SetCurrentBindState(BIND_STATES.STATES.WAIT_MACHINE_BIND);
                KineticComms.Instance.ConveyBindStatus(_BindState.toString());
                UIMAINModuleHandler.Instance.InitUIHandler.MachineLoadKineticsService();
                break;
        default:
                break;
        }
        }
//End inetics CommansConvey



public static SystemFlowManager Instance = new SystemFlowManager();

private SystemFlowManager()
        {
        // DB_Local_Store Db = new DB_Local_Store();
        // Db.RemoveDB();
        // Db.PlaceDB();
        // Db.InitializeDB();
        KineticRemoteRequestHandler.Instance.set_ShutDownConvey(this);
        KineticComms.Instance.SetServiceConnectionEstablishedConveyListener(this);
        KineticComms.Instance.SetServiceConnectionBindRequestConveyListener(this);
//        MachinePositionContext.Instance.Initialize();
        }

private void ResetAllSessionListeners()
        {
        SetCurrentBindState(BIND_STATES.STATES.NA);
        }

private void EnableAllSessionListeners()
        {

        }


        }


