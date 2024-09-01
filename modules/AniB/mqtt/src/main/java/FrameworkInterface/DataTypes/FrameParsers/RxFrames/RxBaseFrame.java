package FrameworkInterface.DataTypes.FrameParsers.RxFrames;

import android.util.StateSet;

import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ANIMSG;

public class RxBaseFrame {
    public ANIMSG jANIMSG = ANIMSG.NA;
    public String FRAME_ID = "";

    public void ParseJson(String Json)
    {
        try {

            JSONObject jsonDictionary = GenericExtentions.parseJSONString(Json);

            jANIMSG = ANIMSG.valueOf(jsonDictionary.getString("ANIMSG"));
        }
        catch (Exception e)
        {}
    }
}
