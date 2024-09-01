package Framework.JOBS.Initialization.MachineBindJOB;

import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAttachLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestInstantTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestLeftLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestPowerOnLockServo;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestRightLockServoAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

public class MachineBindJOB extends Job implements KineticsResponseConvey
        {

            boolean EnableMotion = true;
            boolean EnableVerticalMotion = true;

            ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
            ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();
            MachineBindStatusDelegate BindStateConvey;
            ArrayList<KineticsRequest> KineticsTurnRequest = new ArrayList<KineticsRequest>();
            ArrayList<KineticsRequest>  KineticsLeanRequest = new ArrayList<KineticsRequest>();
            ArrayList<KineticsRequest>  KineticsLiftRequest = new ArrayList<KineticsRequest>();
            ArrayList<KineticsRequest>  KineticsTiltRequest = new ArrayList<KineticsRequest>();

            private boolean MachineBindActuators = false;
            private ArrayList<Actuator> BindedActuatorList = new ArrayList<>();



//KineticsResponseConvey delegate
public void CommsLost() {

        super.delegate.notify_LostResource( ID);
        }

public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {

        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
        WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
        LastKineticsResponse = responeData;
        Log.i("MachineBindJOB", responeData.get(0).ResponseType.toString());
        super.delegate.notify_NextStep( ID);

        }
        }
        //End KineticsResponseConvey delegate


            BIND_STATES.STATES CURRENT_STATE = BIND_STATES.STATES.NA;
public MachineBindJOB(MachineBindStatusDelegate bindStateConvey) {
        super();
        BindStateConvey = bindStateConvey;
        PRIORITY = JOB_PRIORITY.USER_CRITICAL;
        BindStateConvey.BindStateChanged(BIND_STATES.STATES.NA);
        }

