//
//  AnimationTransitionSet.swift
//  BoltBot
//
//  Created by Uday on 15/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnimationTransitionSet
{



    public Map<AnimationObject, AnimationTransition> TransitionSet = new HashMap<AnimationObject, AnimationTransition>()
    {{
        put(AnimationObject.Image_EyeBrowRight, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeBrowLeft, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeRight, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeLeft, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeBallRight, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeBallLeft, new ImageAnimationTransition());
        put(AnimationObject.Image_EyePupilRight, new ImageAnimationTransition());
        put(AnimationObject.Image_EyePupilLeft, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeLidLeft, new ImageAnimationTransition());
        put(AnimationObject.Image_EyeLidRight, new ImageAnimationTransition());


        put(AnimationObject.Motor_Tilt, new MotionAnimationTransition());
        put(AnimationObject.Motor_Turn, new MotionAnimationTransition());
        put(AnimationObject.Motor_Lift, new MotionAnimationTransition());
        put(AnimationObject.Motor_Lean, new MotionAnimationTransition());
    }};
    
    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("AnimationTransitionSet");


            for(Map.Entry<AnimationObject, AnimationTransition> item : TransitionSet.entrySet()) {

                JSONObject dictionaryProperty = dictionary.getJSONObject(item.getKey().toString());
                TransitionSet.get(item.getKey()).parseJson(dictionaryProperty);

            }
        }
        catch (Exception e)
        {}
        
    }
    
    public String Json(){
        
        String json = "";
        
        
        json = json.concat("{ \"AnimationTransitionSet\" : {");

        for(Map.Entry<AnimationObject, AnimationTransition> item : TransitionSet.entrySet()) {

            json = json.concat(" \""+item.getKey().toString()+"\" : "+item.getValue().Json()+" ,");
            
        }
        
        json =  json.replaceAll("^,", "");

        json = json.concat("}");
        json = json.concat("}");
        
        return json;
        
    }

    public  void destry()
    {
        TransitionSet.clear();
       // TransitionSet = null;

    }
    
}

