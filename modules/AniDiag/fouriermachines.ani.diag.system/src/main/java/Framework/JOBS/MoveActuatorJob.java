package Framework.JOBS;

import android.os.ParcelUuid;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.UUID;

import Framework.DataTypes.Constants.ActuatorAttachStates;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.SERVO_MOVE_STATES;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

public class MoveActuatorJob extends Job implements KineticsResponseConvey
{

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();

    ArrayList<KineticsRequest> kineticsRequest = new ArrayList<KineticsRequest>();

    Actuator actuatorType;
    Integer Angle;

    SERVO_MOVE_STATES.STATES CURRENT_STATE = SERVO_MOVE_STATES.STATES.MOVE_ACTUATOR;

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


    public MoveActuatorJob(Actuator actuator, Integer Degree) {
        super();
        actuatorType = actuator;
        Angle = Degree;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
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
        CURRENT_STATE = SERVO_MOVE_STATES.STATES.FINISH;
        DOSTEP();
    }

    void DOSTEP()
    {
        switch(CURRENT_STATE)
        {
            case MOVE_ACTUATOR:
                CURRENT_STATE = SERVO_MOVE_STATES.STATES.FINISH;
                ArrayList<KineticsRequest> kineticsRequest = new ArrayList<>();
                KineticsRequestAngle angleRequest = new KineticsRequestAngle((Angle), actuatorType);
                KineticsRequestTiming timingRequest = new KineticsRequestTiming(2000, actuatorType);
                KineticsRequestDelay delayRequest = new KineticsRequestDelay(0, actuatorType);
                KineticsRequestServoEasing easingRequest = new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, actuatorType);
                KineticsRequestEasingInOut inoutRequest = new KineticsRequestEasingInOut(CommandLabels.EasingType.IO, actuatorType);

                kineticsRequest.add(angleRequest);
                kineticsRequest.add(timingRequest);
                kineticsRequest.add(delayRequest);
                kineticsRequest.add(easingRequest);
                kineticsRequest.add(inoutRequest);
                kineticsRequest.add(new KineticsRequestTrigger());

                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(kineticsRequest);
                break;
            case FINISH:
                Pause();
                break;
        }

    }


}
