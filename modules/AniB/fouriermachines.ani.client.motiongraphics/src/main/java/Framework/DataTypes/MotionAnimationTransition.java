package Framework.DataTypes;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDamp;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestDelay;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestEasingInOut;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestFrequency;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestServoEasing;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestTiming;
import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequestVelocity;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class MotionAnimationTransition extends AnimationTransition
{
    Integer Timing = 0;
    Integer Delay = 0;
    Integer Frequency = 0;
    Integer Damp = 0;
    Integer Velocity = 0;
    CommandLabels.EasingFunction EasingFunction = CommandLabels.EasingFunction.LIN;
    CommandLabels.EasingType EasingType = CommandLabels.EasingType.IN;

    public Boolean setKineticsTransition(KineticsRequest request)
    {
        switch(request.RequestType) {
        case TMG:
            Timing = ((KineticsRequestTiming)request).Timing;
            return true;
        case DEL:
            Delay = ((KineticsRequestDelay)request).Delay;
            return true;
        case FRQ:
            Frequency = ((KineticsRequestFrequency)request).Frequency;
            return true;
        case DMP:
            Damp = ((KineticsRequestDamp)request).Damping;
            return true;
        case VEL:
            Velocity = ((KineticsRequestVelocity)request).Velocity;
            return true;
        case EAS:
            EasingFunction = ((KineticsRequestServoEasing)request).EasingFunction;
            return true;
        case INO:
            EasingType = ((KineticsRequestEasingInOut)request).EasingType;
            return true;
        default:
            break;
        }

        return false;
    }

    public ArrayList<KineticsRequest> GetTransitionCommandString(Actuator actuator) {
        ArrayList<KineticsRequest> Command = new ArrayList<KineticsRequest>();
        Command.add(new KineticsRequestServoEasing(EasingFunction, actuator));
        Command.add(new KineticsRequestEasingInOut(EasingType, actuator));
        Command.add(new KineticsRequestTiming(Timing, actuator));
        Command.add(new KineticsRequestDelay(Delay, actuator));

        if(Timing == 0 && Delay ==0)
        {
            Log.d("MotionTransition","ZERO TIMING CONDITION");
        }

        if(EasingFunction == CommandLabels.EasingFunction.TRI ||
            EasingFunction == CommandLabels.EasingFunction.SNW ||
            EasingFunction == CommandLabels.EasingFunction.TRW)
        {
            Command.add(new KineticsRequestFrequency(Frequency,  actuator));
        }
        if(EasingFunction == CommandLabels.EasingFunction.SPR)
        {

            Command.add(new KineticsRequestFrequency(Frequency,  actuator));
            Command.add(new KineticsRequestDamp(Damp,  actuator));
            Command.add(new KineticsRequestVelocity(Velocity, actuator));
        }
        return Command;
    }

    @Override
    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("Transition");

            Timing = dictionary.getInt("Timing");
            Delay = dictionary.getInt("Delay");
            Frequency = dictionary.getInt("Frequency");
            Damp = dictionary.getInt("Damp");
            Velocity = dictionary.getInt("Velocity");


            EasingFunction = CommandLabels.EasingFunction.valueOf(
            dictionary.getString("EasingFunction"));
            EasingType = CommandLabels.EasingType.valueOf(dictionary.getString("EasingType"));
        }
        catch (Exception e)
        {}
    }

    @Override
    public String Json()
    {

        String json = "";

        json = json.concat("{ \"Transition\" : {");

        json = json.concat(" \"Timing\" : \""+Timing.toString()+"\" ,");

        json = json.concat(" \"Delay\" : \""+Delay.toString()+"\" ,");

        json = json.concat(" \"Velocity\" : \""+Velocity.toString()+"\" ,");

        json = json.concat(" \"Frequency\" : \""+Frequency.toString()+"\" ,");

        json = json.concat(" \"Damp\" : \""+Damp.toString()+"\" ,");

        json = json.concat(" \"EasingFunction\" : \""+EasingFunction.toString()+"\" ,");

        json = json.concat(" \"EasingType\" : \""+EasingType.toString()+"\" ");

        json = json.concat("}}");

        return json;

    }


    public MotionAnimationTransition()
    {}

   public MotionAnimationTransition(
        Integer _Timing,
        Integer _Delay,
        Integer _Frequency,
        Integer _Damp,
        CommandLabels.EasingFunction _EasingFunction,
        CommandLabels.EasingType _EasingType)
    {
        Timing = _Timing;
        Delay = _Delay;
        Frequency = _Frequency;
        Damp = _Damp;
        EasingFunction = _EasingFunction;
        EasingType = _EasingType;
    }
}
