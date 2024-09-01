//
//  AnimationTransition.swift
//  BoltBot
//
//  Created by Uday on 15/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Framework.DataTypes.UIViewAnimationOptions;

import Framework.DataTypes.UIViewAnimationOptions;
import Framework.DataTypes.Constants.AnimationEasingTypes;
import Framework.DataTypes.Constants.AnimationFillModes;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.UIElementAnimationOptions;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDamp;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestFrequency;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestVelocity;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public  class AnimationTransition
{
    public  String Json() {
        return  "";
    }
    
    public void parseJson(JSONObject json)
    {

    }
}

//All timings are in milliseconds

