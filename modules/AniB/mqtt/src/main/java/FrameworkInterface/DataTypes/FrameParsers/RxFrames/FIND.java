package FrameworkInterface.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.RxBaseFrame;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANIMSG;

public class FIND extends RxBaseFrame {
    String ID = "";
    String NAME = "";
    ACK jACK = ACK.NA;


    public FIND()
    {
        super();
    }

    public FIND(String _ID, String _NAME, ACK _ACK) {
        super();
        jANIMSG = ANIMSG.FIND;
        ID = _ID;
        NAME = _NAME;
        jACK = _ACK;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANIMSG.FIND.toString());
            json.put("ID", ID);
            json.put("NAME", NAME);
            json.put("ACK", jACK.toString());
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
            NAME = jsonDictionary.getString("NAME");
            jACK = ACK.valueOf(jsonDictionary.getString("ACK"));
        }
        catch (Exception e)
        {}
    }
}
