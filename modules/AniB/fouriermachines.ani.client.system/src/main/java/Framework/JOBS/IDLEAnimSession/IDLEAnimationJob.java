package Framework.JOBS.IDLEAnimSession;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Delegates.AnimationParameterTypeDelegates;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class IDLEAnimationJob extends AnimationEngine implements AnimationParameterTypeDelegates
{

            Timer AnimationTimer;
        Integer ActivePeriod = 0; //InSTEPS_OF 1 Sec
        Integer SLEEP_TIMEOUT = 60; //GO TO Sleep After 10 seconds
        ArrayList<AnimationEngineParameterGroup> Animation_Sleep_Buffer = new ArrayList<AnimationEngineParameterGroup>();
            UUID NAPID = UUID.randomUUID();
       public static boolean IsJobResumedFirstTime = true;
       static  String LOG_TAG = "IDLEAnimationJob";
        IDLEAnimStatusDelegate IDLEAnimDelegate;

public IDLEAnimationJob(IDLEAnimStatusDelegate delIDLEAnim) {

        super();
    Name = "IDLE ANIM JOB";
      //  InitializeAnimationBuffer();
        PRIORITY = JOB_PRIORITY.LIVE;
    ShouldAutoTerinateJob = false;
    IDLEAnimDelegate = delIDLEAnim;
        }

        void InitializeAnimationBuffer()
        {
            if(IsJobResumedFirstTime)
            {
                ArrayList<AnimationEngineParameterGroup> DefaultSetOfAnimationsGroups = new ArrayList<AnimationEngineParameterGroup>();


                AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();
//                AnimationEngineParameterGroup napanimGroup = expressionHelper.GetNapGraphicOnlyAnimation(this);
                AnimationEngineParameterGroup napanimGroup = expressionHelper.GetNapAnimation(this);
                    DefaultSetOfAnimationsGroups.add(napanimGroup);
                NAPID = napanimGroup.AnimationGroupID;

                UpdateAnimationsToEngineBuffer( DefaultSetOfAnimationsGroups);
                expressionHelper = null;
            }
            else {
                if(NAPID != null) {
                    RemoveBufferedDataWithID(NAPID);
                }
                DisableEmptyBufferFromIndex();

                if (IsBufferGoingEmpty()) {
                    ArrayList<AnimationEngineParameterGroup> DefaultSetOfAnimationsGroups = new ArrayList<AnimationEngineParameterGroup>();

                    if(NAPID != null) {
//                        DefaultSetOfAnimationsGroups.add(expressionHelper.GetStraightAnimation());
                        UpdateAnimationsToEngineBuffer( new ArrayList<AnimationEngineParameterGroup>(){{
                            add(new AnimationExpressionHelper().GetStraightAnimation());
                        }});
                        NAPID = null;
                    }

                    AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();
                    String json = GetRandomNoAttentionAnimation();
                    DefaultSetOfAnimationsGroups.add(expressionHelper.GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(json));
                    DefaultSetOfAnimationsGroups.add(expressionHelper.GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(json));
                    UpdateAnimationsToEngineBuffer(DefaultSetOfAnimationsGroups);
                    for (int i = 0; i < 5; i++) {
//                        String json = GetRandomNoAttentionAnimation();
                        UpdateAnimationsToEngineBuffer( new ArrayList<AnimationEngineParameterGroup>(){{
                            add(new AnimationExpressionHelper().GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight( GetRandomNoAttentionAnimation()));
                        }});
//                        DefaultSetOfAnimationsGroups.add(expressionHelper.GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(json));
                    }
//                    UpdateAnimationsToEngineBuffer(DefaultSetOfAnimationsGroups);
                }

            }
        }

        void AnimationTimerTick()
        {
            ActivePeriod = ActivePeriod+1;
            if(SLEEP_TIMEOUT <= ActivePeriod)
            {
            AnimationTimer.cancel();
                final   AnimationEngineParameterGroup napAnimation = new AnimationExpressionHelper().GetNapAnimation(this);
                NAPID = napAnimation.AnimationGroupID;
                InsertAnimationAt( new ArrayList<AnimationEngineParameterGroup>(){{
                add(napAnimation);
            }} ,  1);

            //Remove all animation which are on top of nap animation
            EmptyBufferFromIndex(2);
            }
            else
            if(IsBufferGoingEmpty())
            {
                final String json = GetRandomNoAttentionAnimation();
                AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();
                UpdateAnimationsToEngineBuffer( new ArrayList<AnimationEngineParameterGroup>(){{
                add(new AnimationExpressionHelper().GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight( json));
                }});
            }
        }

        String GetRandomNoAttentionAnimation()
        {
            ArrayList<String> NoAttention_Animations = new ArrayList<String>(){{
                add("gotdown_lookrightup_thinking");
                add("gotdown_lookleftup_thinking");
                add("gotdown_lookrightdown_thinking");
                add("gotdown_lookleftdown_thinking");
                add("look_right");
                add("look_left");
                add("focus_leftdown");
                add("focus_rightdown");
                add("focus_leftup");
                add("think_leftup");
                add("look_rightup");
            }};

            Random rand = new Random();
            Integer index = rand.nextInt(NoAttention_Animations.size());

            DB_Local_Store dbHAndler = new DB_Local_Store();
            Expressions_Type Expression =  dbHAndler.readExpression(NoAttention_Animations.get(index));
            return Expression.Action_Data;
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
                LoadAnimation();

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
//                    LoadAnimation();
//                }
            }
            else
            {
                LoadAnimation();
            }
        }

        public  void LoadAnimation()
        {
            if(Motors.isEmpty()) {
                ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
                    add(AnimationObject.Motor_Turn);
                    add(AnimationObject.Motor_Lean);
                    add(AnimationObject.Motor_Lift);
                    add(AnimationObject.Motor_Tilt);
                }};
                Motors = MotionElements;
            }

            InitializeEngine(new ArrayList<AnimationEngineParameterGroup>());
            Log.i(LOG_TAG, "InitializeAnimationBuffer started.");
            InitializeAnimationBuffer();
            Log.i(LOG_TAG, "InitializeAnimationBuffer started.");


            //Runs only First time Job was initiliazed
            if(STATE == JOB_STATE.NA)
            {
                final  AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();

                if(AnimationEngine.IsHalfBlink)
                {
                    PushImmediateAnimation( new ArrayList<AnimationEngineParameterGroup>(){{
                        add(expressionHelper.GetBlinkCorrectionStraight());
                    }});
                }
                STATE = JOB_STATE.INITIALIZED;
                CurrentAnimationEngineState = AnimationEngineStates.SEND_MOTION_COMMAND;
                //Resume();
            }
        }

        @Override
        public void TakeOverResources(JobConvey _delegate)
        {
        STATE = JOB_STATE.NA;
        ActivePeriod = 0;
        super.TakeOverEngineResources( _delegate);
            Log.i(LOG_TAG, "TakeOverMotionGraphicResourcesAndInitializeEngine started.");
        TakeOverMotionGraphicResourcesAndInitializeEngine();
            Log.i(LOG_TAG, "TakeOverMotionGraphicResourcesAndInitializeEngine done.");
            if(IDLEAnimDelegate != null)
            IDLEAnimDelegate.IDLEAnimationStarted();
        }

        public void RestartEngine()
        {
            STATE = JOB_STATE.NA;
            ActivePeriod = 0;
            TakeOverMotionGraphicResourcesAndInitializeEngine();
            if(IDLEAnimDelegate != null)
                IDLEAnimDelegate.IDLEAnimationStarted();
        }

        @Override
       public  void Resume() {

        if(IsJobResumedFirstTime)
        {
            IsJobResumedFirstTime = false;
            STATE = JOB_STATE.RUNNING;
        }

        if(STATE == JOB_STATE.INITIALIZED)
        {
        if(ReadyEngineToResume()){
            if(AnimationTimer != null) {
                AnimationTimer.cancel();
            }

            AnimationTimer = new Timer();
            AnimationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                AnimationTimerTick();
            }
        }, 0, 500);

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

        public void Pause() {
        IsPaused = true;
        STATE = JOB_STATE.PAUSED;
        if(AnimationTimer != null)
        AnimationTimer.cancel();
        PauseEngine();
        }




        //AnimationParameterTypeDelegates
        public void AnimationFinished() {
            if (IsPaused) {
                RestartEngine();
                Resume();
            } else {
                Pause();

                if (IDLEAnimDelegate != null) {
                    IDLEAnimDelegate.IDLEAnimationFinished();
                }
            }
        }
        //End of AnimationParameterTypeDelegates
        }
