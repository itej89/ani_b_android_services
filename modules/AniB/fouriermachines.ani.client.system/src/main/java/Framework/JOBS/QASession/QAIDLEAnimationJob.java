package Framework.JOBS.QASession;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.GlobalContext;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Constants.MotionStartType;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import client.ani.fouriermachines.fouriermachinesaniclientsystem.R;

public class QAIDLEAnimationJob  extends AnimationEngine
{
    Timer AnimationTimer;
    String SpeakText = "" ;



    public  QAIDLEAnimationJob() {

        super();
        Name = "QA IDLE Animation";
        ShouldAutoTerinateJob = false;
        PRIORITY = JOB_PRIORITY.QAIDLE;


        StringBuilder returnString = new StringBuilder();
        InputStream inputStream = GlobalContext.context.getResources().openRawResource(R.raw.annieutf8);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        try{
        while ((line = bufferedReader.readLine()) != null) {
            returnString.append(line);
        }

            bufferedReader.close();
            inputStream.close();
        }
        catch(Exception e)
            {

            }

            SpeakText = returnString.toString();
        }




    void InitializeAnimationBuffer()
    {
        EmptyExpressionBuffer();

        ArrayList<AnimationEngineParameterGroup> DefaultSetOfAnimationsGroups = new ArrayList<AnimationEngineParameterGroup>();

        AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();

        if(AnimationEngine.IsHalfBlink)
        {
            DefaultSetOfAnimationsGroups.add(expressionHelper.GetBlinkCorrectionStraight());
        }

        AnimationEngineParameterGroup straightAnimation = expressionHelper.GetStraightAnimation( SpeakText);
        SpeakText = "";
        straightAnimation.Expressions.get(0).TriggerType = MotionStartType.INSTANT_MOVE;

        DefaultSetOfAnimationsGroups.add(straightAnimation);
        DefaultSetOfAnimationsGroups.add(expressionHelper.GetStraightBlink());

        UpdateAnimationsToEngineBuffer( DefaultSetOfAnimationsGroups);
    }

    void AnimationTimerTick()
    {
        if(CurrentAnimationEngineState == AnimationEngineStates.NA)
        {
            ArrayList<AnimationEngineParameterGroup> DefaultSetOfAnimationsGroups = new ArrayList<AnimationEngineParameterGroup>();

            AnimationExpressionHelper expressionHelper = new AnimationExpressionHelper();
            DefaultSetOfAnimationsGroups.add(expressionHelper.GetStraightBlink());

            UpdateAnimationsToEngineBuffer( DefaultSetOfAnimationsGroups);

            CurrentAnimationEngineState = AnimationEngineStates.SEND_MOTION_COMMAND;
            Resume();
        }
    }



    void TakeOverMotionGraphicResourcesAndInitializeEngine()
    {
        if(Views.isEmpty()) {

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

//            final ArrayList<View> elements = UIMAINModuleHandler.Instance.AniUIHandler.GetAllUIElements();
//            final Map<AnimationObject, View> AnimationObjectMap = new HashMap<AnimationObject, View>();
//
//            Runnable UIRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (this) {
//                        for (View view : elements) {
//                            AnimationObjectMap.put(AnimationObject.fromInt(Integer.parseInt((String) view.getTag())), view);
//                        }
//                        this.notify();
//                    }
//                }
//            };
//
//            new Handler(Looper.getMainLooper()).post(UIRunnable);
//
//            synchronized (UIRunnable) {
//
//                Views = AnimationObjectMap;
//                LoadAnimation();
//
//            }
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
        InitializeAnimationBuffer();


        //Runs only First time Job was initiliazed
        if(STATE == JOB_STATE.NA)
        {
            STATE = JOB_STATE.INITIALIZED;
            // Resume();
        }
    }

    @Override
    public void TakeOverResources(JobConvey _delegate)
    {
        STATE = JOB_STATE.NA;
        super.TakeOverEngineResources( _delegate);
        TakeOverMotionGraphicResourcesAndInitializeEngine();
    }
    @Override
    public void Resume() {


        if(STATE == JOB_STATE.INITIALIZED)
        {
            if(ReadyEngineToResume()){
                if(AnimationTimer != null)
                AnimationTimer.cancel();

                AnimationTimer = new Timer();
                AnimationTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        AnimationTimerTick();
                    }
                }, 0, 3000);
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

    @Override
    public void Pause() {
        STATE = JOB_STATE.PAUSED;
        if(AnimationTimer != null)
            AnimationTimer.cancel();
        PauseEngine();
    }

}
