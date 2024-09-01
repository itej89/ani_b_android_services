package Framework.JOBS.Applet;

import android.os.ParcelUuid;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AnimationPositions;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.Applet.ActionRequestConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestInstantTrigger;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class AugmentRacingJob extends Job implements KineticsResponseConvey, ActionRequestConvey
{
    Timer MotionTimer;
    ParcelUuid WaitingForKineticsRequestAck = null;
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();
    ArrayList<KineticsRequest> KineticsRequestForPose = new ArrayList<KineticsRequest>();
    String PoseStateName;
    int LeanRef = -1;
    int LiftRef = -1;
    int TurnRef = -1;
    int TiltRef = -1;
    int LeanCurrent = -1;
    int LiftCurrent = -1;
    int TurnCurrent = -1;
    int TiltCurrent = -1;

    private static final int TURN_LIMIT = 15;
    private static final int TILT_LIMIT = 5;

    private static final int TURN_STEP = 5;
    private static final int TILT_STEP = 3;
    private static final int LEAN_STEP = 3;
    private static final int LIFT_STEP = 3;

    enum   KineticStates{NA, Read_Degree_Lift, Read_Degree_Lean, SetPose, DoPose, SetBeat, DoBeat, Done}
    KineticStates CURRENT_STATE = KineticStates.NA;

    //KineticsResponseConvey delegate
    public void CommsLost() {
        super.delegate.notify_LostResource( ID);
    }

    public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {
        if(_Acknowledgement.toString().equals(WaitingForKineticsRequestAck.toString())){
            LastKineticsResponse = responeData;
            WaitingForKineticsRequestAck = null;
            TickCounter = 0;
        }
    }

    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
        //Retry if any communication fail
        CURRENT_STATE =  KineticStates.NA;
//        DOSTEP();
    }
    //End KineticsResponseConvey delegate



    public AugmentRacingJob(String _PoseStateName) {
        super();
        PRIORITY = JOB_PRIORITY.APPLET;
        CURRENT_STATE = KineticStates.NA;
        PoseStateName = _PoseStateName;
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
    }
    @Override
    public void Pause() {
        if(MotionTimer != null) {
            MotionTimer.cancel();
        }
        CURRENT_STATE= KineticStates.Done;
        KineticComms.Instance.SetKineticsResposeListener( null);
        KineticComms.Instance.ResetCommsContext();
        super.Pause();
        super.delegate.notify_Finish( ID);
    }
    @Override
    public void Resume() {
        if(CURRENT_STATE == KineticStates.NA) {

            //Read Primary pose kinectic commands
            DB_Local_Store dbHAndler = new DB_Local_Store(DB_Local_Store.GetDefaultDBPathFromName(DB_Local_Store.APPLET_BASE_DB));
            Expressions_Type Expression =  dbHAndler.readExpression(PoseStateName);

            AnimationPositions position = new AnimationPositions();
            JSONObject jObj = GenericExtentions.parseJSONString(Expression.Action_Data);
            position.parseJson(jObj);

            ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
                add(AnimationObject.Motor_Lean);
                add(AnimationObject.Motor_Lift);
            }};

            AnimationEngine engine  = new AnimationEngine();
            KineticsRequestForPose.addAll(engine.GetMotorCommand(position, MotionElements));

            for(int i=0; i<KineticsRequestForPose.size(); i++)
            {
                KineticsRequest req = KineticsRequestForPose.get(i);
                if(req.RequestType == CommandLabels.CommandTypes.ANG)
                {
                    switch (((KineticsRequestAngle)(req)).ActuatorType)
                    {
                        case LEAN:
                            LeanRef = ((KineticsRequestAngle)(req)).Angle;
                            LeanCurrent = LeanRef;
                            break;
                        case LIFT:
                            LiftRef = ((KineticsRequestAngle)(req)).Angle;
                            LiftCurrent = LiftRef;
                            break;
                        case TURN:
                            TurnRef = ((KineticsRequestAngle)(req)).Angle;
                            TurnCurrent = TurnRef;
                            break;
                        case TILT:
                            TiltRef = ((KineticsRequestAngle)(req)).Angle;
                            TiltCurrent = TiltRef;
                            break;
                        default:
                            break;
                    }
                }
                else
                if(req.RequestType == CommandLabels.CommandTypes.EAS)
                {
                    switch (((KineticsRequestServoEasing)(req)).ActuatorType)
                    {
                        case TURN:
                            KineticsRequestForPose.remove(i--);
                            break;
                        case TILT:
                            KineticsRequestForPose.remove(i--);
                            break;
                        default:
                            break;
                    }
                }
                else
                if(req.RequestType == CommandLabels.CommandTypes.TMG)
                {
                    switch (((KineticsRequestTiming)(req)).ActuatorType)
                    {
                        case TURN:
                            KineticsRequestForPose.remove(i--);
                            break;
                        case TILT:
                            KineticsRequestForPose.remove(i--);
                            break;
                        default:
                            break;
                    }
                }
                else
                if(req.RequestType == CommandLabels.CommandTypes.DEL)
                {
                    switch (((KineticsRequestDelay)(req)).ActuatorType)
                    {
                        case TURN:
                            KineticsRequestForPose.remove(i--);
                            break;
                        case TILT:
                            KineticsRequestForPose.remove(i--);
                            break;
                        default:
                            break;
                    }
                }
                else
                if(req.RequestType == CommandLabels.CommandTypes.INO)
                {
                    switch (((KineticsRequestEasingInOut)(req)).ActuatorType)
                    {
                        case TURN:
                            KineticsRequestForPose.remove(i--);
                            break;
                        case TILT:
                            KineticsRequestForPose.remove(i--);
                            break;
                        default:
                            break;
                    }
                }
            }


            CURRENT_STATE = KineticStates.SetPose;


            if(MotionTimer != null) {
                MotionTimer.cancel();
            }

            vertical_step_Count = 0;
            should_reverse_vertical = false;
            TurnRightAdder = 0;
            TurnLeftAdder = 0;
            DOSTEP();
            startTimer();

        }
        else
        {
            DOSTEP();
        }

    }
    //End Job Override

    void startTimer()
    {

        MotionTimer = new Timer();
        MotionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                MotionTimerTick();
            }
        }, 50);
    }

    int TickCounter = 0;
    void MotionTimerTick()
    {
        if(CURRENT_ACTION != ActionRequest.NA) {
            // To handle no-ack from hardware, add a timeout of 2 seconds for a timer of 50 ms
            if(WaitingForKineticsRequestAck == null || TickCounter > 40) {
                TickCounter = 0;
                super.delegate.notify_NextStep(ID);
            }
            else {
                if (TickCounter > 200) {
                    TickCounter = 0;
                }

                TickCounter++;
            }
        }
        startTimer();
    }
    //ActionRequest
    public void TurnLeft(){
//        if(TurnCurrent < TurnRef)
//            CURRENT_ACTION = ActionRequest.TURN_CENTRE;
//        else
        if(TurnCurrent < TurnRef+TURN_LIMIT+5)
            CURRENT_ACTION = ActionRequest.TURN_LEFT;
        else
            CURRENT_ACTION = ActionRequest.NA;


    }

    public void TurnCentre(){
        CURRENT_ACTION = ActionRequest.TURN_CENTRE;

    }

    public void TurnRight(){
//        if(TurnCurrent > TurnRef)
//            CURRENT_ACTION = ActionRequest.TURN_CENTRE;
//        else
        if(TurnCurrent > TurnRef-TURN_LIMIT-5)
            CURRENT_ACTION = ActionRequest.TURN_RIGHT;
        else
            CURRENT_ACTION = ActionRequest.NA;

    }
    //ActionRequest
    int vertical_step_Count = 0;
    int MAX_VERTICAL_STEPCOUNT = 6;
    boolean should_reverse_vertical = false;
    int TurnRightAdder = 0;
    int TurnLeftAdder = 0;
    boolean IsFirstTime = true;
    public  enum ActionRequest {TURN_LEFT, TURN_RIGHT, TURN_CENTRE, TRG,NA};
    ActionRequest CURRENT_ACTION = ActionRequest.NA;
    void DOSTEP()
    {
        switch (CURRENT_ACTION)
        {
            case TURN_LEFT: {
                if(TurnCurrent < TurnRef+TURN_LIMIT+5) {
                    TurnRightAdder = 0;
                    TurnLeftAdder ++;
                    ArrayList<KineticsRequest> KineticsRequestAction = new ArrayList<KineticsRequest>();
                    KineticsRequestAction.addAll(new ArrayList<KineticsRequest>() {{
                        TurnCurrent = TurnCurrent + TURN_STEP+TurnLeftAdder;
                        TiltCurrent = TiltCurrent + TILT_STEP+TurnLeftAdder;

                        int delay = 0;
                        if(!should_reverse_vertical) {
                            LeanCurrent = LeanCurrent + LEAN_STEP;
                            LiftCurrent = LiftCurrent - LIFT_STEP;
                        }
                        else
                        {
                            delay = 600;
                            LeanCurrent = LeanCurrent - LEAN_STEP;
                            LiftCurrent = LiftCurrent + LIFT_STEP;
                        }
                        vertical_step_Count++;
                        if(vertical_step_Count > MAX_VERTICAL_STEPCOUNT)
                        {
                            vertical_step_Count = 0;
                            should_reverse_vertical = !should_reverse_vertical;
                        }

                        add(new KineticsRequestAngle(TurnCurrent, Actuator.TURN));
                        add(new KineticsRequestAngle(TiltCurrent, Actuator.TILT));

                            add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.TURN));
                            add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.TURN));
                            add(new KineticsRequestTiming(700, Actuator.TURN));

                            add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.TILT));
                            add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.TILT));
                            add(new KineticsRequestTiming(700, Actuator.TILT));


