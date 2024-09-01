package Framework.JOBS.StudioSession;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.SystemEventHandlers.AudioContextConveyHandler;
import Framework.SystemEventHandlers.UIChoreogramHandler;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Delegates.AnimationParameterTypeDelegates;
import FrameworkInterface.DataTypes.Delegates.ChoreogramBindings;
import FrameworkInterface.DataTypes.Delegates.PlayerConvey;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class ChoreogramJob  extends AnimationEngine implements AnimationParameterTypeDelegates, PlayerConvey, ChoreogramBindings, ChoreogramRead
{

    public ChoreogramJob()
    {
        super();
        Name = "CHOREOGRAM JOB";
        delChoreogramBinding = this;
        ShouldAutoTerinateJob = false;
        PRIORITY = JOB_PRIORITY.CHOREOGRAM;
    }





         void TakeOverMotionGraphicResourcesAndInitializeEngine()
        {

            if(Views.isEmpty())
            {

                ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
                    add(AnimationObject.Motor_Turn);
                    add(AnimationObject.Motor_Lean);
                    add(AnimationObject.Motor_Lift);
                    add(AnimationObject.Motor_Tilt);
                }};

                final Map<Integer, View> elements = UIMAINModuleHandler.Instance.AniUIHandler.GetAllUIViews();
                final Map<AnimationObject, View> AnimationObjectMap = new HashMap<AnimationObject, View>();

                for (Map.Entry<Integer, View> view : elements.entrySet()) {
                    AnimationObjectMap.put(AnimationObject.fromInt(view.getKey()), view.getValue());
                }

                Views = AnimationObjectMap;
                InitializeEngine(new ArrayList<AnimationEngineParameterGroup>());

//                final ArrayList<View> elements =  UIMAINModuleHandler.Instance.AniUIHandler.GetAllUIElements();
//                final Map<AnimationObject, View> AnimationObjectMap = new HashMap<AnimationObject, View>();
//
//                Runnable UIRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//
//                        synchronized (this) {
//                            for (View view : elements) {
//                                AnimationObjectMap.put(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())), view);
//                            }
//                            this.notify();
//                        }
//                    }
//                };
//
//                new Handler(Looper.getMainLooper()).post(UIRunnable);
//
//                synchronized(UIRunnable) {
//
//                    Views = AnimationObjectMap;
//                    InitializeEngine(new ArrayList<AnimationEngineParameterGroup>());
//                }

            }
            else
            {
                InitializeEngine(new ArrayList<AnimationEngineParameterGroup>());
            }
        }




    @Override
    public void TakeOverResources(JobConvey _delegate)
        {
        STATE = JOB_STATE.NA;
        super.TakeOverEngineResources(_delegate);
        TakeOverMotionGraphicResourcesAndInitializeEngine();
        AudioContextConveyHandler.Instance.PullPlayerDelegates(this);
        UIChoreogramHandler.Instance.setNotifyOnRead(this);
        }

    @Override
    public  void Resume() {
        if(STATE == JOB_STATE.INITIALIZED)
        {
        if(ReadyEngineToResume()){
        STATE = JOB_STATE.RUNNING;

        }
        else
        {
        return;
        }
        }

        if(STATE == JOB_STATE.RUNNING)
        {
        ResumeEngine();
        }
        }

        public Boolean IsPaused()
        {
        if(STATE == JOB_STATE.PAUSED)
        {
        return true;
        }
        return false;
        }

    @Override
    public  void  Pause() {
        STATE = JOB_STATE.PAUSED;
        AudioContextConveyHandler.Instance.RevokePlayerDelegates();
        UIChoreogramHandler.Instance.RevokeChoreogramRead();
        PauseEngine();
        //self.delegate.notify_Finish(ID: ID)
        }

        //ChoreogramBindings
        public boolean ShouldWaitToTrigger(Integer StartSec) {

        if(StartSec < 0)
        {
        return false;
        }
        if((audioProgress*1000) < StartSec)
        {
        return true;
        }

        return false;
        }
        //End of ChoreogramBindings

        //PlayerConvey
        public void FinishedPlayingSound() {

        }

    Integer audioProgress = 0;
    Integer StopTime = 0;
        public void UpdateAudioPlayProgress(int progress) {
        audioProgress = progress;
        if(progress > StopTime)
        {
        Stop();
            if(UIChoreogramHandler.Instance.getChoreogramConvey() != null) {
                UIChoreogramHandler.Instance.getChoreogramConvey().ChoreogramFinished();
            }
        }
        else
            if(UIChoreogramHandler.Instance.getChoreogramConvey() != null)
            {
                UIChoreogramHandler.Instance.getChoreogramConvey().ProgressUpdated(progress);
            }
        }
        //End of PlayerConvey

        //ChoreogramRead
        public Boolean IsPlaying() {
            return ArticulationManager.Instance.IsSoundPlayerPlaying();
        }
        //End of ChoreogramRead

        public void Stop()
        {
            ArticulationManager.Instance.PauseSound();
            haltEngine();
        }

        public void requestToDoAnimation(String audio, int StartSec, int StopSec, ArrayList<BeatsType> beats) {

        audioProgress = 0;

            AnimationExpressionHelper ExpressionHelper = new AnimationExpressionHelper();

            DB_Local_Store dbHAndler = new DB_Local_Store();
            Expressions_Type Expression =  dbHAndler.readExpression("Stand_Straight");
            AnimationEngineParameterGroup Straight_Aniamtion = ExpressionHelper.GetPlaneAnimation(Expression.Action_Data);

        ArrayList<AnimationEngineParameterGroup> animationGroup = new ArrayList<AnimationEngineParameterGroup>();

        if(this.IsHalfBlink)
        {
        animationGroup.add(ExpressionHelper.GetBlinkCorrectionStraight());
        }

        animationGroup.add(Straight_Aniamtion);

        animationGroup.get(0).Expressions.get(0).StartSec = -1;

        if(beats.size() > 0)
        {
            AnimationEngineParameterGroup arrayOfAnimations = ExpressionHelper.GetPlaneAnimationSetFromBeat(beats);
            animationGroup.add(arrayOfAnimations);

            if(animationGroup.size() > 1)
            {
                if(animationGroup.get(1).Expressions.size() > 0)
                {
                    animationGroup.get(1).Expressions.get(0).audio = audio;

                    StopTime = (animationGroup.get(1).Expressions.get(animationGroup.get(1).Expressions.size()-1).EndSec);
                    animationGroup.get(1).Expressions.get(animationGroup.get(1).Expressions.size()-1).delegate = this;
                }
            }
        }
        else
        {
            animationGroup.get(0).Expressions.get(0).audio = audio;
            animationGroup.get(0).Expressions.get(0).StartSec = StartSec;
            animationGroup.get(0).Expressions.get(0).delegate = this;
            StopTime = (StopSec);
        }

        ClearBufferedData();
        InsertAnimationAt(animationGroup,  0);
        //EmptyBufferFromIndex(Index: animationGroup.count)


        //Restart animation state machine during job resume or when state machine finished
        if(STATE == JOB_STATE.NA || CurrentAnimationEngineState == AnimationEngineStates.NA)
        {
        STATE = JOB_STATE.INITIALIZED;
        CurrentAnimationEngineState = AnimationEngineStates.SEND_MOTION_COMMAND;
        Resume();
        }

        if(IsIDLE())
        {
        CurrentAnimationEngineState = AnimationEngineStates.SEND_MOTION_COMMAND;
        Resume();
        }

        }

    //AnimationParameterTypeDelegates
    public void AnimationFinished() {
        Stop();
        if(UIChoreogramHandler.Instance.getChoreogramConvey() != null) {
            UIChoreogramHandler.Instance.getChoreogramConvey().ChoreogramFinished();
        }

    }
    //End of AnimationParameterTypeDelegates


        }

