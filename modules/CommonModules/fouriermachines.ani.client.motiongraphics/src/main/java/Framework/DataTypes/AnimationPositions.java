//
//  AnimationsDefault.swift
//  BoltBot
//
//  Created by Uday on 14/05/17.
//  Copyright © 2017 itej89. All rights reserved.
//

package Framework.DataTypes;

import org.json.JSONObject;



public class AnimationPositions
{
    public AnimationStateSet State = new AnimationStateSet();
    public AnimationTransitionSet Transition = new AnimationTransitionSet();
    public String sentance = "";
    public String audio = "";
    public int StartSec = 0;
    public int EndSec = 0;
    public Double volume = 0.2;
    
    
    
   public void parseJson(JSONObject json)
    {
        try {


            JSONObject dictionary = json.getJSONObject("AnimationPositions");

            JSONObject dictionaryProperty = dictionary.getJSONObject("State");

            State.parseJson(dictionaryProperty);

            dictionaryProperty = dictionary.getJSONObject("Transition");

            Transition.parseJson(dictionaryProperty);

            audio = dictionary.getString("audio");
            volume = dictionary.getDouble("volume");

        }
        catch (Exception e)
        {}
        
    }
    
  public  String Json(){
        
        String json = "";


        json = json.concat("{ \"AnimationPositions\" : {");


        json = json.concat(" \"State\" : "+State.Json()+" , ");
        json = json.concat(" \"Transition\" : "+Transition.Json()+" , ");

        json = json.concat(" \"audio\" : \""+audio+"\" , ");
        json = json.concat(" \"volume\" : \""+volume+"\"  ");




        json = json.concat("}}");
        
        return json;
        
    }
    
    public  void destroy()
    {
        State.destry();
       // State=  null;
        Transition.destry();
       // Transition = null;

    }
    
}

