//
//  AnimationEngineParameterType.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 29/05/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface.DataTypes;

import org.json.JSONObject;

import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.AnimationPositions;
import FrameworkInterface.DataTypes.Constants.MotionStartType;
import FrameworkInterface.DataTypes.Delegates.AnimationParameterTypeDelegates;

public class AnimationEngineParameterType
{
    public AnimationEngineParameterType()
    {}

    public AnimationParameterTypeDelegates delegate;
    
    public AnimationPositions CustomPositionParameters;
    
    public AnimationPositions animationPosition;
    
    public Boolean IsHalfBlink = false;
    
    public JSONObject Json;
    public String sentance = "";

    //Added for Choreogram
    public String audio = "";
    public Integer StartSec = 0;
    public Integer EndSec = 0;


    public MotionStartType TriggerType = MotionStartType.WAIT_AND_MOVE;
    public void setParameter(AnimationObject object)
    {
    
    }

    public  void  destroy()
    {
        if(animationPosition != null)
        {
            animationPosition.destroy();
           // animationPosition = null;

        }
    }
}
