//
//  AnimationPropertySet.swift
//  BoltBot
//
//  Created by Uday on 14/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnimationStateSet
{

    public Map<AnimationObject, AnimationState> StateSet = new HashMap<AnimationObject, AnimationState>()
    {{
        put(AnimationObject.Image_EyeBrowRight, new ImageAnimationState());
        put(AnimationObject.Image_EyeBrowLeft, new ImageAnimationState());
        put(AnimationObject.Image_EyeRight, new ImageAnimationState());
        put(AnimationObject.Image_EyeLeft, new ImageAnimationState());
        put(AnimationObject.Image_EyeBallRight, new ImageAnimationState());
        put(AnimationObject.Image_EyeBallLeft, new ImageAnimationState());
        put(AnimationObject.Image_EyePupilRight, new ImageAnimationState());
        put(AnimationObject.Image_EyePupilLeft, new ImageAnimationState());
        put(AnimationObject.Image_EyeLidLeft, new ImageAnimationState());
        put(AnimationObject.Image_EyeLidRight, new ImageAnimationState());


        put(AnimationObject.Motor_Tilt, new MotorAnimationState());
        put(AnimationObject.Motor_Turn, new MotorAnimationState());
        put(AnimationObject.Motor_Lift, new MotorAnimationState());
        put(AnimationObject.Motor_Lean, new MotorAnimationState());
    }};

    
    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("AnimationStateSet");


            for(Map.Entry item : StateSet.entrySet()) {

                JSONObject dictionaryProperty = dictionary.getJSONObject(item.getKey().toString());
                StateSet.get(item.getKey()).parseJson(dictionaryProperty);

            }
        }
        catch (Exception e)
        {}
    }
    
    
    public String Json(){
        
        String json = "";


        json = json.concat("{ \"AnimationStateSet\" : {");

        for(Map.Entry<AnimationObject, AnimationState> item : StateSet.entrySet()) {

            json = json.concat(" \""+item.getKey().toString()+"\" : "+item.getValue().Json()+" ,");
            
        }
        
        json =  json.replaceAll("^,", "");

        json = json.concat("}");
        json = json.concat("}");
        
        return json;
        
    }

    public  void destry()
    {
        StateSet.clear();
       // StateSet = null;

    }
}



