package Framework.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ACK;
import Framework.DataTypes.Enums.ANIMSG;

public class CATEGORY_ACK extends RxBaseFrame {
    String ID = "";
    ACK jACK = ACK.NA;

    public CATEGORY_ACK()
    {
        super();
    }

    public CATEGORY_ACK(String _ID , ACK _ACK) {
        super();
        jANIMSG = ANIMSG.CATEGORY_ACK;
        ID = _ID;
        jACK = _ACK;
        IsWaitForACK = false;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANIMSG", ANIMSG.CATEGORY_ACK.toString());
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
