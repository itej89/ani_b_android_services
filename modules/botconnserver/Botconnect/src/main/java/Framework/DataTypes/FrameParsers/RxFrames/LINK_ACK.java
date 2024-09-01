package Framework.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.Enums.ACK;
import Framework.DataTypes.Enums.ANIMSG;
import Framework.DataTypes.GenericExtentions;


public class LINK_ACK extends RxBaseFrame {
    String ID = "";
    ACK jACK = ACK.NA;

    public LINK_ACK()
    {
        super();
    }

    public LINK_ACK(String _ID , ACK _ACK) {
        super();
        jANIMSG = ANIMSG.LINK_ACK;
        ID = _ID;
        jACK = _ACK;
        IsWaitForACK = false;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANIMSG", ANIMSG.LINK_ACK.toString());
            json.put("ID", ID);
            json.put("ACK", jACK);
            json.put("FRAME_ID", FRAME_ID);

        }
        catch (Exception e){}

        String jsonstr = json.toString();
        return jsonstr;

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
