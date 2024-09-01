package Framework.JOBS;

import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.ActuatorAttachStates;
import Framework.DataTypes.Constants.ActuatorDetachStates;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

public class DettachActuatorJob extends Job implements KineticsResponseConvey
{


    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();

    ArrayList<KineticsRequest> kineticsRequest = new ArrayList<KineticsRequest>();

    Actuator actuatorType;

    //KineticsResponseConvey delegate
    public void CommsLost() {

        super.delegate.notify_LostResource( ID);
    }

    public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {

        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
            WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
            LastKineticsResponse = responeData;
            super.delegate.notify_NextStep( ID);
        }
    }
    //End KineticsResponseConvey delegate


    ActuatorDetachStates.STATES CURRENT_STATE = ActuatorDetachStates.STATES.NA;
    public DettachActuatorJob(Actuator actuator) {
        super();
        actuatorType = actuator;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = ActuatorDetachStates.STATES.PING;
    }
    @Override
    public void Pause() {
        KineticComms.Instance.SetKineticsResposeListener( null);
        KineticComms.Instance.ResetCommsContext();

        super.Pause();
        super.delegate.notify_Finish( ID);
    }
    @Override
    public void Resume() {
        DOSTEP();
    }
//End Job Override

    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
        //Retry if any communication fail
        CURRENT_STATE = ActuatorDetachStates.STATES.PING;
        DOSTEP();
    }

    void DOSTEP()
    {
        DetachActuator();
    }

    void DetachActuator()
    {
        switch(CURRENT_STATE) {

            case PING: {
                CURRENT_STATE = ActuatorDetachStates.STATES.VALIDATE_PING;
                WaitingForKineticsRequestAck = KineticComms.Instance.StartMotion();
                break;
            }
            case VALIDATE_PING: {
                if (LastKineticsResponse.size() == 1 && LastKineticsResponse.get(0).ResponseType == CommandLabels.CommandTypes.TRG) {
                    CURRENT_STATE = ActuatorDetachStates.STATES.DISCONNECT_Actuator;
                    super.delegate.notify_NextStep(ID);
                }
                break;
            }

            case DISCONNECT_Actuator: {
                CURRENT_STATE = ActuatorDetachStates.STATES.DETACH_Actuator;
                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(actuatorType);
                break;

            }
            case DETACH_Actuator: {
                    CURRENT_STATE = ActuatorDetachStates.STATES.Finish;
                    WaitingForKineticsRequestAck = KineticComms.Instance.DetachSignalToActuator(actuatorType);

                break;
            }
            case Finish:
                Pause();
                break;
            default:
                break;
        }

    }


}
