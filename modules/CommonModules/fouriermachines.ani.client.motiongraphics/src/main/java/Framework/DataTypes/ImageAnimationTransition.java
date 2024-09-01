package Framework.DataTypes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Framework.DataTypes.UIViewAnimationOptions;
import Framework.DataTypes.Constants.AnimationEasingTypes;
import Framework.DataTypes.Constants.AnimationFillModes;
import Framework.DataTypes.Constants.UIElementAnimationOptions;

public class ImageAnimationTransition extends AnimationTransition
{
    public enum TransitionType { NA, Transition, Identity}
    public TransitionType ImageTransitionType = TransitionType.Transition;

   public Integer Duration = 1000;
   public Integer Delay = 0;
   public ArrayList<UIViewAnimationOptions.Options> AnimationCurveType = new ArrayList<UIViewAnimationOptions.Options>(){{ add(UIViewAnimationOptions.Options.allowUserInteraction); }};

    public Double Amplitude = 0.0;
    public Double Damping = 0.0;

    AnimationEasingTypes.Types KeyframeAnimation_EasingFunction = AnimationEasingTypes.Types.linear;
    AnimationFillModes.Modes KeyframeAnimation_FillMode = AnimationFillModes.Modes.Removed;



    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("Transition");

            Duration = dictionary.getInt("Duration");
                    Delay = dictionary.getInt("Delay");
                    Amplitude = dictionary.getDouble("Amplitude");
                    Damping = dictionary.getDouble("Damping");
                    KeyframeAnimation_EasingFunction = AnimationEasingTypes.AnimationEasingTypesStringToOptions.get(dictionary.getString("KeyframeAnimation_EasingFunction"));

                    KeyframeAnimation_FillMode = AnimationFillModes.AnimationFillModesStringToOptions.get(dictionary.getString("KeyframeAnimation_FillMode"));
                    JSONArray animCurveArray =  dictionary.getJSONArray("AnimationCurveType");


            for(int i=0; i<animCurveArray.length(); i++)
            {

                AnimationCurveType.add(UIElementAnimationOptions.UITransitionCurveStringToOptions.get(animCurveArray.getString(i)));
            }

        }
        catch (Exception e)
        {

        }
    }


    public  String Json()
    {

        String json = "";

        json = json.concat("{ \"Transition\" : {");

        json = json.concat(" \"Duration\" : \""+Duration.toString()+"\" ,");

        json = json.concat(" \"Delay\" : \""+Delay.toString()+"\" ,");

        json = json.concat(" \"Damping\" : \""+Damping.toString()+"\" ,");

        json = json.concat(" \"Amplitude\" : \""+Amplitude.toString()+"\" ,");

        json = json.concat(" \"KeyframeAnimation_EasingFunction\" : \""+KeyframeAnimation_EasingFunction.toString()+"\" ,");

        json = json.concat(" \"KeyframeAnimation_FillMode\" : \""+KeyframeAnimation_FillMode.toString()+"\" ,");
        json = json.concat(" \"AnimationCurveType\" : {");

        Integer i = 0;

        while(i < UIElementAnimationOptions.Options.count)
        {

            if(AnimationCurveType.contains(UIElementAnimationOptions.UITransitionCurveOptions.get(i)))
            {
                json = json.concat(" \"key\" : \""+ UIElementAnimationOptions.Options.fromInt(i).toString()+"\" ,");
            }

            i = i+1;

        }

        json =  json.replaceAll("^,", "");

        json = json.concat(" } ");

        json = json.concat("}}");

        return json;

    }


    public ImageAnimationTransition()
    {}

   public ImageAnimationTransition(TransitionType _ImageTransitionType,
       Integer  _Duration, Integer  _Delay, ArrayList<UIViewAnimationOptions.Options>  _AnimationCurveType, Double  _Damping, Double  _Amplitude)
    {
        ImageTransitionType = _ImageTransitionType;
        Duration = _Duration;
        Delay = _Delay;
        AnimationCurveType = _AnimationCurveType;
        Damping = _Damping;
        Amplitude = _Amplitude;
    }

}
