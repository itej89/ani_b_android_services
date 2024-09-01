//
//  MotionExpressionHelper.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 06/05/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface.DataTypes.Helpers;


import android.util.Pair;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import Framework.DataTypes.Constants.AnimationTypes;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.AnimationAction;
import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.EmotionEnums;
import Framework.DataTypes.MotorAnimationState;
import FrameworkInterface.DataTypes.AnimationEngineParameterGroup;
import FrameworkInterface.DataTypes.AnimationEngineParameterType;
import FrameworkInterface.DataTypes.Constants.AnimationOnPauseRestartAction;
import FrameworkInterface.DataTypes.Delegates.AnimationParameterTypeDelegates;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import fm.ani.client.db.DB_Local_Store;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;

public class AnimationExpressionHelper {


    public AnimationExpressionHelper() {

    }

    public AnimationEngineParameterGroup GetNapGraphicOnlyAnimation(AnimationParameterTypeDelegates delegate) {
        DB_Local_Store dbHAndler = new DB_Local_Store();

        Expressions_Type napExpression = dbHAndler.readExpression("nap");

        JSONObject napJsonMsgObject = GenericExtentions.parseJSONString(napExpression.Action_Data);

        try {
            //Diable Motor Commands
            ((((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            //Set Graphic Animation Duration
            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((napJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", "700");

        }
        catch (Exception e){}

            final AnimationEngineParameterType napAnimation = new AnimationEngineParameterType();
        napAnimation.Json = napJsonMsgObject;
        napAnimation.delegate = delegate;
        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
            add(napAnimation);
        }};

        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetPlaneAnimationSetFromBeat(ArrayList<BeatsType> beats)
    {

        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>();

        for(BeatsType beat : beats)
        {
            String json = beat.Action_Data;

            JSONObject jsonDictionary = GenericExtentions.parseJSONString(json);


            AnimationEngineParameterType responseAnimationParameter = new AnimationEngineParameterType();

            responseAnimationParameter.Json = jsonDictionary;

            try {

                String TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }


                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }


                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }



                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

                TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).getString("AnimationKind");

                if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
                    (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
                }

            }
            catch (Exception e){}

            responseAnimationParameter.StartSec = beat.StartSec;
            responseAnimationParameter.EndSec = beat.EndSec;

            animationEngineParameterGroup.Expressions.add(responseAnimationParameter);
        }

        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetPlaneAnimation(String json)
    {
       return GetPlaneAnimation(json, "");
    }

    public AnimationEngineParameterGroup GetPlaneAnimation(String json,String response)
    {
        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        JSONObject jsonDictionary = GenericExtentions.parseJSONString(json);

        AnimationEngineParameterType responseAnimationParameter = new AnimationEngineParameterType();

        responseAnimationParameter.Json = jsonDictionary;

       try
       {
           String TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }


           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }


           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }



           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }

           TransformKind = (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).getString("AnimationKind");

