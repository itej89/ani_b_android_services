//
//  MachineCurrentPositionScreenShot.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 06/05/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface;

import android.content.Context;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AnimationPositions;
import Framework.DataTypes.ImageAnimationState;
import Framework.DataTypes.ImageAnimationTransition;
import Framework.DataTypes.MotionAnimationTransition;
import Framework.DataTypes.MotorAnimationState;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestForActuator;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Delegates.KineticsParameterUpdatesConvey;

public class MachinePositionContext implements KineticsParameterUpdatesConvey
{
    private static MachinePositionContext _Instance;


    private MachinePositionContext()
    {
    }

    public static MachinePositionContext Instance = new  MachinePositionContext();

    
    public AnimationPositions CurrentParameters = new AnimationPositions();
    
    private AnimationPositions TemporaryMotionParameters = new AnimationPositions();
    
    public void Initialize()
    {
        KineticComms.Instance.SetKineticsParameterUpdatesListener(this);
    }
    
    private AnimationObject GetAnimationObjectFromKineticsRequest(KineticsRequest request)
    {
        KineticsRequestForActuator actuatorRequest = ((KineticsRequestForActuator)request);
        if(actuatorRequest != null)
        {
            AnimationObject animationObject = AnimationObject.fromInt(actuatorRequest.ActuatorType.getValue());
            if(animationObject != null)
            {
                return animationObject;
            }
        }
        
         return AnimationObject.NA;
    }

    public void removeGraphicElements()
    {
        if(CurrentParameters.State.StateSet.size() > 0)
        {
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeBallLeft);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeBrowLeft);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeLeft);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeLidLeft);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyePupilLeft);


            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeBallRight);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeBrowRight);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeRight);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyeLidRight);
            CurrentParameters.State.StateSet.remove(AnimationObject.Image_EyePupilRight);
        }

        if(CurrentParameters.Transition.TransitionSet.size() > 0) {
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeBallLeft);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeBrowLeft);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeLeft);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeLidLeft);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyePupilLeft);


            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeBallRight);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeBrowRight);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeRight);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyeLidRight);
            CurrentParameters.Transition.TransitionSet.remove(AnimationObject.Image_EyePupilRight);
        }
    }

    public void setElementParameters(AnimationObject animationObjet, ImageAnimationState State, ImageAnimationTransition Transition)
    {
        CurrentParameters.State.StateSet.put(animationObjet, State);
        CurrentParameters.Transition.TransitionSet.put(animationObjet, Transition);
    }

    //KineticsParameterUpdatesConvey
    
    //sets the loaded parameters for the actuators
    public void ParameterUpdated(KineticsRequest request)
    {
        switch(request.RequestType) {
        case ANG: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotorAnimationState) TemporaryMotionParameters.State.StateSet.get(animationObject)).setKineticsState(request);
            }
            break;
        }
        case TMG: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case DEL: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case FRQ: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case DMP: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case VEL: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case EAS: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        case INO: {
            AnimationObject animationObject = GetAnimationObjectFromKineticsRequest(request);
            if (animationObject != AnimationObject.NA) {
                ((MotionAnimationTransition) TemporaryMotionParameters.Transition.TransitionSet.get(animationObject)).setKineticsTransition(request);
            }
            break;
        }
        default:
            break;
        }
    }

    public void ParametersSetSuccessfully()
    {
        CurrentParameters.State.StateSet.put(AnimationObject.Motor_Turn,  TemporaryMotionParameters.State.StateSet.get(AnimationObject.Motor_Turn));
        CurrentParameters.State.StateSet.put(AnimationObject.Motor_Lift,  TemporaryMotionParameters.State.StateSet.get(AnimationObject.Motor_Lift));
        CurrentParameters.State.StateSet.put(AnimationObject.Motor_Lean,  TemporaryMotionParameters.State.StateSet.get(AnimationObject.Motor_Lean));
        CurrentParameters.State.StateSet.put(AnimationObject.Motor_Tilt,  TemporaryMotionParameters.State.StateSet.get(AnimationObject.Motor_Tilt));
        
        CurrentParameters.Transition.TransitionSet.put(AnimationObject.Motor_Turn,  TemporaryMotionParameters.Transition.TransitionSet.get(AnimationObject.Motor_Turn));
        CurrentParameters.Transition.TransitionSet.put(AnimationObject.Motor_Lift,  TemporaryMotionParameters.Transition.TransitionSet.get(AnimationObject.Motor_Lift));
        CurrentParameters.Transition.TransitionSet.put(AnimationObject.Motor_Lean,  TemporaryMotionParameters.Transition.TransitionSet.get(AnimationObject.Motor_Lean));
        CurrentParameters.Transition.TransitionSet.put(AnimationObject.Motor_Tilt,  TemporaryMotionParameters.Transition.TransitionSet.get(AnimationObject.Motor_Tilt));
    }
    //End of KineticsParameterUpdatesConvey
}
