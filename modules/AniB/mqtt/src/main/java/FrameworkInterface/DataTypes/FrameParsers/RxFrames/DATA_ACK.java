package FrameworkInterface.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANIMSG;

public class DATA_ACK extends RxBaseFrame {
    String ID = "";
    ACK jACK = ACK.NA;

    public DATA_ACK()
    {
        super();
    }

    public DATA_ACK(String _ID , ACK _ACK) {
        super();
        jANIMSG = ANIMSG.DATA_ACK;
        ID = _ID;
        jACK = _ACK;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANIMSG.DATA_ACK.toString());
            json.put("ID", ID);
            json.put("ACK", jACK);
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
            jACK = ACK.valueOf(jsonDictionary.getString("ACK"));


        }
        catch (Exception e)
        {}
    }
}
