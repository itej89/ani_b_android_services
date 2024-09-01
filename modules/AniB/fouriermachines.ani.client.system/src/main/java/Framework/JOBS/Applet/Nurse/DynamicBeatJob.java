package Framework.JOBS.Applet.Nurse;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AnimationPositions;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Delegates.Generic.GenericTypeStreamDelegate;
import Framework.DataTypes.Delegates.JobConvey;
import FrameworkImplementation.DataTypes.Delegates.LinkTransportConvey;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Job;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestFrequency;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTrigger;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import Framework.DataTypes.Transports.Helpers.RecievedData;
import Framework.SystemEventHandlers.LinkAnchorStreamHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.InterfaceImplementation.BotConnectImplementation;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class DynamicBeatJob extends Job implements LinkTransportConvey, KineticsResponseConvey, GenericTypeStreamDelegate
{

    ParcelUuid WaitingForKineticsRequestAck = new ParcelUuid(UUID.randomUUID());
    ArrayList<KineticsResponse> LastKineticsResponse = new ArrayList<KineticsResponse>();
    ArrayList<KineticsRequest> KineticsRequestForPose = new ArrayList<KineticsRequest>();
    String PoseStateName;
    int LeanRef = -1;
    int LiftRef = -1;
    int BPMDelta = -1;

    GeneralConsultationRecorder _genericConsulatationRecorder = new GeneralConsultationRecorder();
    CountDownTimer _RecordingCountDownTimer;
    int RecodingTimeout = 60; //Seconds

    enum RecordingStates  {RECORDING_ON, RECORDING_OFF};
    RecordingStates RecordingState = RecordingStates.RECORDING_OFF;

    public void UserTriggerNotify()
    {
        switch (RecordingState)
        {
            case RECORDING_ON:
                StopConsultationRecording();
                break;
            case RECORDING_OFF:
                StartConsultationRecording();
                break;
        }
    }

    public  void  StartConsultationRecording()
    {
        if(RecordingState == RecordingStates.RECORDING_OFF) {
            _genericConsulatationRecorder.PrepareRecoding();
            UIMAINModuleHandler.Instance.AppletUIHandler.StartNurseRecording();
            if (_RecordingCountDownTimer != null)
                _RecordingCountDownTimer.cancel();

            _RecordingCountDownTimer = new CountDownTimer(RecodingTimeout * 1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Log.d("StartConsultationRecording", "Counter Timeout");
                    if(RecordingState == RecordingStates.RECORDING_ON)
                        UserTriggerNotify();
                    Log.d("StartConsultationRecording", "REALEASED by Timeout");
                }
            }.start();

            RecordingState = RecordingStates.RECORDING_ON;
        }
    }

    public void  StopConsultationRecording()
    {
        if(RecordingState == RecordingStates.RECORDING_ON)
        {
            UIMAINModuleHandler.Instance.AppletUIHandler.StopNurseRecording();
            RecordingState = RecordingStates.RECORDING_OFF;

            if (_RecordingCountDownTimer != null)
                _RecordingCountDownTimer.cancel();

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    _genericConsulatationRecorder.EndRecording();
                    _genericConsulatationRecorder.RequestConsultation();
                }
            });
        }
    }

    //Start of stream Bind and Unbind tasks
    private class BindLinkStreamTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            if(BotConnectImplementation.Instance.IsLinkAvailable(LINK_ANCHORS.HEART_BEAT))
            {
                BotConnectImplementation.Instance.StartLinkStream(LINK_ANCHORS.HEART_BEAT, DynamicBeatJob.this);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    private class StopLinkStreamTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            if(BotConnectImplementation.Instance.IsLinkAvailable(LINK_ANCHORS.HEART_BEAT))
            {
                BotConnectImplementation.Instance.StopLinkStream(LINK_ANCHORS.HEART_BEAT);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
    //End of of stream Bind and Unbind tasks


    public void LinkDataReceived(LINK_ANCHORS Anchor, final RecievedData Data) {


        ArticulationManager.Instance.PlayAudioStream(Data.Data);

        if(RecordingState == RecordingStates.RECORDING_ON)
        {
            _genericConsulatationRecorder.AddBeat(Data.Data);
        }
    }


    enum   KineticStates{NA, Read_Degree_Lift, Read_Degree_Lean, SetPose, DoPose, SetBeat, DoBeat, Done}
    KineticStates CURRENT_STATE = KineticStates.NA;

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

        public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
            //Retry if any communication fail
            CURRENT_STATE =  KineticStates.NA;
            DOSTEP();
        }
    //End KineticsResponseConvey delegate

    //GenericTypeStreamDelegate
    public void notifyStream(Map.Entry<LINK_ANCHORS, String> LinkData)
    {
        if(LinkData != null && LinkData.getKey() == LINK_ANCHORS.HEART_MONITOR)
        {
            JSONObject dictionaryProperty = GenericExtentions.parseJSONString(LinkData.getValue());
            try {
                int SPO2 = -1; int BPM = -1; int PULSE = 0; double TEMPERATURE = -1;
                if (dictionaryProperty.has("HeartRate") && !dictionaryProperty.isNull("HeartRate") && dictionaryProperty.get("HeartRate") instanceof  Double)
                {
                    BPM = (int)dictionaryProperty.getDouble("HeartRate");
                    if(BPM < 40) BPM = 40;
                    else if(BPM > 120) BPM = 120;
                    BPMDelta = (int)GenericExtentions.map(BPM, 40, 120, 500, -500);

                    if(RecordingState == RecordingStates.RECORDING_ON) {
                        _genericConsulatationRecorder.AddBPM(BPM);
                    }
                }

                if (dictionaryProperty.has("HeartSensorRaw") && !dictionaryProperty.isNull("HeartSensorRaw") && dictionaryProperty.get("HeartSensorRaw") instanceof  Double) {
                    PULSE = (int)dictionaryProperty.getDouble("HeartSensorRaw");
                    if(RecordingState == RecordingStates.RECORDING_ON) {
                        _genericConsulatationRecorder.AddPulse(PULSE);
                    }
                }

                if (dictionaryProperty.has("Temperature") && !dictionaryProperty.isNull("Temperature") && dictionaryProperty.get("Temperature") instanceof  Double) {

                    TEMPERATURE = dictionaryProperty.getDouble("Temperature");
                    if(RecordingState == RecordingStates.RECORDING_ON) {
                        _genericConsulatationRecorder.AddTEMPERATURE(TEMPERATURE);
                    }
                }

                if (dictionaryProperty.has("SPO2") && !dictionaryProperty.isNull("SPO2") && dictionaryProperty.get("SPO2") instanceof  Double) {

                    SPO2 = (int)dictionaryProperty.getDouble("SPO2");
                    if(RecordingState == RecordingStates.RECORDING_ON) {
                        _genericConsulatationRecorder.AddSPO2(SPO2);
                    }
                }


                UIMAINModuleHandler.Instance.AppletUIHandler.UpdateNurseData(BPM, PULSE,  SPO2, TEMPERATURE );

            } catch (JSONException e) {
                e.printStackTrace();
                BPMDelta = -1;
            }
        }

        if(BPMDelta != -1 && CURRENT_STATE == KineticStates.Done){
            CURRENT_STATE = KineticStates.SetPose;
            DOSTEP();
        }

    }
    //End of GenericTypeStreamDelegate

    public DynamicBeatJob(String _PoseStateName) {
        super();
        PRIORITY = JOB_PRIORITY.APPLET;
        CURRENT_STATE = KineticStates.NA;
        PoseStateName = _PoseStateName;
    }

    //Job Override
    @Override
    public void TakeOverResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        LinkAnchorStreamHandler.instance.SubscribeToStream(LINK_ANCHORS.HEART_MONITOR, this);
        ArticulationManager.Instance.ReadyAudioStream();
        new BindLinkStreamTask().execute();
        super.TakeOverResources(_delegate);
    }
    @Override
    public void Pause() {
        CURRENT_STATE= KineticStates.Done;
        KineticComms.Instance.SetKineticsResposeListener( null);
        KineticComms.Instance.ResetCommsContext();
        LinkAnchorStreamHandler.instance.UnSubscribeStream(LINK_ANCHORS.HEART_MONITOR, this.StreamDelegateID);
        new StopLinkStreamTask().execute();
        ArticulationManager.Instance.CloseAudioStream();
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
                                        break;
                                        case LIFT:
                                            LiftRef = ((KineticsRequestAngle)(req)).Angle;
                                            break;
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
//                DOSTEP();
            }
            else
            {
//                DOSTEP();
            }
    }
    //End Job Override


    void DOSTEP()
    {
        switch (CURRENT_STATE)
        {
            case NA:
                break;

            case SetPose:
                CURRENT_STATE = KineticStates.DoPose;
                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(KineticsRequestForPose);
                break;

            case DoPose:
                if(BPMDelta != -1) {
                    CURRENT_STATE = KineticStates.SetBeat;
                }
                else
                {
                    CURRENT_STATE = KineticStates.Done;
                }
                ArrayList<KineticsRequest> _TriggerAngles = new ArrayList<KineticsRequest>();
                _TriggerAngles.add(new KineticsRequestTrigger());
                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(_TriggerAngles);

                break;

            case SetBeat:
                CURRENT_STATE = KineticStates.DoBeat;

                ArrayList<KineticsRequest> KineticsRequestForBeat = new ArrayList<KineticsRequest>();
                KineticsRequestForBeat.addAll(new ArrayList<KineticsRequest>() {{
                    add(new KineticsRequestAngle(LeanRef+5, Actuator.LEAN));
                    add(new KineticsRequestAngle(LiftRef-5, Actuator.LIFT));

                    add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SNW, Actuator.LIFT));
                    add(new KineticsRequestServoEasing(CommandLabels.EasingFunction.SNW, Actuator.LEAN));

                    add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IO, Actuator.LIFT));
                    add(new KineticsRequestEasingInOut(CommandLabels.EasingType.IO, Actuator.LEAN));

                    add(new KineticsRequestTiming(3000+BPMDelta, Actuator.LIFT));
                    add(new KineticsRequestTiming(3000+BPMDelta, Actuator.LEAN));

                    add(new KineticsRequestDelay(0, Actuator.LIFT));
                    add(new KineticsRequestDelay(0, Actuator.LEAN));

                    add(new KineticsRequestFrequency(3000+BPMDelta, Actuator.LIFT));
                    add(new KineticsRequestFrequency(3000+BPMDelta, Actuator.LEAN));
                }});
                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(KineticsRequestForBeat);

                break;

            case DoBeat:
                CURRENT_STATE = KineticStates.SetPose;

                ArrayList<KineticsRequest> _TriggerBeat = new ArrayList<KineticsRequest>();
                _TriggerBeat.add(new KineticsRequestTrigger());
                WaitingForKineticsRequestAck = KineticComms.Instance.SetParameters(_TriggerBeat);

                break;
        }
    }
}