//                        add(new KineticsRequestAngle(LeanCurrent, Actuator.LEAN));
//                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.LEAN));
//                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LEAN));
//                        add(new KineticsRequestTiming(700, Actuator.LEAN));
//                        add(new KineticsRequestDelay(delay, Actuator.LEAN));
//
//                        add(new KineticsRequestAngle(LiftCurrent, Actuator.LIFT));
//                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.LIFT));
//                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LIFT));
//                        add(new KineticsRequestTiming(700, Actuator.LIFT));

//                        if(!IsCentered)
                        add(new KineticsRequestInstantTrigger());
//                        else
//                            add(new KineticsRequestTrigger());


                    }});

                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(KineticsRequestAction);
                }
                break;
            }

            case TURN_CENTRE: {
                ArrayList<KineticsRequest> KineticsRequestAction = new ArrayList<KineticsRequest>();
                KineticsRequestAction.addAll(new ArrayList<KineticsRequest>() {{
                    TurnCurrent = TurnRef;
                    TiltCurrent = TiltRef;

                    add(new KineticsRequestAngle(TurnCurrent, Actuator.TURN));
                    add(new KineticsRequestAngle(TiltCurrent, Actuator.TILT));

                    add(new KineticsRequestTiming(5000, Actuator.TURN));
                    add(new KineticsRequestTiming(5000, Actuator.TILT));

                    add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.TURN));
                    add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IO, Actuator.TURN));

                    add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.TILT));
                    add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IO, Actuator.TILT));


                    add(new KineticsRequestInstantTrigger());

                }});
                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(KineticsRequestAction);
                break;
            }

            case TURN_RIGHT: {
                if(TurnCurrent > TurnRef-TURN_LIMIT-5) {
                    TurnLeftAdder = 0;
                    TurnRightAdder++;
                    ArrayList<KineticsRequest> KineticsRequestAction = new ArrayList<KineticsRequest>();
                    KineticsRequestAction.addAll(new ArrayList<KineticsRequest>() {{

                        int delay = 0;
                        TurnCurrent = TurnCurrent - TURN_STEP-TurnRightAdder;
                        TiltCurrent = TiltCurrent - TILT_STEP-TurnRightAdder;
                        if(!should_reverse_vertical) {
                            LeanCurrent = LeanCurrent + LEAN_STEP;
                            LiftCurrent = LiftCurrent - LIFT_STEP;
                        }
                        else
                        {
                            delay = 600;
                            LeanCurrent = LeanCurrent - LEAN_STEP;
                            LiftCurrent = LiftCurrent + LIFT_STEP;
                        }
                        vertical_step_Count++;
                        if(vertical_step_Count > MAX_VERTICAL_STEPCOUNT)
                        {
                            vertical_step_Count = 0;
                            should_reverse_vertical = !should_reverse_vertical;
                        }

                        add(new KineticsRequestAngle(TurnCurrent, Actuator.TURN));


                        add(new KineticsRequestAngle(TiltCurrent, Actuator.TILT));


                            add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.EXP, Actuator.TURN));
                            add(new KineticsRequestEasingInOut(CommandLabels.EasingType.OU, Actuator.TURN));
                            add(new KineticsRequestTiming(700, Actuator.TURN));

                            add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.TILT));
                            add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.TILT));
                            add(new KineticsRequestTiming(700, Actuator.TILT));


//                        add(new KineticsRequestAngle(LeanCurrent, Actuator.LEAN));
//                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.LEAN));
//                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LEAN));
//                        add(new KineticsRequestTiming(700, Actuator.LEAN));
//                        add(new KineticsRequestDelay(delay, Actuator.LEAN));
//
//                        add(new KineticsRequestAngle(LiftCurrent, Actuator.LIFT));
//                        add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SIN, Actuator.LIFT));
//                        add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IN, Actuator.LIFT));
//                        add(new KineticsRequestTiming(700, Actuator.LIFT));

                        add(new KineticsRequestInstantTrigger());

                    }});
                    WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(KineticsRequestAction);
                }
                break;
            }

            case TRG: {
                break;
            }
        }
        CURRENT_ACTION = ActionRequest.NA;
    }
}