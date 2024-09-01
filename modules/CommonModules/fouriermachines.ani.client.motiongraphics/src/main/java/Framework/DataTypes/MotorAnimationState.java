package Framework.DataTypes;

import android.content.Context;
import android.util.Pair;

import org.json.JSONObject;

import java.util.ArrayList;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestAngle;
import FrameworkInterface.InterfaceImplementation.KineticComms;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class MotorAnimationState extends AnimationState
{
    public AnimationObject TargetObject = AnimationObject.NA;
    public Boolean IsDeltaAngle = false;
    public Pair<Integer, Boolean> Angle = new Pair<Integer, Boolean>(0, false);

    public Boolean setKineticsState(KineticsRequest request) {
        switch(request.RequestType)
        {
        case ANG:
            Angle = new Pair<>(((KineticsRequestAngle)request).Angle,true);
            return true;
        default:
            break;
        }

        return false;
    }

   public ArrayList<KineticsRequest> GetStateCommandString(Actuator actuator) {
        ArrayList<KineticsRequest> Command = new ArrayList<KineticsRequest>();
        if(Angle.second == true){
            Integer angle = (Angle.first);
            if(IsDeltaAngle == true)
            {
                angle = KineticComms.Instance.GetFullAngleFromDeltaAngle(angle,  actuator);
            }
            Command.add(new KineticsRequestAngle((angle), actuator));
        }
        return Command;
    }
    @Override
    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("AnimationState");

            IsDeltaAngle = dictionary.getBoolean("IsDeltaAngle");

            dictionary = dictionary.getJSONObject("Angle");

            Angle =  new Pair<>(dictionary.getInt("key"), dictionary.getBoolean("value"));
        }
        catch (Exception e)
        {}
    }

    @Override
    public String Json(){


        String json = "";

        json = json.concat("{ \"AnimationState\" : {");
        json = json.concat(" \"IsDeltaAngle\" : \""+IsDeltaAngle.toString()+"\" , ");
        json = json.concat(" \"Angle\" : {");
        json = json.concat((" \"key\" : \""+Angle.first.toString()+"\" , "+" \"value\" : \""+Angle.second.toString()+"\" "));
        json = json.concat("}}");

        json = json.concat("}");

        return json;

    }

    public MotorAnimationState(){}

   public MotorAnimationState(AnimationObject _TargetObject,
        Pair<Integer, Boolean> _Angle)
    {
        Angle = _Angle;
    }

}
