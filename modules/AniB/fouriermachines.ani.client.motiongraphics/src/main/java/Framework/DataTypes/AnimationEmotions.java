//
//  Animation_Emotions.swift
//  Ani_AnimationStudio
//
//  Created by Tej Kiran on 11/12/17.
//  Copyright © 2017 Ani. All rights reserved.
//

package Framework.DataTypes;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Constants.EmotionEnums;

public class AnimationEmotions {
    public Map<EmotionEnums.Emotions , Double> EmotionData = new HashMap<EmotionEnums.Emotions , Double>(){{
        put(EmotionEnums.Emotions.JOY, 0.0);
        put(EmotionEnums.Emotions.SURPRISE, 0.0);
        put(EmotionEnums.Emotions.FEAR, 0.0);
        put(EmotionEnums.Emotions.SADNESS, 0.0);
        put(EmotionEnums.Emotions.ANGER, 0.0);
        put(EmotionEnums.Emotions.DISGUST, 0.0);
    }};


    void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("AnimationEmotions");


            EmotionData.put(EmotionEnums.Emotions.JOY, dictionary.getDouble("JOY"));
            EmotionData.put(EmotionEnums.Emotions.SURPRISE, dictionary.getDouble("SURPRISE"));
            EmotionData.put(EmotionEnums.Emotions.FEAR, dictionary.getDouble("FEAR"));
            EmotionData.put(EmotionEnums.Emotions.SADNESS, dictionary.getDouble("SADNESS"));
            EmotionData.put(EmotionEnums.Emotions.ANGER, dictionary.getDouble("ANGER"));
            EmotionData.put(EmotionEnums.Emotions.DISGUST, dictionary.getDouble("DISGUST"));
        }
        catch (Exception e)
        {}
    }
    
    public String Json(){
        
        String json = "";
        
        
        json.concat("{ \"AnimationEmotions\" : {");



        json = json.concat(" \"JOY\" : \""+EmotionData.get(EmotionEnums.Emotions.JOY)+"\" , ");
        json = json.concat(" \"SURPRISE\" : \""+ EmotionData.get(EmotionEnums.Emotions.SURPRISE)+"\" ,  ");
        json = json.concat(" \"FEAR\" : \""+EmotionData.get(EmotionEnums.Emotions.FEAR)+"\" ,  ");
        json = json.concat(" \"SADNESS\" : \""+EmotionData.get(EmotionEnums.Emotions.SADNESS)+"\" , ");
        json = json.concat(" \"ANGER\" : \""+EmotionData.get(EmotionEnums.Emotions.ANGER)+"\" , ");
        json = json.concat(" \"DISGUST\" : \""+EmotionData.get(EmotionEnums.Emotions.DISGUST)+"\"  ");
        
        json.concat("}}");
        
        return json;
        
    }
}