           if (TransformKind == AnimationTypes.Type.Tranformation.name()) {
               (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).put("AnimationKind", AnimationTypes.Type.TransformOverlay.name());
           }
       }
       catch (Exception e){}

        responseAnimationParameter.sentance = response;

        animationEngineParameterGroup.Expressions.add(responseAnimationParameter);
        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetNapAnimation(AnimationParameterTypeDelegates delegate) {
        DB_Local_Store dbHAndler = new DB_Local_Store();

        Expressions_Type napExpression = dbHAndler.readExpression("nap");

        JSONObject napJsonMsgObject = GenericExtentions.parseJSONString(napExpression.Action_Data);

        final AnimationEngineParameterType napAnimation = new AnimationEngineParameterType();

        napAnimation.Json = napJsonMsgObject;

        try {
            //Laod Calibrated Motor positions for shutdown state
            ((((((napAnimation.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet"))
                    .getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("key", String.valueOf(MachineConfig.Instance.MachineActuatorList.get(Actuator.LEAN).ShutdownDeltaAngle));


            ((((((napAnimation.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet"))
                    .getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("key", String.valueOf(MachineConfig.Instance.MachineActuatorList.get(Actuator.LIFT).ShutdownDeltaAngle));


            ((((((napAnimation.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet"))
                    .getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("key", String.valueOf(MachineConfig.Instance.MachineActuatorList.get(Actuator.TILT).ShutdownDeltaAngle));


            ((((((napAnimation.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet"))
                    .getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("key", String.valueOf(MachineConfig.Instance.MachineActuatorList.get(Actuator.TURN).ShutdownDeltaAngle));
        }
        catch (Exception e) {}



        napAnimation.delegate = delegate;
        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
            add(napAnimation);
        }};

        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetBlinkCorrectionStraight() {
        DB_Local_Store dbHAndler = new DB_Local_Store();

        Expressions_Type straightxpression = dbHAndler.readExpression("Stand_Straight");

        JSONObject straightJsonMsgObject = GenericExtentions.parseJSONString(straightxpression.Action_Data);

        try {
            //Diable Motor Commands
            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");


            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");


            //Set Graphic Animation Duration
            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidLeft")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidRight")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).
                    getJSONObject("Transition")).put("Duration", "50");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).
                    getJSONObject("Transition")).put("Duration", "50");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).
                    getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).
                    getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).
                    getJSONObject("Transition")).put("Duration", "100");


            final AnimationEngineParameterType straightAnimation = new AnimationEngineParameterType();
            straightAnimation.Json = straightJsonMsgObject;
            AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

            animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
                add(straightAnimation);
            }};

            return animationEngineParameterGroup;
        } catch (Exception e) {
        }

        return null;
    }

    public AnimationEngineParameterGroup GetStraightAnimation() {
        return GetStraightAnimation("");
    }

    public AnimationEngineParameterGroup GetStraightAnimation(String responseText) {
        DB_Local_Store dbHAndler = new DB_Local_Store();

        Expressions_Type straightxpression = dbHAndler.readExpression("Stand_Straight");

        JSONObject straightJsonMsgObject = GenericExtentions.parseJSONString(straightxpression.Action_Data);

        try {
            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lean")).getJSONObject("Transition")).put("Timing", "1600");


            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Turn")).getJSONObject("Transition")).put("Timing", "1500");


            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Tilt")).getJSONObject("Transition")).put("Timing", "1500");


            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lift")).getJSONObject("Transition")).put("Timing", "1500");


            //Set Graphic Animation Duration
            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", "700");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", "700");

            final AnimationEngineParameterType straightAnimation = new AnimationEngineParameterType();
            straightAnimation.Json = straightJsonMsgObject;
            straightAnimation.sentance = responseText;
            AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

            animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
                add(straightAnimation);
            }};

            return animationEngineParameterGroup;
        } catch (Exception e) {

        }
        return null;
    }

    public AnimationEngineParameterGroup GetStraightBlink() {
        DB_Local_Store dbHAndler = new DB_Local_Store();


        Expressions_Type blinkCloseExpression = dbHAndler.readExpression("blink_close");

        JSONObject blinkClosejsonMsgObject = GenericExtentions.parseJSONString(blinkCloseExpression.Action_Data);

        final AnimationEngineParameterType blinkCloseAnimation = new AnimationEngineParameterType();
        blinkCloseAnimation.Json = blinkClosejsonMsgObject;
        blinkCloseAnimation.IsHalfBlink = true;


        Expressions_Type straightxpression = dbHAndler.readExpression("Stand_Straight");

        JSONObject straightJsonMsgObject = GenericExtentions.parseJSONString(straightxpression.Action_Data);
        try {
            //Diable Motor Commands
            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");

            ((((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).
                    getJSONObject("AnimationStateSet")).getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).
                    getJSONObject("Angle")).put("value", "false");


            //Set Graphic Animation Duration
            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", "200");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", "200");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", "200");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", "200");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", "200");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", "100");

            (((((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", "200");


            final AnimationEngineParameterType straightAnimation = new AnimationEngineParameterType();
            straightAnimation.Json = straightJsonMsgObject;

            AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

            animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
                add(blinkCloseAnimation);
                add(straightAnimation);
            }};

            return animationEngineParameterGroup;
        } catch (Exception e) {
        }
        return null;
    }
    public AnimationEngineParameterGroup GetAnimationEngineParameterTypes(String json)
    {
        return  GetAnimationEngineParameterTypes(json, "", AnimationOnPauseRestartAction.DESTROY);
    }
    public AnimationEngineParameterGroup GetAnimationEngineParameterTypes(String json, String response)
    {
        return  GetAnimationEngineParameterTypes(json, response, AnimationOnPauseRestartAction.DESTROY);
    }

    public AnimationEngineParameterGroup GetAnimationEngineParameterTypes(String json, String response, AnimationOnPauseRestartAction OnPauseAction)

    {
        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        JSONObject jsonDictionary = GenericExtentions.parseJSONString(json);

        final AnimationEngineParameterType responseAnimationParameter = new AnimationEngineParameterType();

        responseAnimationParameter.Json = jsonDictionary;

        responseAnimationParameter.sentance = response;

        animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>()
        {{
            add(responseAnimationParameter);
        }};

        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetAnimationEngineParameterTypesWithBlinkAndStraight(String json, String response) {
        return GetAnimationEngineParameterTypesWithBlinkAndStraight(json, response, AnimationOnPauseRestartAction.DESTROY);
    }


    public AnimationEngineParameterGroup GetAnimationEngineParameterTypesWithBlinkAndStraight(String json) {
        return GetAnimationEngineParameterTypesWithBlinkAndStraight(json, "", AnimationOnPauseRestartAction.DESTROY);

    }


    public AnimationEngineParameterGroup GetAnimationEngineParameterTypesWithBlinkAndStraight(String json, String response, AnimationOnPauseRestartAction AnimationOnPauseRestartAction) {


        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

        try {


            DB_Local_Store dbHAndler = new DB_Local_Store();


            JSONObject jsonDictionary = GenericExtentions.parseJSONString(json);

            final AnimationEngineParameterType responseAnimationParameter = new AnimationEngineParameterType();

            responseAnimationParameter.Json = jsonDictionary;

            responseAnimationParameter.sentance = response;


            Expressions_Type blinkCloseExpression = dbHAndler.readExpression("blink_close");

            JSONObject blinkClosejsonMsgObject = GenericExtentions.parseJSONString(blinkCloseExpression.Action_Data);

            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            (((((blinkClosejsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).put("AnimationKind", "TransformOverlay");


            final AnimationEngineParameterType blinkCloseAnimation = new AnimationEngineParameterType();
            blinkCloseAnimation.Json = blinkClosejsonMsgObject;
            blinkCloseAnimation.IsHalfBlink = true;


            final AnimationEngineParameterType responseAnimationParameterNoMotion = new AnimationEngineParameterType();
            responseAnimationParameterNoMotion.Json = GenericExtentions.parseJSONString(json);

            //Diable Motor Commands
            ((((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");


            ((((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");


            ((((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");


            ((((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("State")).getJSONObject("AnimationStateSet")).getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");


            //Set Graphic Animation Duration
            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", "200");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", "200");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", "200");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", "200");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", "100");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", "100");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", "200");

            (((((responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", "200");


            Expressions_Type straightxpression = dbHAndler.readExpression("Stand_Straight");

            JSONObject straightJsonMsgObject = GenericExtentions.parseJSONString(straightxpression.Action_Data);


            (((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).put("Motor_Lean", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lean"));
            (((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).put("Motor_Turn", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Turn"));
            (((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).put("Motor_Tilt", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Tilt"));
            (((straightJsonMsgObject.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).put("Motor_Lift", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lift"));


            final AnimationEngineParameterType straightAnimation = new AnimationEngineParameterType();
            straightAnimation.Json = straightJsonMsgObject;






            animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>() {{
                add(responseAnimationParameter);
                add(blinkCloseAnimation);
                add(responseAnimationParameterNoMotion);
                add(straightAnimation);
            }};


        } catch (Exception e) {

        }
        return animationEngineParameterGroup;
    }

    public AnimationEngineParameterGroup GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(String json)
    {
        return  GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(json, "");
    }
    public AnimationEngineParameterGroup GetAnimationEngineParameterTypesTimeStretchedWithBlinkAndStraight(String json, String response)
    {
        AnimationEngineParameterGroup animationEngineParameterGroup = new AnimationEngineParameterGroup();

try {
    DB_Local_Store dbHAndler = new DB_Local_Store();

    Random rand = new Random();
    int iStrtchedTime = rand.nextInt(3000 - 2500) + 2500;
    String StrtchedTime = String.valueOf(iStrtchedTime);

    JSONObject jsonDictionary = GenericExtentions.parseJSONString(json);
    final AnimationEngineParameterType responseAnimationParameter = new AnimationEngineParameterType();
    JSONObject jAnimationPositions = jsonDictionary.getJSONObject("AnimationPositions");
    JSONObject jTransitions = jAnimationPositions.getJSONObject("Transition");
    JSONObject jAnimationTransitionSet = jTransitions.getJSONObject("AnimationTransitionSet");

    ((jAnimationTransitionSet.getJSONObject("Motor_Lean")).getJSONObject("Transition")).put("Timing",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Motor_Turn")).getJSONObject("Transition")).put("Timing",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Motor_Tilt")).getJSONObject("Transition")).put("Timing",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Motor_Lift")).getJSONObject("Transition")).put("Timing",StrtchedTime);

    //Set Graphic Animation Duration
    ((jAnimationTransitionSet.getJSONObject("Image_EyeLidLeft")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeLidRight")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    ((jAnimationTransitionSet.getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration",StrtchedTime);

    responseAnimationParameter.Json = jsonDictionary;

    responseAnimationParameter.sentance = response;


    Expressions_Type blinkCloseExpression = dbHAndler.readExpression("blink_close");
    JSONObject blinkClosejsonMsgObject = GenericExtentions.parseJSONString(blinkCloseExpression.Action_Data);
    JSONObject jblinkCloseAnimationPositions = blinkClosejsonMsgObject.getJSONObject("AnimationPositions");
    JSONObject jblinkCloseStates = jblinkCloseAnimationPositions.getJSONObject("State");
    JSONObject jblinkCloseStateSetSet = jblinkCloseStates.getJSONObject("AnimationStateSet");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeLeft")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeRight")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeBrowRight")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeBrowLeft")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeBallLeft")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyeBallRight")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyePupilLeft")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");

    ((jblinkCloseStateSetSet.getJSONObject("Image_EyePupilRight")).getJSONObject("AnimationState")).put("Duration","TransformOverlay");


    final AnimationEngineParameterType blinkCloseAnimation = new AnimationEngineParameterType();
    blinkCloseAnimation.Json = blinkClosejsonMsgObject;

    blinkCloseAnimation.IsHalfBlink = true;


    final AnimationEngineParameterType responseAnimationParameterNoMotion = new AnimationEngineParameterType();
    responseAnimationParameterNoMotion.Json = GenericExtentions.parseJSONString(json);
    JSONObject jresponseAnimationPositions = responseAnimationParameterNoMotion.Json.getJSONObject("AnimationPositions");
    JSONObject jresponseTransitions = jresponseAnimationPositions.getJSONObject("Transition");
    JSONObject jresponseStates = jresponseAnimationPositions.getJSONObject("State");
    JSONObject jresponseTransitionSet = jresponseTransitions.getJSONObject("AnimationTransitionSet");
    JSONObject jresponseStateSetSet = jresponseStates.getJSONObject("AnimationStateSet");

    //Diable Motor Commands
    (((jresponseStateSetSet.getJSONObject("Motor_Lean")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");

    (((jresponseStateSetSet.getJSONObject("Motor_Lift")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");

    (((jresponseStateSetSet.getJSONObject("Motor_Tilt")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");

    (((jresponseStateSetSet.getJSONObject("Motor_Turn")).getJSONObject("AnimationState")).getJSONObject("Angle")).put("value", "false");


    //Set Graphic Animation Duration
    ((jresponseTransitionSet.getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", "200");

    ((jresponseTransitionSet.getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", "200");

    ((jresponseTransitionSet.getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", "200");

    ((jresponseTransitionSet.getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", "200");

    ((jresponseTransitionSet.getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", "100");

    ((jresponseTransitionSet.getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", "100");

    ((jresponseTransitionSet.getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", "200");

    ((jresponseTransitionSet.getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", "200");


    Expressions_Type straightxpression = dbHAndler.readExpression("Stand_Straight");
    JSONObject straightJsonMsgObject = GenericExtentions.parseJSONString(straightxpression.Action_Data);
    JSONObject jstraightxpressionPositions = straightJsonMsgObject.getJSONObject("AnimationPositions");
    JSONObject jstraightxpressionTransitions = jstraightxpressionPositions.getJSONObject("Transition");
    JSONObject jstraightxpressionTransitionSet = jstraightxpressionTransitions.getJSONObject("AnimationTransitionSet");


    jstraightxpressionTransitionSet.put("Motor_Lean", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lean"));


    jstraightxpressionTransitionSet.put("Motor_Turn", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Turn"));


    jstraightxpressionTransitionSet.put("Motor_Tilt", (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Tilt"));


    jstraightxpressionTransitionSet.put("Motor_Lift",  (((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Motor_Lift"));


    //Set Graphic Animation Duration
    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeLidLeft")).getJSONObject("Transition")).put("Duration",  (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidLeft")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeLidRight")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLidRight")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeLeft")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeRight")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeRight")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowRight")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBrowLeft")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallLeft")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyeBallRight")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilRight")).getJSONObject("Transition")).getString("Duration"));

    ((jstraightxpressionTransitionSet.getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).put("Duration", (((((jsonDictionary.getJSONObject("AnimationPositions")).getJSONObject("Transition")).getJSONObject("AnimationTransitionSet")).getJSONObject("Image_EyePupilLeft")).getJSONObject("Transition")).getString("Duration"));

    final AnimationEngineParameterType straightAnimation = new AnimationEngineParameterType();
    straightAnimation.Json = straightJsonMsgObject;


    animationEngineParameterGroup.Expressions = new ArrayList<AnimationEngineParameterType>(){{
        add(responseAnimationParameter);
        add(blinkCloseAnimation);
        add(responseAnimationParameterNoMotion);
        add(straightAnimation);
    }};
}
catch (Exception e)
{

}
        return animationEngineParameterGroup;
    }
    
    
    public Boolean MakeDeltaAnglesForStudioExpression(int EmSynthID, JSONObject data, String SoundID)
    {
        AnimationAction animationAction = new AnimationAction();
        
        animationAction.ParseJson( data);

        MotorAnimationState Turn_MotorState = ((MotorAnimationState)animationAction.Position.State.StateSet.get(AnimationObject.Motor_Turn));
        if(Turn_MotorState != null && Turn_MotorState.Angle.second);
        {
            Turn_MotorState.IsDeltaAngle = true;
            Turn_MotorState.Angle = new Pair<>(KineticComms.Instance.GetDeltaAngleFromFullAngle( Turn_MotorState.Angle.first,  Actuator.TURN) , true);
        }

        MotorAnimationState Lift_MotorState = ((MotorAnimationState)animationAction.Position.State.StateSet.get(AnimationObject.Motor_Lift));
        if(Lift_MotorState != null && Lift_MotorState.Angle.second);
        {
            Lift_MotorState.IsDeltaAngle = true;
            Lift_MotorState.Angle = new Pair<>(KineticComms.Instance.GetDeltaAngleFromFullAngle( Lift_MotorState.Angle.first,  Actuator.LIFT) , true);
        }

        MotorAnimationState Lean_MotorState = ((MotorAnimationState)animationAction.Position.State.StateSet.get(AnimationObject.Motor_Lean));
        if(Lean_MotorState != null && Lean_MotorState.Angle.second);
        {
            Lean_MotorState.IsDeltaAngle = true;
            Lean_MotorState.Angle = new Pair<>(KineticComms.Instance.GetDeltaAngleFromFullAngle( Lean_MotorState.Angle.first,  Actuator.LEAN) , true);
        }

        MotorAnimationState Tilt_MotorState = ((MotorAnimationState)animationAction.Position.State.StateSet.get(AnimationObject.Motor_Tilt));
        if(Tilt_MotorState != null && Tilt_MotorState.Angle.second);
        {
            Tilt_MotorState.IsDeltaAngle = true;
            Tilt_MotorState.Angle = new Pair<>(KineticComms.Instance.GetDeltaAngleFromFullAngle( Tilt_MotorState.Angle.first,  Actuator.TILT) , true);
        }



        Expressions_Type expressions_Type =  new Expressions_Type( animationAction.Name, animationAction.Position.Json(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.JOY).floatValue(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.SURPRISE).floatValue(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.FEAR).floatValue(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.SADNESS).floatValue(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.ANGER).floatValue(),
                animationAction.Emotion.EmotionData.get(EmotionEnums.Emotions.DISGUST).floatValue(),
                EmSynthID,
                SoundID);

        DB_Local_Store dbHandler = new DB_Local_Store();
        
        
        return   dbHandler.saveExpression(expressions_Type);
    }
}
