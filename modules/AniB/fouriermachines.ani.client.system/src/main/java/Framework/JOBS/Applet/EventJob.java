package Framework.JOBS.Applet;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AppletIntents;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.AnimationEngineConvey;
import Framework.DataTypes.Delegates.Applet.AppletJobConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class EventJob extends AnimationEngine implements AnimationEngineConvey {
    AppletJobConvey delegateResponse;
    AppletIntents.Intents AppletName;
    String JsonAnimation;

   public enum Events{
        PORTRAIT_OnLoad
    }

    Events OnLoadState = Events.PORTRAIT_OnLoad;


    public EventJob(AppletIntents.Intents _AppletName, Events _OnLoadState , AppletJobConvey _delegateResponse) {
        super();

        Name = "Event Job";
        AppletName = _AppletName;
        OnLoadState = _OnLoadState;
        delegateResponse = _delegateResponse;
        PRIORITY = JOB_PRIORITY.APPLET;
        delAnimationEngineConvey = this;
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
//            ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
//                add(AnimationObject.Motor_Turn);
//                add(AnimationObject.Motor_Lean);
//                add(AnimationObject.Motor_Lift);
//                add(AnimationObject.Motor_Tilt);
//            }};
//
//
//            Runnable UIRunnable = new Runnable() {
//                @Override
//                public void run() {
//
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
//            synchronized (UIRunnable) {
//
//                Views = AnimationObjectMap;
//                LoadAnimation();
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
        requestToDoEventAnimation(Events.PORTRAIT_OnLoad);
    }


    @Override
    public void TakeOverResources(JobConvey _delegate)
    {
        STATE = JOB_STATE.NA;
        super.TakeOverEngineResources(_delegate);
        TakeOverMotionGraphicResourcesAndInitializeEngine();
    }
    @Override
    public void Resume() {
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

    @Override
    public void Pause() {
        STATE = JOB_STATE.PAUSED;
        PauseEngine();
        //delegate.notify_Finish( ID);
    }

    //AnimationEngineConvey
    public void AnuimationEngineFinalized()
    {
        if(delegateResponse != null)
        {
            delegateResponse.EventAnimationFinished();
        }
    }
    //End of AnimationEngineConvey


    public void requestToDoEventAnimation(Events EventName) {

        DB_Local_Store dbHAndler = new DB_Local_Store(DB_Local_Store.GetDefaultDBPathFromName(DB_Local_Store.APPLET_BASE_DB));

        Expressions_Type Expression =  dbHAndler.readExpression(EventName.name());



        AnimationExpressionHelper ExpressionHelper = new AnimationExpressionHelper();

        AnimationEngineParameterGroup AnimParam = ExpressionHelper.GetAnimationEngineParameterTypes(Expression.Action_Data);


        ArrayList<AnimationEngineParameterGroup> animationGroup = new ArrayList<AnimationEngineParameterGroup>();

        if(AnimationEngine.IsHalfBlink)
        {
            animationGroup.add(ExpressionHelper.GetBlinkCorrectionStraight());
        }

        animationGroup.add(AnimParam);

        UpdateAnimationsToEngineBuffer( animationGroup);

        //Restart animation state machine during job resume or when state machine finished
        if(STATE == JOB_STATE.NA || CurrentAnimationEngineState == AnimationEngine.AnimationEngineStates.NA)
        {
            STATE = JOB_STATE.INITIALIZED;
            CurrentAnimationEngineState = AnimationEngine.AnimationEngineStates.SEND_MOTION_COMMAND;
            Resume();
        }
    }

}
