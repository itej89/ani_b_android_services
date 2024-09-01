//
//  AnimationEngine.swift
//  BoltBot
//
//  Created by Uday on 19/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package FrameworkInterface;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.CGAffineTransform;
import Framework.DataTypes.Delegates.AnimationActionCreatorConvey;
import Framework.DataTypes.TransformMatrixToValueConvertor;
import Framework.DataTypes.TransformValues;
import Framework.DataTypes.UIViewAnimationOptions;
import Framework.DataTypes.UnitConvertor;
import Framework.AnimationActionCreator;
import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AnimationPositions;
import Framework.DataTypes.Constants.AnimationTypes;
import Framework.DataTypes.Constants.CircularPathDirection;
import Framework.DataTypes.Constants.JOB_STATE;
import Framework.DataTypes.Delegates.JobConvey;
import Framework.DataTypes.Delegates.AnimationEngineConvey;
import Framework.DataTypes.ImageAnimationState;
import Framework.DataTypes.ImageAnimationTransition;
import Framework.DataTypes.Job;
import Framework.DataTypes.MotionAnimationTransition;
import Framework.DataTypes.MotorAnimationState;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.AnimationEngineParameterType;
import FrameworkInterface.DataTypes.Constants.AnimationOnPauseRestartAction;
import FrameworkInterface.DataTypes.Delegates.AnimationParameterTypeDelegates;
import FrameworkInterface.DataTypes.Delegates.ChoreogramBindings;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Delegates.KineticsResponseConvey;

import static Framework.DataTypes.Constants.CircularPathDirection.Direction.clockwise;

public class AnimationEngine extends Job implements KineticsResponseConvey, Animation.AnimationListener, AnimationActionCreatorConvey
{

    public  UUID TAskID= UUID.randomUUID();

    public static Boolean IsHalfBlink = false;

    public boolean IsPaused = true;

    public Boolean ShouldAutoTerinateJob = true;
    
    ArrayList<ParcelUuid> WaitingForKineticsRequestAck = new ArrayList<ParcelUuid>();

    public void CommsLost() {
        super.delegate.notify_LostResource( ID);
    }
    
    private boolean notifyAfterAnimationFinished = false;

    public ChoreogramBindings delChoreogramBinding = null;
    private AnimationParameterTypeDelegates delNotifyAnimFinish = null;
    public  AnimationEngineConvey delAnimationEngineConvey = null;

    public boolean ValidateAcknowledgement(UUID _Acknowledgement){
        for (ParcelUuid ack: WaitingForKineticsRequestAck
             ) {
            if(_Acknowledgement.toString().equals(ack.getUuid().toString())) {
                WaitingForKineticsRequestAck.remove( ack);
                return true;
            }
        }
        return false;
    }

    public void MachineResponseRecieved(ArrayList<KineticsResponse> responeData, UUID _Acknowledgement) {
        Log.d("AE : "+Name, "MachineResponseRecieved ACK:"+_Acknowledgement.toString());
        if(WaitingForKineticsRequestAck.size() > 0){
        if(ValidateAcknowledgement(_Acknowledgement)){
//            WaitingForKineticsRequestAck.clear();
            Log.d("AE : "+Name, "MACH ACK Recieved");
            if(WaitingForKineticsRequestAck.size() == 0 && (CurrentAnimationEngineState == AnimationEngineStates.SEND_TRIGGER || CurrentAnimationEngineState == AnimationEngineStates.START_ANIMATION))
            {
                Log.d("AE : "+Name, "MACH ACK Trig");
                super.delegate.notify_NextStep( ID);
            }
        }
        else {

            Log.d("AE : "+Name, "MachineResponseRecieved not matched , que size : "+WaitingForKineticsRequestAck.size());
        }
        }
        Log.d("AE : "+Name, "MachineResponseRecieved done.");
    }
    
    
    Integer ImageAnimationSemaphore = 0;
    
    public enum AnimationEngineStates { NA, SEND_MOTION_COMMAND, SEND_TRIGGER, START_ANIMATION, SEND_NEXT_MOTION_COMMAND, SEND_WAIT_TRIGGER ,FINALIZE}
    public AnimationEngineStates CurrentAnimationEngineState = AnimationEngineStates.NA;
    
    
    //ExpressionBuffer contains a list of expressions (each expression is a list of  "grouped Animations" ex: Blink and strainght)
    ArrayList<AnimationEngineParameterGroup> ExpressionBuffer = new ArrayList<AnimationEngineParameterGroup>();
    Integer ExpressionBufferParameterIndexer = 0;



