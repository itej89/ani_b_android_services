package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ANSTMSG;

public class DISCONNECT extends TxBaseFrame {
    String ID = "";

    public DISCONNECT() {
        super();
        jANSTMSG = ANSTMSG.DISCONNECT;
    }

    public DISCONNECT(String _ID) {
        super();
        jANSTMSG = ANSTMSG.DISCONNECT;
        ID = _ID;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.DISCONNECT.toString());
            json.put("ID", ID);
            json.put("FRAME_ID", FRAME_ID);

        }
        catch (Exception e){}

        return json.toString();


    }

    public void ParseJson(String Json)
    {
        super.ParseJson(Json);
        try {

            JSONObject jsonDictionary = GenericExtentions.parseJSONString(Json);

            ID = jsonDictionary.getString("ID");
        }
        catch (Exception e)
        {}
    }
}
