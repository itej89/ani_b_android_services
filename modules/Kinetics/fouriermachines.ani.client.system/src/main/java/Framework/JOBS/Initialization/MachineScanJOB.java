package Framework.JOBS.Initialization;

import java.util.Timer;
import java.util.TimerTask;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.CommsStatusConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Machine;
import Framework.SystemEventHandlers.KineticCommsStateHandler;

public class MachineScanJOB extends Job implements KineticsCommsConvey {
    CommsStatusConvey commsConvey;



    //ScanTimer On Tick
    void ScanTimer_onTick() {
        StopScanTiemr();
        KineticComms.Instance.StartScan();
    }
//End ScanTimer OnTick


    //Kinetics CommansConvey
    public void commsStateChanged(MachineCommsStates State) {
        switch (State) {
            case POWERED_OFF:

                break;
            case READY_TO_SCAN:

                StartScanTimer();
                break;
            default:
                break;
        }

        commsConvey.commsStateChanged(State);
    }

    public void newMachineFound(Machine Device) {
        commsConvey.newMachineFound(Device);
    }
//End inetics COmmansConvey


    public MachineScanJOB(CommsStatusConvey _delegate) {
        super();
        commsConvey = _delegate;

        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
    }

    Timer ScanTimer;

        void StartScanTimer()
        {
            ScanTimer = new Timer();
        ScanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScanTimer_onTick();
            }
        }, 1000);
        }
    void StopScanTiemr()
        {
        if(ScanTimer != null){
        ScanTimer.cancel();
        }
        }



//Job Override
    @Override
public  void TakeOverResources(JobConvey _delegate) {
        super.TakeOverResources( _delegate);

        StopScanTiemr();
        KineticComms.Instance.SetCommsDelegate(this);
        KineticComms.Instance.InitializeComms();
        }
    @Override
public  void Pause() {

        KineticComms.Instance.SetCommsDelegate( KineticCommsStateHandler.Instance);
        KineticComms.Instance.StopScan();
        if(super.delegate != null){
        super.delegate.notify_Finish( ID);
        }
        }
    @Override
public  void Resume() {

        }
        //EndJob override

        }