   public  Map<AnimationObject, View> Views = new HashMap<AnimationObject, View>();
   public ArrayList<AnimationObject> Motors = new ArrayList<AnimationObject>();
    
    
   public void EmptyExpressionBuffer()
    {
        ExpressionBuffer.clear();
        ExpressionBufferParameterIndexer = 0;
    }
    
   public Boolean ReadyEngineToResume()
    {
        Boolean Status = false;
        if(ExpressionBuffer.size() > 0)
        {
            if(ExpressionBufferParameterIndexer == 0)
            {
                Status = true;
            }
            else
            if(ExpressionBufferParameterIndexer > 0)
            {
                if(ExpressionBuffer.get(0).OnPauseAction == AnimationOnPauseRestartAction.DESTROY)
                {

                    ExpressionBuffer.remove( 0);
                    System.gc();
                    ExpressionBufferParameterIndexer = 0;
                    if(ExpressionBuffer.size() > 0)
                    {
                        Status = true;
                    }
                }
                else
                    if(ExpressionBuffer.get(0).OnPauseAction == AnimationOnPauseRestartAction.RESTART)
                    {
                        ExpressionBufferParameterIndexer = 0;
                        Status = true;
                    }
            }
        }
        
        if(Status)
        {
            WaitingForKineticsRequestAck.clear();
        }
        else
        {
            CurrentAnimationEngineState  = AnimationEngineStates.NA;
        }
        return Status;
    }
    
 
    
    public AnimationEngineParameterType GetCurrentExpressionParameterType()
    {

        return ExpressionBuffer.get(0).Expressions.get(ExpressionBufferParameterIndexer);
    }
    
    public AnimationPositions GetCurrentAnimaitonPosition()
    {
        try {
            return ExpressionBuffer.get(0).Expressions.get(ExpressionBufferParameterIndexer).animationPosition;
        }
        catch (Exception e)
        {
            Log.d(Name, "OUT OF BOUNDS");
        }
            return  null;
    }
    
    public Boolean AreAllAnimationsFinished()
    {
        if(EmptyBufferFromIndex && EmptyBufferIndex > 0)
        {
            while (ExpressionBuffer.size() > EmptyBufferIndex)
            {
                ExpressionBuffer.get(EmptyBufferIndex).destroy();
                ExpressionBuffer.remove(EmptyBufferIndex);
            }

            EmptyBufferFromIndex = false;
            EmptyBufferIndex = -1;
        }

        if(ExpressionBuffer.size() > 1 || (ExpressionBuffer.size() == 1 && ExpressionBuffer.get(0).Expressions.size()-1 > ExpressionBufferParameterIndexer))
        {
            return false;
        }
        return true;
    }

    public Boolean IsIDLE()
    {
        if(CurrentAnimationEngineState == AnimationEngineStates.NA)
            return  true;

        return false;
    }

public Boolean IsFistExpressionAlreadyPerformed()
{
    if(ExpressionBuffer.isEmpty()) {
        return  false;
    }

        if (ExpressionBuffer.get(0).Expressions == null || ExpressionBuffer.get(0).Expressions.size() == 0) {
            Log.d(Name, "First Expression  deleted");
            return true;
        }


    return  false;
}
    
