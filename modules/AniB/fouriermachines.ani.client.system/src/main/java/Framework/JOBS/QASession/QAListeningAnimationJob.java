package Framework.JOBS.QASession;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.JOB_PRIORITY;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.AnimationEngineConvey;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Delegates.QASession.QAListenerJobConvey;
import FrameworkInterface.AnimationEngine;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.Helpers.AnimationExpressionHelper;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class QAListeningAnimationJob extends AnimationEngine implements AnimationEngineConvey
        {

            QAListenerJobConvey delQAListenerJobConvey;

public QAListeningAnimationJob(QAListenerJobConvey delegate) {

        super();
    Name = "QA Listening";
        ShouldAutoTerinateJob = true;
        PRIORITY = JOB_PRIORITY.QAATTENTION;
    delQAListenerJobConvey  =delegate;
    delAnimationEngineConvey = this;
        }

        String  GetRandomAttentionAnimation()
        {
        ArrayList<String> thinking_anims =
           new ArrayList<String>(){{
               add("subtle_straight1");
               add("subtle_straight2");
               add("subtle_straight3");
               add("subtle_straight4");
               add("subtle_straight5");
               add("subtle_straight6");
               add("subtle_straight7");
               add("subtle_straight8");
           }};

            Random rand = new Random();
        Integer index = rand.nextInt(thinking_anims.size());


        String animation = thinking_anims.get(index);

            DB_Local_Store dbHAndler = new DB_Local_Store();

        Expressions_Type thoughtAnimation =   dbHAndler.readExpression(animation);
        return thoughtAnimation.Action_Data;
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

//    final ArrayList<View> elements = UIMAINModuleHandler.Instance.AniUIHandler.GetAllUIElements();
//    final Map<AnimationObject, View> AnimationObjectMap = new HashMap<AnimationObject, View>();
//    ArrayList<AnimationObject> MotionElements = new ArrayList<AnimationObject>() {{
//        add(AnimationObject.Motor_Turn);
//        add(AnimationObject.Motor_Lean);
//        add(AnimationObject.Motor_Lift);
//        add(AnimationObject.Motor_Tilt);
//    }};
//
//
//    Runnable UIRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//            synchronized (this) {
//                for (View view : elements) {
//                    AnimationObjectMap.put(AnimationObject.fromInt(Integer.parseInt((String) view.getTag())), view);
//                }
//                this.notify();
//            }
//        }
//    };
//    new Handler(Looper.getMainLooper()).post(UIRunnable);
//    synchronized (UIRunnable) {
//        Views = AnimationObjectMap;
//        LoadAnimation();
//
//    }
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
                requestToDoAnimation();
            }

        @Override
            public void  TakeOverResources(JobConvey _delegate)
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
       public   void Pause() {
        STATE = JOB_STATE.PAUSED;
        PauseEngine();
        delegate.notify_Finish(ID);
        }



        public void requestToDoAnimation() {

if(STATE != JOB_STATE.PAUSED)
{
            if (IsBufferGoingEmpty()) {

                AnimationExpressionHelper ExpressionHelper = new AnimationExpressionHelper();

                AnimationEngineParameterGroup arrayOfAnimations = ExpressionHelper.GetAnimationEngineParameterTypesWithBlinkAndStraight(GetRandomAttentionAnimation());

                ArrayList<AnimationEngineParameterGroup> animationGroup = new ArrayList<AnimationEngineParameterGroup>();

                if (AnimationEngine.IsHalfBlink) {
                    animationGroup.add(ExpressionHelper.GetBlinkCorrectionStraight());
                }



                animationGroup.add(arrayOfAnimations);
                UpdateAnimationsToEngineBuffer(animationGroup);



            }

            //Restart animation state machine during job resume or when state machine finished
            if (STATE == JOB_STATE.NA || CurrentAnimationEngineState == AnimationEngineStates.NA) {
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
}

            //AnimationEngineConvey
            public    void AnuimationEngineFinalized()
            {
                if(delQAListenerJobConvey != null)
                {
                    delQAListenerJobConvey.QAListenerAnimationFinished();
                }
            }
            //End of AnimationEngineConvey

        }

