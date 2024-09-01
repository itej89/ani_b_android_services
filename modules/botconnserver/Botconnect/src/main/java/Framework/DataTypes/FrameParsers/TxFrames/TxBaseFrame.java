package Framework.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANSTMSG;

public class TxBaseFrame {
   public ANSTMSG jANSTMSG = ANSTMSG.NA;
    public String FRAME_ID = "";


    public void ParseJson(String Json)
    {
        try {

            JSONObject jsonDictionary = GenericExtentions.parseJSONString(Json);

            if(jsonDictionary.getString("ANSTMSG") != null) {
                jANSTMSG = ANSTMSG.valueOf(jsonDictionary.getString("ANSTMSG"));
            }

            FRAME_ID = jsonDictionary.getString("FRAME_ID");
        }
        catch (Exception e)
        {}
    }
}