//Job Override
@Override
public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
        CURRENT_STATE = BIND_STATES.STATES.PING;
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

    public void BindActuatorSignal()
    {
        MachineBindActuators = true;
        if(CURRENT_STATE == BIND_STATES.STATES.WAIT_MACHINE_BIND)
        {
            CURRENT_STATE = BIND_STATES.STATES.IS_TURN_CONNECTED;
            super.delegate.notify_NextStep(ID);
        }
    }

    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
        //Retry if any communication fail
        CURRENT_STATE = BIND_STATES.STATES.PING;
        DOSTEP();
        }

        void DOSTEP()
        {
        switch(CURRENT_STATE) {

        case PING: {
                CURRENT_STATE = BIND_STATES.STATES.VALIDATE_PING;
                WaitingForKineticsRequestAck = KineticComms.Instance.StartMotion(GlobalContext.context.getPackageName());

                notify_BindState(BIND_STATES.STATES.PING);
                break;
        }
        case VALIDATE_PING: {
                if (LastKineticsResponse.size() == 1 && LastKineticsResponse.get(0).ResponseType == CommandLabels.CommandTypes.TRG) {
                        CURRENT_STATE = BIND_STATES.STATES.DETACH_TURN;
                        super.delegate.notify_NextStep(ID);
                }
                break;
        }
            case DETACH_TURN: {
                CURRENT_STATE = BIND_STATES.STATES.DETACH_LEAN;
                WaitingForKineticsRequestAck = KineticComms.Instance.DetachSignalToActuator(GlobalContext.context.getPackageName(), Actuator.TURN);
                break;
            }
            case DETACH_LEAN: {
                CURRENT_STATE = BIND_STATES.STATES.DETACH_LIFT;
                WaitingForKineticsRequestAck = KineticComms.Instance.DetachSignalToActuator(GlobalContext.context.getPackageName(), Actuator.LEAN);
                break;
            }
            case DETACH_LIFT: {
                CURRENT_STATE = BIND_STATES.STATES.DETACH_TILT;
                WaitingForKineticsRequestAck = KineticComms.Instance.DetachSignalToActuator(GlobalContext.context.getPackageName(), Actuator.LIFT);
                break;
            }
            case DETACH_TILT: {
                CURRENT_STATE = BIND_STATES.STATES.IS_TURN_CONNECTED;
                WaitingForKineticsRequestAck = KineticComms.Instance.DetachSignalToActuator(GlobalContext.context.getPackageName(), Actuator.TILT);
                break;
            }
        case IS_TURN_CONNECTED: {

            if(!BindedActuatorList.contains(Actuator.TURN))
            {
                CURRENT_STATE = BIND_STATES.STATES.CONNECT_TURN;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(GlobalContext.context.getPackageName(), Actuator.TURN);
                break;
            }
            else
            {
                CURRENT_STATE = BIND_STATES.STATES.IS_LEAN_CONNECTED;
                super.delegate.notify_NextStep(ID);
            }

                }
            case CONNECT_TURN: {
                if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse))
                {
                    CURRENT_STATE = BIND_STATES.STATES.IS_TURN_ATTACHED;
                    super.delegate.notify_NextStep(ID);
                } else {

                    CURRENT_STATE = BIND_STATES.STATES.IS_TURN_ATTACHED;
                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(GlobalContext.context.getPackageName(), Actuator.TURN);
                }


                break;
            }
        case IS_TURN_ATTACHED: {
                        CURRENT_STATE = BIND_STATES.STATES.DUMMY_READ_DEGREE_TURN;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(GlobalContext.context.getPackageName(), Actuator.TURN);
                break;
        }
        case DUMMY_READ_DEGREE_TURN: {
                        if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse)) {
                                KineticsTurnRequest.clear();
                                CURRENT_STATE = BIND_STATES.STATES.IS_LEAN_CONNECTED;
                                super.delegate.notify_NextStep(ID);
                        } else {
                                CURRENT_STATE = BIND_STATES.STATES.READ_DEGREE_TURN;
                                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.TURN);
                        }
                        break;
                }
        case READ_DEGREE_TURN: {
                        KineticsTurnRequest.addAll(new ArrayList<KineticsRequest>() {{
                                add(new KineticsRequestTiming(100, Actuator.TURN));
                                add(new KineticsRequestDelay(0, Actuator.TURN));
                        }});
                        CURRENT_STATE = BIND_STATES.STATES.SET_DEGREE_TURN;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.TURN);

                break;
        }
        case SET_DEGREE_TURN: {
                CURRENT_STATE = BIND_STATES.STATES.TRIGGER_TURN;

                final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(Actuator.TURN, LastKineticsResponse);

                if (request != null) {
                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.LIN, Actuator.TURN));
                                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.TURN));
                                        add(request);
                                }});
                }

                break;
        }

                case TRIGGER_TURN: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_TURN;

                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                        _TriggerAngles.addAll(KineticsTurnRequest);

                        if (_TriggerAngles.size() > 0) {
                                _TriggerAngles.add(new KineticsRequestTrigger());
                                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                        _TriggerAngles);
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }

                        break;
                }
                case TRIGGER_TURN_CONFIRM: {


                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_TURN;


                        new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
                                        _TriggerAngles.add(new KineticsRequestTrigger());
                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                                _TriggerAngles);

                                }
                        }, 100);




                        break;
                }

                case ATTACH_SERVO_TURN: {
                        CURRENT_STATE = BIND_STATES.STATES.IS_LEAN_CONNECTED;
                        if (KineticsTurnRequest.size() > 0) {

                            if(MachineBindActuators) {
                                if(EnableMotion) {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(GlobalContext.context.getPackageName()
                                        ,Actuator.TURN);
//                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
//                                            Actuator.TURN);
                                }
                                else
                                {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                            Actuator.TURN);
                                }
                                BindedActuatorList.add(Actuator.TURN);
                            }
                            else
                            {
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                        Actuator.TURN);
                            }
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }
                        break;

                }



                case IS_LEAN_CONNECTED: {
                    if(!BindedActuatorList.contains(Actuator.LEAN))
                    {
                        CURRENT_STATE = BIND_STATES.STATES.CONNECT_LEAN;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(GlobalContext.context.getPackageName(), Actuator.LEAN);
                        break;
                    }
                    else
                    {
                        CURRENT_STATE = BIND_STATES.STATES.IS_LIFT_CONNECTED;
                        super.delegate.notify_NextStep(ID);
                    }


                }
                case CONNECT_LEAN: {
                        if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse))
                        {
                                CURRENT_STATE = BIND_STATES.STATES.IS_LEAN_ATTACHED;
                                super.delegate.notify_NextStep(ID);
                        } else {

                                CURRENT_STATE = BIND_STATES.STATES.IS_LEAN_ATTACHED;
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(GlobalContext.context.getPackageName(), Actuator.LEAN);
                        }


                        break;
                }
                case IS_LEAN_ATTACHED: {
                        CURRENT_STATE = BIND_STATES.STATES.DUMMY_READ_DEGREE_LEAN;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(GlobalContext.context.getPackageName(), Actuator.LEAN);
                        break;
                }
        case DUMMY_READ_DEGREE_LEAN: {
                        if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse)) {
                                KineticsLeanRequest.clear();
                                CURRENT_STATE = BIND_STATES.STATES.IS_LIFT_CONNECTED;
                                super.delegate.notify_NextStep(ID);
                        }
                        else {
                                CURRENT_STATE = BIND_STATES.STATES.READ_DEGREE_LEAN;
                                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.LEAN);
                        }

                        break;
                }
        case READ_DEGREE_LEAN: {
                        KineticsLeanRequest.addAll(
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestTiming(100, Actuator.LEAN));
                                        add(new KineticsRequestDelay(0, Actuator.LEAN));
                                }});
                        CURRENT_STATE = BIND_STATES.STATES.SET_DEGREE_LEAN;

                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.LEAN);


                break;
        }
        case SET_DEGREE_LEAN: {
                CURRENT_STATE = BIND_STATES.STATES.TRIGGER_LEAN;

               final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(Actuator.LEAN, LastKineticsResponse);

                if (request != null) {
                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.LIN, Actuator.LEAN));
                                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LEAN));
                                        add(request);
                                }});
                }

                break;
        }
                case TRIGGER_LEAN: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_LEAN;

                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                        _TriggerAngles.addAll(KineticsLeanRequest);

                        if (_TriggerAngles.size() > 0) {
                                _TriggerAngles.add(new KineticsRequestTrigger());
                                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                        _TriggerAngles);
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }

                        break;
                }
                case TRIGGER_LEAN_CONFIRM: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_LEAN;

                        new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
                                        _TriggerAngles.add(new KineticsRequestTrigger());
                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                                _TriggerAngles);

                                }
                        }, 100);


                        break;
                }

                case ATTACH_SERVO_LEAN: {
                        CURRENT_STATE = BIND_STATES.STATES.IS_LIFT_CONNECTED;
                        if (KineticsLeanRequest.size() > 0) {

                            if(MachineBindActuators) {
                                if(EnableVerticalMotion) {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(GlobalContext.context.getPackageName()
                                        ,Actuator.LEAN);
//                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
//                                            Actuator.LEAN);
                                }
                                else
                                {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                            Actuator.LEAN);
                                }
                                BindedActuatorList.add(Actuator.LEAN);
                            }else
                            {
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                        Actuator.LEAN);

                            }

                        } else {
                                super.delegate.notify_NextStep(ID);
                        }
                        break;

                }

        case IS_LIFT_CONNECTED: {
            if(!BindedActuatorList.contains(Actuator.LIFT))
            {
                CURRENT_STATE = BIND_STATES.STATES.CONNECT_LIFT;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(GlobalContext.context.getPackageName(), Actuator.LIFT);
                break;
            }
            else
            {
                CURRENT_STATE = BIND_STATES.STATES.IS_TILT_CONNECTED;
                super.delegate.notify_NextStep(ID);
            }
        }
        case CONNECT_LIFT: {
                if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse))
               {
                        CURRENT_STATE = BIND_STATES.STATES.IS_LIFT_ATTACHED;
                        super.delegate.notify_NextStep(ID);
                } else {

                        CURRENT_STATE = BIND_STATES.STATES.IS_LIFT_ATTACHED;
                        WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(GlobalContext.context.getPackageName(), Actuator.LIFT);
                }
                break;
        }
        case IS_LIFT_ATTACHED: {
                CURRENT_STATE = BIND_STATES.STATES.DUMMY_READ_DEGREE_LIFT;
                WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(GlobalContext.context.getPackageName(), Actuator.LIFT);
                break;
        }
        case DUMMY_READ_DEGREE_LIFT: {
                        if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse))
                        {
                                KineticsLiftRequest.clear();
                                CURRENT_STATE = BIND_STATES.STATES.IS_TILT_CONNECTED;
                                super.delegate.notify_NextStep(ID);
                        }
                        else
                        {
                                CURRENT_STATE = BIND_STATES.STATES.READ_DEGREE_LIFT;
                                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.LIFT);
                        }

                        break;
                }

        case READ_DEGREE_LIFT: {
                        KineticsLiftRequest.addAll(
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestTiming(100, Actuator.LIFT));
                                        add(new KineticsRequestDelay(0, Actuator.LIFT));
                                }});
                        CURRENT_STATE = BIND_STATES.STATES.SET_DEGREE_LIFT;

                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.LIFT);


                break;
        }
        case SET_DEGREE_LIFT: {
                CURRENT_STATE = BIND_STATES.STATES.TRIGGER_LIFT;
                final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(
                Actuator.LIFT, LastKineticsResponse);
                if (request != null) {
                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.LIN, Actuator.LIFT));
                                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LIFT));
                                        add(request);
                                }});
                }

                break;
        }
                case TRIGGER_LIFT: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_LIFT;

                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                        _TriggerAngles.addAll(KineticsLiftRequest);

                        if (_TriggerAngles.size() > 0) {
                                _TriggerAngles.add(new KineticsRequestTrigger());
                                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                        _TriggerAngles);
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }

                        break;
                }
                case TRIGGER_LIFT_CONFIRM: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_LIFT;

                        new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
                                        _TriggerAngles.add(new KineticsRequestTrigger());
                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                                _TriggerAngles);


                                }
                        }, 100);

