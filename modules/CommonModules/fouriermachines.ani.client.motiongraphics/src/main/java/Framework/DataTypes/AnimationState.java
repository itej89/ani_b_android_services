//
//  AnimationProperties.swift
//  BoltBot
//
//  Created by Uday on 14/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;



import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Framework.DataTypes.Constants.AnimationTypes;
import Framework.DataTypes.Constants.CircularPathDirection;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;

import static Framework.DataTypes.Constants.CircularPathDirection.CircularPathDirectionStringToOptions;


public  class AnimationState
{
  public  String Json(){
      return  "";
  }
    
   public void parseJson(JSONObject json){}
}
