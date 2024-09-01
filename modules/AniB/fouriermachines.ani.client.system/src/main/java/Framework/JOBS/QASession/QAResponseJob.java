package Framework.JOBS.QASession;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Delegates.QASession.QAResponseJobConvey;
import Framework.DataTypes.Delegates.AnimationEngineConvey;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import Framework.SystemEventHandlers.UIMAINModuleHandler;

    public class QAResponseJob extends AnimationEngine implements  AnimationEngineConvey
    {
            QAResponseJobConvey delegateResponse;
            String Response;
            String JsonAnimation;

        public QAResponseJob(String _Response, String _JsonAnimation, QAResponseJobConvey _delegateResponse)
        {
        super();
        Name = "QA Response";
        Response = _Response;
        JsonAnimation = _JsonAnimation;
        PRIORITY = JOB_PRIORITY.QARESPONSE;
        delegateResponse = _delegateResponse;
        delAnimationEngineConvey = this;
        }

        void TakeOverMotionGraphicResourcesAndInitializeEngine()
        {

            Log.d("QAResponseJob", "TakeOverMotionGraphicResourcesAndInitializeEngine");
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


                Log.d("QAResponseJob", "TakeOverMotionGraphicResourcesAndInitializeEngine Load animation.");
                LoadAnimation();

//                final ArrayList<View> elements = UIMAINModuleHandler.Instance.AniUIHandler.GetAllUIElements();
//                final Map<AnimationObject, View> AnimationObjectMap = new HashMap<AnimationObject, View>();
//                ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
//                    add(AnimationObject.Motor_Turn);
//                    add(AnimationObject.Motor_Lean);
//                    add(AnimationObject.Motor_Lift);
//                    add(AnimationObject.Motor_Tilt);
//                }};
//
//
//                Runnable UIRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//
//                        synchronized (this) {
//                            for (View view : elements) {
//                                AnimationObjectMap.put(AnimationObject.fromInt(Integer.parseInt((String) view.getTag())), view);
//                            }
//                            this.notify();
//                        }
//                    }
//                };
//                new Handler(Looper.getMainLooper()).post(UIRunnable);
//                synchronized (UIRunnable) {
//
//                    Views = AnimationObjectMap;
//                    LoadAnimation();
//
//
//                }
            }
            else
            {
                LoadAnimation();
            }


            Log.d("QAResponseJob", "TakeOverMotionGraphicResourcesAndInitializeEngine done.");

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
            requestToDoAnimation();
        }

        @Override
        public void TakeOverResources(JobConvey _delegate)
        {
        STATE = JOB_STATE.NA;
        super.TakeOverEngineResources(_delegate);
        TakeOverMotionGraphicResourcesAndInitializeEngine();
        }

        @Override
        public void Resume()
        {


            Log.d("QAResponseJob", "Resume.");
        if(STATE == JOB_STATE.INITIALIZED)
        {

            Log.d("QAResponseJob", "Resume INITIALIZED");
        if(ReadyEngineToResume()){
        STATE = JOB_STATE.RUNNING;

        }
        else
        {
            Log.e("QAResponseJob", "ReadyEngineToResume failed!!");
        return;
        }
        }

        if(STATE == JOB_STATE.RUNNING)
        {
            Log.d("QAResponseJob", "Resume RUNNING");
        ResumeEngine();
        }


            Log.d("QAResponseJob", "Resume done");
}

        @Override
        public void Pause()
        {
        STATE = JOB_STATE.PAUSED;
        PauseEngine();

        //delegate.notify_Finish( ID);

        }

        //AnimationEngineConvey
        public    void AnuimationEngineFinalized()
        {

            Log.d("QAResponseJob", "AnuimationEngineFinalized.");
            if(delegateResponse != null)
            {
                Log.d("QAResponseJob", "AnuimationEngineFinalized event sent");
                delegateResponse.QAAnimationFinished();
            }
            Log.d("QAResponseJob", "AnuimationEngineFinalized done.");
        }
        //End of AnimationEngineConvey

        public void requestToDoAnimation()
        {


            Log.d("QAResponseJob", "requestToDoAnimation.");
            AnimationExpressionHelper ExpressionHelper = new AnimationExpressionHelper();

        AnimationEngineParameterGroup AnimParam = ExpressionHelper.GetAnimationEngineParameterTypes(JsonAnimation,  Response);


        ArrayList<AnimationEngineParameterGroup> animationGroup = new ArrayList<AnimationEngineParameterGroup>();

        if(AnimationEngine.IsHalfBlink)
        {
        animationGroup.add(ExpressionHelper.GetBlinkCorrectionStraight());
        }


        animationGroup.add(AnimParam);

        UpdateAnimationsToEngineBuffer( animationGroup);

        //Restart animation state machine during job resume or when state machine finished
        if(STATE == JOB_STATE.NA || CurrentAnimationEngineState == AnimationEngineStates.NA)
        {
        STATE = JOB_STATE.INITIALIZED;
        CurrentAnimationEngineState = AnimationEngineStates.SEND_MOTION_COMMAND;
        Resume();
        }

            Log.d("QAResponseJob", "requestToDoAnimation done");
        }

        }