    public Boolean MoveAnimationParameterIndexUp()
    {
        if(ExpressionBuffer.size() > 0)
        {

            
            if(ExpressionBuffer.get(0).Expressions.size()-1 > ExpressionBufferParameterIndexer)
            {
                ExpressionBufferParameterIndexer = ExpressionBufferParameterIndexer + 1;
                return true;
            }
            else
            {
                ExpressionBufferParameterIndexer = 0;
                ExpressionBuffer.remove( 0);





                if(ExpressionBuffer.size() > 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
    
    public Boolean IsBufferEmpty()
    {
        if(ExpressionBuffer.size() > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public Boolean IsBufferGoingEmpty()
    {
        if(ExpressionBuffer.size() > 2)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void InsertAnimationAt(ArrayList<AnimationEngineParameterGroup> PositionData, Integer Index)
    {

            for(AnimationEngineParameterGroup group : PositionData)
            {
                for(AnimationEngineParameterType position : group.Expressions)
                {
                    
                    position.animationPosition = new AnimationPositions();
                    
                    position.animationPosition.parseJson( position.Json);
                    
                    position.animationPosition.sentance = position.sentance;
                    position.animationPosition.audio = position.audio;
                    position.animationPosition.StartSec = position.StartSec;
                    position.animationPosition.EndSec = position.EndSec;
                }
                
            }

            if(ExpressionBuffer.size() > Index)
            {

                if(Index == 0)
                {
                    ExpressionBufferParameterIndexer = 0;
                }
                ExpressionBuffer.addAll(Index, PositionData);
            }
            else
            {
                ExpressionBufferParameterIndexer = 0;
                ExpressionBuffer.addAll(PositionData);
            }


    }
    
    public void PushImmediateAnimation(ArrayList<AnimationEngineParameterGroup> PositionData)
    {
        if(ExpressionBuffer.size() > 0)
        {
            for(AnimationEngineParameterGroup group : PositionData)
            {
                for(AnimationEngineParameterType position : group.Expressions)
                {
                    
                    position.animationPosition = new AnimationPositions();
                    
                    position.animationPosition.parseJson(position.Json);
                    
                    position.animationPosition.sentance = position.sentance;
                }
                
            }
            
            ExpressionBuffer.addAll(0, PositionData);
        }
    }
    
    public void UpdateAnimationsToEngineBuffer(ArrayList<AnimationEngineParameterGroup> PositionData)
    {
        for(AnimationEngineParameterGroup group : PositionData)
        {
            for(AnimationEngineParameterType position : group.Expressions)
            {
                position.animationPosition = new AnimationPositions();

                position.animationPosition.parseJson( position.Json);

                position.animationPosition.sentance = position.sentance;
            }
            
            ExpressionBuffer.add(group);
        }
    }

    public boolean EmptyBufferFromIndex = false;
    public int EmptyBufferIndex = -1;

    public void RemoveBufferedDataWithID(UUID ID)
    {
        for(int i=0; i<ExpressionBuffer.size(); i++)
        {
            if(ExpressionBuffer.get(i).AnimationGroupID == ID)
            {
                ExpressionBuffer.remove(i);
                break;
            }
        }
    }

    public void ClearBufferedData()
    {
        ExpressionBuffer.clear();
        ExpressionBufferParameterIndexer = 0;
    }

    public void DisableEmptyBufferFromIndex()
    {
        EmptyBufferIndex = -1;
        EmptyBufferFromIndex = false;
    }


    public void EmptyBufferFromIndex(Integer Index)
    {
        EmptyBufferIndex = Index;
        EmptyBufferFromIndex = true;
    }
//
//    public void InitializeEngine(ArrayList<AnimationEngineParameterGroup> PositionData, Map<AnimationObject, View> _Views, ArrayList<AnimationObject> _Motors)
//    {
//
//            UpdateAnimationsToEngineBuffer(PositionData);
//
//
//        Views = _Views;
//        Motors = _Motors;
//
//        CurrentAnimationEngineState  = AnimationEngineStates.SEND_MOTION_COMMAND;
//
//    }

    public void InitializeEngine(ArrayList<AnimationEngineParameterGroup> PositionData)
    {

        UpdateAnimationsToEngineBuffer(PositionData);

        //Condition added for pause and resume case.. where index 0 animation is altready performed, but not removed from buffer
        while(IsFistExpressionAlreadyPerformed())
        {
            MoveAnimationParameterIndexUp();
        }

        CurrentAnimationEngineState  = AnimationEngineStates.SEND_MOTION_COMMAND;

    }
  
    
    
    public void TakeOverEngineResources(JobConvey _delegate) {
        KineticComms.Instance.SetKineticsResposeListener(this);
        super.TakeOverResources(_delegate);
    }
    
   
    public void PauseEngine() {
        
        KineticComms.Instance.SetKineticsResposeListener(null);
        CurrentAnimationEngineState = AnimationEngineStates.NA;
        super.Pause();
    }
    
    public void ResumeEngine() {
        DoSTEP();
    }


    
    public void MachiResponseTimeout(ArrayList<KineticsResponse> partialResponse, UUID _Acknowledgement) {
        Log.d("AE : "+Name, "MACH Respose Timeout Recieved");
        if(WaitingForKineticsRequestAck.size() > 0){
            if(ValidateAcknowledgement(_Acknowledgement)){
                Log.d("AE : "+Name, "MACH ACK Recieved");
                if(WaitingForKineticsRequestAck.size() == 0 && (CurrentAnimationEngineState == AnimationEngineStates.SEND_TRIGGER || CurrentAnimationEngineState == AnimationEngineStates.START_ANIMATION))
                {
                    Log.d("AE : "+Name, "MACH ACK Trig");
                    super.delegate.notify_NextStep( ID);
                }
            }
        }
    }
    
    void DoSTEP(){
        
        if(STATE == JOB_STATE.RUNNING)
        {
        
        switch(CurrentAnimationEngineState) {
        case SEND_MOTION_COMMAND: {
            IsPaused = false;
            Log.d("AE : "+Name, "SEND_MOTION_COMMAND");
            if (IsMotionCommandPresent(GetCurrentAnimaitonPosition()) == true) {

                ArrayList<KineticsRequest> Comamnd = GetMotorCommand(GetCurrentAnimaitonPosition(), Motors);

                //This should get triggered by next motion acknowledgement
                CurrentAnimationEngineState = AnimationEngineStates.SEND_TRIGGER;

                Log.d("AE", "PARAM SET");
                WaitingForKineticsRequestAck.add(KineticComms.Instance.SetParameters(Comamnd));
                Log.d("AE", "PARAM SENDIGN");

            } else {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(GetCurrentAnimaitonPosition().audio == "") {

//                            while((WaitingForKineticsRequestAck.size() > 0 || ImageAnimationSemaphore > 0) && CurrentAnimationEngineState != AnimationEngineStates.NA)
//                            {
//                                try {
//
//                                    Thread.sleep(10);
//                                }
//                                catch (Exception e){}
//                            }

                            while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                try {

                                    Thread.sleep(10);
                                } catch (Exception e) {

                                }
                            }
                        }
                        CurrentAnimationEngineState = AnimationEngineStates.START_ANIMATION;
                        AnimationEngine.super.delegate.notify_NextStep(ID);


            }
            });
                t.run();

            }
        }
            break;
        case SEND_TRIGGER: {
            Log.d("AE : "+Name, "SEND_TRIGGER");

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                                CurrentAnimationEngineState = AnimationEngineStates.START_ANIMATION;

                                switch (GetCurrentExpressionParameterType().TriggerType) {
                                    case WAIT_AND_MOVE:
//                                        while((WaitingForKineticsRequestAck.size() > 0 || ImageAnimationSemaphore > 0) && CurrentAnimationEngineState != AnimationEngineStates.NA)
//                                        {
//                                            try {
//
//                                                Thread.sleep(10);
//                                            }
//                                            catch (Exception e){}
//                                        }
                                        if(GetCurrentAnimaitonPosition().audio == "") {
                                            while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                                try {

                                                    Thread.sleep(10);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }

                                        Log.d("AE : "+Name, "SEND_TRIGGER StartMotion");
                                        ParcelUuid _UUID =  KineticComms.Instance.StartMotion();
                                        WaitingForKineticsRequestAck.add(_UUID);
                                        Log.d("AE : "+Name, "SEND_TRIGGER ACK ID : "+_UUID.getUuid().toString());
                                        break;
                                    case INSTANT_MOVE:
                                        if(GetCurrentAnimaitonPosition().audio == "") {
                                            while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                                try {

                                                    Thread.sleep(10);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                        Log.d("AE : "+Name, "SEND_TRIGGER StartInstantMotion");
                                        WaitingForKineticsRequestAck.add(KineticComms.Instance.StartInstantMotion());
                                        break;
                                }

                        }
                    });
                    t.run();
            }

            break;
        case START_ANIMATION: {
            Log.d("AE : "+Name, "START_ANIMATION");
            ImageAnimationSemaphore = 0;

            if( notifyAfterAnimationFinished )
            {

                if(delNotifyAnimFinish != null)
                {
                    delNotifyAnimFinish.AnimationFinished();

                    delNotifyAnimFinish = null;
                }

                notifyAfterAnimationFinished = false;
            }


            AnimationEngine.IsHalfBlink = GetCurrentExpressionParameterType().IsHalfBlink;

            Animate(GetCurrentAnimaitonPosition());

            if(!GetCurrentAnimaitonPosition().sentance.equals("")){
                ArticulationManager.Instance.PlayWavData(GetCurrentAnimaitonPosition().sentance,   1.0f,  1.2f);
            }

            if(!GetCurrentAnimaitonPosition().audio.equals("")){
                ArticulationManager.Instance.PlaySoundSegment(GetCurrentAnimaitonPosition().audio,  GetCurrentAnimaitonPosition().StartSec,  GetCurrentAnimaitonPosition().EndSec ,0.3f, 1.2f);
            }

            if (GetCurrentExpressionParameterType().delegate != null) {
                notifyAfterAnimationFinished = true;
                delNotifyAnimFinish = GetCurrentExpressionParameterType().delegate;
            }
            else
            {
                notifyAfterAnimationFinished = false;
                delNotifyAnimFinish = null;
            }


            if (AreAllAnimationsFinished()) {

                CurrentAnimationEngineState = AnimationEngineStates.FINALIZE;
            } else {
                CurrentAnimationEngineState = AnimationEngineStates.SEND_NEXT_MOTION_COMMAND;
                MoveAnimationParameterIndexUp();

            }



            if (CurrentAnimationEngineState == AnimationEngineStates.SEND_NEXT_MOTION_COMMAND) {
                super.delegate.notify_NextStep(ID);
            }
//            else
//            if (CurrentAnimationEngineState == AnimationEngineStates.FINALIZE)
//            {
//                Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    while((ImageAnimationSemaphore > 0) && CurrentAnimationEngineState != AnimationEngineStates.NA)
////                    {
////                        try {
////                            Thread.sleep(10);
////                        }
////                        catch (Exception e){}
////                    }
//
//                    AnimationEngine.super.delegate.notify_NextStep(ID);
//                }
//            });
//
//                t.run();
//
//
//            }

        }
            break;
        case SEND_NEXT_MOTION_COMMAND: {
            Log.d("AE : "+Name, "SEND_NEXT_MOTION_COMMAND");
            if (IsMotionCommandPresent(GetCurrentAnimaitonPosition()) == true)
            {

                //This should get triggered by next animation completion acknowledgement
                CurrentAnimationEngineState = AnimationEngineStates.SEND_WAIT_TRIGGER;

                ArrayList<KineticsRequest> Comamnd = GetMotorCommand(GetCurrentAnimaitonPosition(), Motors);

                WaitingForKineticsRequestAck.add(KineticComms.Instance.SetParameters(Comamnd));

            }
            else
                {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(GetCurrentAnimaitonPosition().audio == "") {

//                                while(ImageAnimationSemaphore > 0)
//                                {
//                                    try {
//
//                                        Thread.sleep(10);
//                                    }
//                                    catch (Exception e){}
//                                }

                                while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                    try {

                                        Thread.sleep(10);
                                    } catch (Exception e) {

                                    }
                                }
                            }

                            if(CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                CurrentAnimationEngineState = AnimationEngineStates.START_ANIMATION;
                            }
                        }
                    });
                    t.run();
            }
        }
            break;
        case SEND_WAIT_TRIGGER: {
            Log.d("AE : "+Name, "SEND_WAIT_TRIGGER");

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {


                    CurrentAnimationEngineState = AnimationEngineStates.START_ANIMATION;

                    switch (GetCurrentExpressionParameterType().TriggerType) {
                        case WAIT_AND_MOVE:
//                            while((WaitingForKineticsRequestAck.size() > 0 || ImageAnimationSemaphore > 0) && CurrentAnimationEngineState != AnimationEngineStates.NA)
//                            {
//                                try {
//
//                                    Thread.sleep(10);
//                                }
//                                catch (Exception e){}
//                            }
                            if(GetCurrentAnimaitonPosition().audio == "") {
                                while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                    try {

                                        Thread.sleep(10);
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            WaitingForKineticsRequestAck.add(KineticComms.Instance.StartMotion());
                            break;
                        case INSTANT_MOVE:
                            if(GetCurrentAnimaitonPosition().audio == "") {
                                while (delChoreogramBinding != null && delChoreogramBinding.ShouldWaitToTrigger(GetCurrentAnimaitonPosition().StartSec) && CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                    try {

                                        Thread.sleep(10);
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            WaitingForKineticsRequestAck.add(KineticComms.Instance.StartInstantMotion());
                            break;
                    }

                }
            });
            t.run();
        }



          break;

         case FINALIZE: {
            Log.d("AE : "+Name, "FINALIZE");

             Thread t = new Thread(new Runnable() {
                 @Override
                 public void run() {


                     CurrentAnimationEngineState = AnimationEngineStates.START_ANIMATION;

//                     while((WaitingForKineticsRequestAck.size() > 0 || ImageAnimationSemaphore > 0) && CurrentAnimationEngineState != AnimationEngineStates.NA)
//                             {
//                                 try {
//
//                                     Thread.sleep(10);
//                                 }
//                                 catch (Exception e){}
//                             }

                             if(CurrentAnimationEngineState != AnimationEngineStates.NA) {
                                 if (AreAllAnimationsFinished()) {
                                     CurrentAnimationEngineState = AnimationEngineStates.NA;
                                     if (ShouldAutoTerinateJob) {
                                         AnimationEngine.super.delegate.notify_Finish(ID);
                                         if (delAnimationEngineConvey != null)
                                             delAnimationEngineConvey.AnuimationEngineFinalized();
                                     }

                                     if (notifyAfterAnimationFinished) {
                                         if (delNotifyAnimFinish != null) {
                                             delNotifyAnimFinish.AnimationFinished();

                                             delNotifyAnimFinish = null;
                                         }

                                         notifyAfterAnimationFinished = false;
                                     }

                                 } else {
                                     CurrentAnimationEngineState = AnimationEngineStates.SEND_NEXT_MOTION_COMMAND;
                                     AnimationEngine.super.delegate.notify_NextStep(ID);

                                 }
                             }
                 }
             });
             t.run();

        }
            break;
        default:
            break;
        }
        
        }
    }
    
  public   ArrayList<KineticsRequest> GetMotorCommand(AnimationPositions Position, ArrayList<AnimationObject> Motors)
    {

        
        ArrayList<KineticsRequest> StateCommandSet = new ArrayList<KineticsRequest>();
        ArrayList<KineticsRequest> TurnStateCommandSet = ((MotorAnimationState)Position.State.StateSet.get(AnimationObject.Motor_Turn)).GetStateCommandString( Actuator.TURN);
        StateCommandSet.addAll(TurnStateCommandSet);
        ArrayList<KineticsRequest> LiftStateCommandSet = ((MotorAnimationState)Position.State.StateSet.get(AnimationObject.Motor_Lift)).GetStateCommandString(Actuator.LIFT);
        StateCommandSet.addAll(LiftStateCommandSet);
        ArrayList<KineticsRequest> LeanStateCommandSet = ((MotorAnimationState)Position.State.StateSet.get(AnimationObject.Motor_Lean)).GetStateCommandString(Actuator.LEAN);
        StateCommandSet.addAll(LeanStateCommandSet);
        ArrayList<KineticsRequest> TiltStateCommandSet = ((MotorAnimationState)Position.State.StateSet.get(AnimationObject.Motor_Tilt)).GetStateCommandString(Actuator.TILT);
        StateCommandSet.addAll(TiltStateCommandSet);
       
        if(TurnStateCommandSet.size() > 0){
            StateCommandSet.addAll(((MotionAnimationTransition)Position.Transition.TransitionSet.get(AnimationObject.Motor_Turn)).GetTransitionCommandString(Actuator.TURN));
        }
         if(LiftStateCommandSet.size() > 0){
             StateCommandSet.addAll(((MotionAnimationTransition)Position.Transition.TransitionSet.get(AnimationObject.Motor_Lift)).GetTransitionCommandString(Actuator.LIFT));
        }
         if(LeanStateCommandSet.size() > 0){
             StateCommandSet.addAll(((MotionAnimationTransition)Position.Transition.TransitionSet.get(AnimationObject.Motor_Lean)).GetTransitionCommandString(Actuator.LEAN));
        }
         if(TiltStateCommandSet.size() > 0){
             StateCommandSet.addAll(((MotionAnimationTransition)Position.Transition.TransitionSet.get(AnimationObject.Motor_Tilt)).GetTransitionCommandString(Actuator.TILT));
        }
     
        
        
        return StateCommandSet;
    }



    
    void Animate(final AnimationPositions Position)
   {
//
//
//      //  DispatchQueue.main.async { [weak self] in
//
//
            for(final Map.Entry<AnimationObject, View> view : Views.entrySet())
            {


              final  AnimationObject Tag =  view.getKey();



                ImageAnimationTransition transition = ((ImageAnimationTransition)Position.Transition.TransitionSet.get(Tag));

                ArrayList<UIViewAnimationOptions.Options>  options = transition.AnimationCurveType;

               final Integer Timing =  ((ImageAnimationTransition)Position.Transition.TransitionSet.get(Tag)).Duration;

               final Integer Delay =  ((ImageAnimationTransition)Position.Transition.TransitionSet.get(Tag)).Delay;


               // AnimationActionCreator.instance.SetDefault(image: Views[view.key]!)


                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorX.second){

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Views.get(view.getKey()).setPivotX((((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorX.first).floatValue());

                    }});

                }

                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorY.second){

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Views.get(view.getKey()).setPivotY((((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorY.first).floatValue());

                        }});

                }


