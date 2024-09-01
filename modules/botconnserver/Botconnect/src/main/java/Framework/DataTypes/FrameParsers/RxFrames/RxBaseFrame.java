package Framework.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANIMSG;

public class RxBaseFrame {
    public ANIMSG jANIMSG = ANIMSG.NA;
    public String FRAME_ID = "";
    public  Boolean IsWaitForACK = false;

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
