package Framework.JOBS;

import android.os.Handler;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import Framework.JOBS.Initialization.MachineBindJOB.MachineBindStatusDelegate;
import Framework.SystemEventHandlers.UIMAINModuleHandler;

public class PowerOffJob extends Job implements KineticsResponseConvey
        {
            ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
        ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();
            ArrayList<KineticsRequest> KineticsTurnRequest = new ArrayList<KineticsRequest>();

public void CommsLost() {
        super.delegate.notify_LostResource( ID);
        }

public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {

        }

public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {
        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
        Pause();
        }
        }

public  PowerOffJob() {
        super();
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
        }

public PowerOffJob(MachineBindStatusDelegate bindStateConvey) {
        super();
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
        }

//Job Override
            @Override
public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources( _delegate);
        }

public void Pause() {
        KineticComms.Instance.SetKineticsResposeListener(null);

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {

            KineticComms.Instance.DisconnectMachine();

        }
    }, 3000);





        super.Pause();
        super.delegate.notify_Finish( ID);
        }

public void Resume() {
        WaitingForKineticsRequestAck = KineticComms.Instance.TurnOffPower(GlobalContext.context.getPackageName());
        }
        //End Job Override
        }
