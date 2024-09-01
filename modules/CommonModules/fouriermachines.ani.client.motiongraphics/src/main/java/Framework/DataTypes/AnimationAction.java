//
//  AnimationAction.swift
//  BoltBot
//
//  Created by Uday on 17/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;


import org.json.JSONObject;

import java.util.ArrayList;

public class AnimationAction
{
   public String Name = "";
    
   public AnimationPositions Position = new AnimationPositions();
;
   public AnimationEmotions Emotion  = new AnimationEmotions();
    
  public   ArrayList<AnimationAction> Action = new ArrayList<AnimationAction>();
    
    public void ParseJson(JSONObject json)  {

try {
    JSONObject Action = json.getJSONObject("AnimationAction");

    Name = Action.getString("Name");

    JSONObject position = Action.getJSONObject("Position");

    Position.parseJson(position);

    JSONObject emotion = Action.getJSONObject("Emotion");

    Emotion.parseJson(emotion);
}
catch (Exception e)
{}
    }
    
    public String Json() {
        
        String json = "";


        json = json.concat("{ \"AnimationAction\" : {");

        json = json.concat(" \"Name\" : "+Name+"  ");

        json = json.concat(" \"Position\" : "+Position.Json()+" , ");

        json = json.concat(" \"Emotion\" : "+Emotion.Json()+"  ");

        json = json.concat("}}");
        
        return json;
        
    }
    
}