                Boolean IsTransformOverlay = false;
                Boolean IsTransformed = false;
                Boolean IsCircularPath = false;
                Boolean IsIdentity = false;


                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnimationKind == AnimationTypes.Type.Tranformation){
                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.a.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.b.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.c.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.d.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.tx.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.ty.second  )
                    {

                        IsTransformed = true;
                    }
                }

                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnimationKind == AnimationTypes.Type.TransformOverlay){
                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.a.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.b.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.c.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.d.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.tx.second && ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.ty.second  )
                    {

                        IsTransformOverlay = true;
                    }
                }

                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnimationKind == AnimationTypes.Type.CircularPath){

                    IsCircularPath = true;
                }

                if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnimationKind == AnimationTypes.Type.Identity){

                    IsIdentity = true;
                }

                if(IsTransformed || IsTransformOverlay || IsIdentity || IsCircularPath)
                {
                    ImageAnimationSemaphore  = (ImageAnimationSemaphore) + 1;

                     MachinePositionContext.Instance.setElementParameters(Tag, ((ImageAnimationState)Position.State.StateSet.get(Tag)),  ((ImageAnimationTransition)Position.Transition.TransitionSet.get(Tag)));
                }



                if(IsTransformed || IsIdentity || IsTransformOverlay)
                {

                     Float alpha  = 1.0f;
                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).opacity.second) {

                        alpha = (((ImageAnimationState) (Position.State.StateSet.get(Tag))).opacity.first.floatValue());

                    }

                    final Float alphaProp = alpha;

                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorX.second)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Views.get(view.getKey()).setPivotX(Views.get(view.getKey()).getWidth() * ((ImageAnimationState) (Position.State.StateSet.get(Tag))).AnchorX.first.floatValue());
                            }});
                    }
                    else
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Views.get(view.getKey()).setPivotX(Views.get(view.getKey()).getWidth() * 0.5f);
                            }});
                    }

                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).AnchorY.second)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Views.get(view.getKey()).setPivotY(Views.get(view.getKey()).getHeight() * ((ImageAnimationState) (Position.State.StateSet.get(Tag))).AnchorY.first.floatValue());
                            }});
                    }
                    else
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Views.get(view.getKey()).setPivotY(Views.get(view.getKey()).getHeight() * 0.5f);
                            }});
                    }


                        if(IsTransformed)
                                {
                                    CGAffineTransform transform = new CGAffineTransform();

                                    transform.a = (((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.a.first.floatValue());

                                    transform.b = (((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.b.first.floatValue());

                                    transform.c = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.c.first.floatValue();

                                    transform.d = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.d.first.floatValue();

                                    transform.tx = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.tx.first.floatValue();

                                    transform.ty = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.ty.first.floatValue();



                                   final TransformValues transform2D = TransformMatrixToValueConvertor.GetValuesFromMatrix(transform);

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {


                                            //Multiply by -1 for ios rotaion direction
                                            Views.get(view.getKey()).animate().rotation(UnitConvertor.RadiasToDegree(transform2D.RotationInRadians) * -1).scaleX(transform2D.ScaleX).scaleY(transform2D.ScaleY).translationX(transform2D.Tx).translationY(transform2D.Ty).setDuration(Timing).setStartDelay(Delay).setInterpolator(new DecelerateInterpolator()).alpha(alphaProp).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            AnimationCompleted();
                                        }
                                    });

                                        }
                                    });

                                }
                                else if(IsTransformOverlay == true)
                                {
                                    //In android Overlay (Code commented) not applicable as we are using parameter animation insted aof matrices.

                                    //This code is samse as "Transform" animation Kind

                                    CGAffineTransform transform = new CGAffineTransform();

                                    transform.a = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.a.first.floatValue();

                                    transform.b = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.b.first.floatValue();

                                    transform.c = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.c.first.floatValue();

                                    transform.d = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.d.first.floatValue();

                                    transform.tx = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.tx.first.floatValue();

                                    transform.ty = ((ImageAnimationState)Position.State.StateSet.get(Tag)).Matrix.ty.first.floatValue();


                                    final TransformValues transform2D = TransformMatrixToValueConvertor.GetValuesFromMatrix(transform);

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {

                                            //Multiply by -1 for ios rotaion direction
                                            Views.get(view.getKey()).animate().rotation(UnitConvertor.RadiasToDegree(transform2D.RotationInRadians) * -1).scaleX(transform2D.ScaleX).scaleY(transform2D.ScaleY).translationX(transform2D.Tx).translationY(transform2D.Ty).setDuration(Timing).setStartDelay(Delay).setInterpolator(new DecelerateInterpolator()).alpha(alphaProp).withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AnimationCompleted();
                                                }
                                            });

                                        }
                                    });

                                }
                                else if(IsIdentity == true)
                                {
                                    AnimationActionCreator.instance.SetDefault((Views.get(view.getKey())), this, Timing, Delay);
                                }



                }
                else if(IsCircularPath)
                {
                    Boolean Direction = true;
                    if(((ImageAnimationState)Position.State.StateSet.get(Tag)).CircularPath.Direction.first == clockwise)
                    {
                        Direction = true;
                    }
                    else  if(((ImageAnimationState)Position.State.StateSet.get(Tag)).CircularPath.Direction.first == CircularPathDirection.Direction.anitiClockwise)
                    {
                        Direction = false;
                    }


//                    int abs    = ArcTranslateAnimation.ABSOLUTE;
//
//                    ArcTranslateAnimation arcAnim = new ArcTranslateAnimation(
//                            abs, startX, abs, finalX, abs, startY, abs, finalY
//                    );
//
//                    arcAnim.setDuration(3000);
//                    arcAnim.setStartOffset(Delay);
//                    arcAnim.setFillAfter(true);
//                    Views.get(view.getKey()).setAnimation(graphicAnimation);
//
//                    let circlePath = UIBezierPath(arcCenter: CGPoint(x: (Position.State.StateSet.get(Tag) as! ImageAnimationState).CircularPath.MidX.keys.first!, y: (Position.State.StateSet.get(Tag) as! ImageAnimationState).CircularPath.MidY.keys.first!), radius: CGFloat((Position.State.StateSet.get(Tag) as! ImageAnimationState).CircularPath.Radius.keys.first!), startAngle: CGFloat((Position.State.StateSet.get(Tag) as! ImageAnimationState).CircularPath.StartAngle.keys.first!)*(CGFloat(Double.pi)/180), endAngle:CGFloat((Position.State.StateSet.get(Tag) as! ImageAnimationState).CircularPath.EndAngle.keys.first!)*(CGFloat(Double.pi)/180), clockwise: Direction)
//
//                    let animation = CAKeyframeAnimation(keyPath: "position");
//                    animation.beginTime = CACurrentMediaTime() + CFTimeInterval(Delay)
//                    animation.delegate = self
//                    animation.duration = CFTimeInterval(Timing)
//                    animation.timingFunction = CAMediaTimingFunction(name: AnimationEasingTypesToCAMediaTimingFunction[(Position.Transition.TransitionSet.get(Tag) as! ImageAnimationTransition).KeyframeAnimation_EasingFunction]!)
//                    animation.fillMode = AnimationFillModesToCGPathFillMode[(Position.Transition.TransitionSet.get(Tag) as! ImageAnimationTransition).KeyframeAnimation_FillMode]!
//                    animation.path = circlePath.cgPath
//
//                    Views[view.key]?.layer.add(animation, forKey: nil)
                //}


            }
        }
    }


    //AnimationActionCreatorConvey

    //End Of AnimationActionCreatorConvey
    public  void SetDefaultCompleted(AnimationObject Tag)
    {
        AnimationCompleted();
    }
    //AnimationListener
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

       AnimationCompleted();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    //End of AnimationListener
    

   
    

    
    void AnimationCompleted() {
        ImageAnimationSemaphore = ImageAnimationSemaphore-1;

        ExecuteNextAnimComplete();

    }

    public void ExecuteNextAnimComplete()
    {
        if(ImageAnimationSemaphore == 0){
            new AnimationAsyncNextStep().execute(super.delegate);
        }
    }

    private class AnimationAsyncNextStep extends AsyncTask<JobConvey, Integer, Long> {
        protected Long doInBackground(JobConvey... convey)
        {
            convey[0].notify_NextStep(ID);
           return  0L;
        }

        protected void onProgressUpdate(Integer progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }



    Boolean IsMotionCommandPresent(AnimationPositions position) {
            if(((MotorAnimationState)position.State.StateSet.get(AnimationObject.Motor_Turn)).Angle.second == true ||
                ((MotorAnimationState)position.State.StateSet.get(AnimationObject.Motor_Lift)).Angle.second == true ||
                ((MotorAnimationState)position.State.StateSet.get(AnimationObject.Motor_Lean)).Angle.second == true ||
                ((MotorAnimationState)position.State.StateSet.get(AnimationObject.Motor_Tilt)).Angle.second == true)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public void haltEngine() {

        CurrentAnimationEngineState = AnimationEngineStates.NA;
    }


@Override
    public  void destroy()
    {
//        Log.d(Name, "Destroyed");
//        if(ExpressionBuffer != null)
//        {
//            for(int i=0; i< ExpressionBuffer.size(); i++)
//            {
//                ExpressionBuffer.get(i).destroy();
//            }
//            ExpressionBuffer.clear();
//            System.gc();
//        }

    }

}
