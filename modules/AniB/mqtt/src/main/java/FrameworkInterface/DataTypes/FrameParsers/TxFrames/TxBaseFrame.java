package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANSTMSG;

public class TxBaseFrame {
   public ANSTMSG jANSTMSG;
    public String FRAME_ID = "";

    public void ParseJson(String Json)
    {
        try {

            JSONObject jsonDictionary = GenericExtentions.parseJSONString(Json);

            jANSTMSG = ANSTMSG.valueOf(jsonDictionary.getString("ANSTMSG"));
        }
        catch (Exception e)
        {}
    }
}