//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
//                                        _TriggerAngles.add(new KineticsRequestTrigger());
//                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
//                                                _TriggerAngles);
//                                }
//                        }, 100);

                        break;
                }
                case ATTACH_SERVO_LIFT: {
                        CURRENT_STATE = BIND_STATES.STATES.IS_TILT_CONNECTED;
                        if (KineticsLiftRequest.size() > 0) {

                            if(MachineBindActuators) {
                                if(EnableVerticalMotion)
                                {
                                WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(GlobalContext.context.getPackageName()
                                        , Actuator.LIFT);
//                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
//                                            Actuator.LIFT);
                                }
                                else
                                {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                            Actuator.LIFT);
                                }
                                BindedActuatorList.add(Actuator.LIFT);
                            }
                            else
                            {
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                        Actuator.LIFT);
                            }

                        } else {
                                super.delegate.notify_NextStep(ID);
                        }
                        break;
                }

                case IS_TILT_CONNECTED: {

                    if(!BindedActuatorList.contains(Actuator.TILT))
                    {
                        CURRENT_STATE = BIND_STATES.STATES.CONNECT_TILT;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorPowerStatus(GlobalContext.context.getPackageName(), Actuator.TILT);
                        break;
                    }
                    else
                    {
                        CURRENT_STATE = BIND_STATES.STATES.SET_ACTIVATE_POSITION;
                        super.delegate.notify_NextStep(ID);
                    }


                }
                case CONNECT_TILT: {
                        if (KineticComms.Instance.CheckActuatorPowerStatus(LastKineticsResponse)){
                                CURRENT_STATE = BIND_STATES.STATES.IS_TILT_ATTACHED;
                                super.delegate.notify_NextStep(ID);
                        } else {

                                CURRENT_STATE = BIND_STATES.STATES.IS_TILT_ATTACHED;
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOnMotor(GlobalContext.context.getPackageName(), Actuator.TILT);
                        }


                        break;
                }

        case IS_TILT_ATTACHED: {
                        CURRENT_STATE = BIND_STATES.STATES.DUMMY_READ_DEGREE_TILT;
                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadActuatorSignalStatus(GlobalContext.context.getPackageName(), Actuator.TILT);

                break;
        }
        case DUMMY_READ_DEGREE_TILT: {
                        if (KineticComms.Instance.CheckActuatorSignalStatus(LastKineticsResponse))
                        {
                                KineticsTiltRequest.clear();
                                CURRENT_STATE = BIND_STATES.STATES.SET_ACTIVATE_POSITION;
                                super.delegate.notify_NextStep(ID);
                        }
                        else
                        {
                                CURRENT_STATE = BIND_STATES.STATES.READ_DEGREE_TILT;
                                WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.TILT);
                        }
                        break;
                }
        case READ_DEGREE_TILT: {
                        KineticsTiltRequest.addAll(
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestTiming(100, Actuator.TILT));
                                        add(new KineticsRequestDelay(0, Actuator.TILT));
                                }});

                        CURRENT_STATE = BIND_STATES.STATES.SET_DEGREE_TILT;

                        WaitingForKineticsRequestAck = KineticComms.Instance.ReadDegree(GlobalContext.context.getPackageName(), Actuator.TILT);



                break;
        }

        case SET_DEGREE_TILT: {
                CURRENT_STATE = BIND_STATES.STATES.TRIGEGER_TILT;

               final KineticsRequest request = KineticComms.Instance.GetKineticRequestAngleFromDegreeResponse(
                Actuator.TILT, LastKineticsResponse);
                if (request != null) {
                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                new ArrayList<KineticsRequest>() {{
                                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.LIN, Actuator.TILT));
                                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.TILT));
                                        add(request);
                                }});
                }


                break;

        }
                case TRIGEGER_TILT: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_TILT;

                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();

                        _TriggerAngles.addAll(KineticsTiltRequest);

                        if (_TriggerAngles.size() > 0) {
                                _TriggerAngles.add(new KineticsRequestTrigger());
                                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                        _TriggerAngles);
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }

                        break;
                }
                case TRIGEGER_TILT_CONFIRM: {
                        CURRENT_STATE = BIND_STATES.STATES.ATTACH_SERVO_TILT;

                        new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {

                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
                                        _TriggerAngles.add(new KineticsRequestTrigger());
                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(GlobalContext.context.getPackageName(),
                                                _TriggerAngles);
                                }
                        }, 100);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                        ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
