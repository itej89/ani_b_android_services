package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANSTMSG;

public class DISCOVER extends TxBaseFrame {

    public DISCOVER() {
        super();
        jANSTMSG = ANSTMSG.DISCOVER;
    }

    public String Json()
    {

        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.DISCOVER.toString());

        }
        catch (Exception e){}

        return json.toString();
    }

    public void ParseJson(String Json)
    {
        super.ParseJson(Json);
    }
}
