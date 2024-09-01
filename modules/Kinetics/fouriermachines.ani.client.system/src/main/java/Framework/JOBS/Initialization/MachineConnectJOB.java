package Framework.JOBS.Initialization;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.CommsStatusConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Delegates.KineticsCommsConvey;
import FrameworkInterface.PublicTypes.Machine;
import Framework.SystemEventHandlers.KineticCommsStateHandler;
import Framework.SystemEventHandlers.KineticRemoteRequestHandler;

public class MachineConnectJOB  extends Job implements KineticsCommsConvey
        {
            Machine machine;
            CommsStatusConvey commsConvey;

//Kinetics COmmansConvey
public void commsStateChanged(MachineCommsStates State) {
        switch(State) {
        case CONNECTED:
        Pause();
        break;
        default:
        break;
        }

        commsConvey.commsStateChanged( State);
        }

public void newMachineFound(Machine Device) {
        //no functionality for now
        }
//End inetics COmmansConvey



public MachineConnectJOB(Machine device, CommsStatusConvey delegate) {
        super();
        machine = device;
        commsConvey = delegate;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
        }



//Job Override
@Override
public  void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetCommsDelegate(this);
        super.TakeOverResources( _delegate);
        }

        @Override
public  void Pause() {
        KineticComms.Instance.SetCommsDelegate( KineticCommsStateHandler.Instance);
        KineticComms.Instance.SetMachineRemoteRequestListener( KineticRemoteRequestHandler.Instance);
        super.delegate.notify_Finish( ID);
        }
            @Override
public  void Resume() {

        KineticComms.Instance.ConnectToMachine( machine);
        }
        //End Job Override

        }