//                                        _TriggerAngles.add(new KineticsRequestTrigger());
//                                        WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(
//                                                _TriggerAngles);
//                                }
//                        }, 100);

                        break;
                }

                case ATTACH_SERVO_TILT: {
                        CURRENT_STATE = BIND_STATES.STATES.SET_ACTIVATE_POSITION;
                        if (KineticsTiltRequest.size() > 0) {
                            if(MachineBindActuators) {
                                if(EnableMotion)
                                {
                                WaitingForKineticsRequestAck = KineticComms.Instance.AttachSignalToActuator(GlobalContext.context.getPackageName()
                                        ,Actuator.TILT);
//                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
//                                            Actuator.LIFT);
                                }
                                else
                                {
                                    WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                            Actuator.LIFT);
                                }
                                BindedActuatorList.add(Actuator.TILT);
                            }
                            else
                            {
                                WaitingForKineticsRequestAck = KineticComms.Instance.PowerOffMotor(GlobalContext.context.getPackageName(),
                                        Actuator.TILT);
                            }
                        } else {
                                super.delegate.notify_NextStep(ID);
                        }
                        break;
                }






        case SET_ACTIVATE_POSITION:
        {
            if(MachineBindActuators)
            {
                if(BindedActuatorList.contains(Actuator.LEAN) && BindedActuatorList.contains(Actuator.TURN) &&
                        BindedActuatorList.contains(Actuator.LIFT) && BindedActuatorList.contains(Actuator.TILT)) {
                    CURRENT_STATE = BIND_STATES.STATES.MACHINE_BINDED;
                }
                else
                {
                    CURRENT_STATE = BIND_STATES.STATES.IS_TURN_CONNECTED;
                }

                super.delegate.notify_NextStep(ID);
            }
            else
            if(CURRENT_STATE != BIND_STATES.STATES.MACHINE_BINDED)
            {
                CURRENT_STATE = BIND_STATES.STATES.WAIT_MACHINE_BIND;
                notify_BindState(BIND_STATES.STATES.WAIT_MACHINE_BIND);
            }

                break;
        }
        case MACHINE_BINDED: {
                super.delegate.notify_Finish(ID);
            notify_BindState(BIND_STATES.STATES.MACHINE_BINDED);
                break;
        }
        default:
        break;
        }

        }

        void notify_BindState(BIND_STATES.STATES _BindState)
        {
            if(BindStateConvey != null)
            {
                BindStateConvey.BindStateChanged(_BindState);
            }
        }

        }
