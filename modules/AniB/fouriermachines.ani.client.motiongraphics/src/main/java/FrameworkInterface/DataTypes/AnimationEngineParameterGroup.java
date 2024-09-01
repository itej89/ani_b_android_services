//
//  AnimationEngineParameterGroup.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 16/06/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface.DataTypes;

import java.util.ArrayList;
import java.util.UUID;

import FrameworkInterface.DataTypes.Constants.AnimationOnPauseRestartAction;

public class AnimationEngineParameterGroup
{
    public AnimationEngineParameterGroup()
    {}
    
    //Used to connect multipleAnimations that are part of a one complete expression
    public UUID AnimationGroupID = UUID.randomUUID();
    
    public AnimationOnPauseRestartAction OnPauseAction = AnimationOnPauseRestartAction.DESTROY;
    
    public ArrayList<AnimationEngineParameterType> Expressions = new ArrayList<AnimationEngineParameterType>();

    public void destroy()
    {
        if(Expressions != null) {
            for(int i=0; i< Expressions.size(); i++)
            {
                Expressions.get(i).destroy();
            }
            Expressions.clear();
          //  Expressions = null;

        }

    }
}
