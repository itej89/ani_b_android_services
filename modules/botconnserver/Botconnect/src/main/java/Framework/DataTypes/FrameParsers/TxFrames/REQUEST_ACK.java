package Framework.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.Enums.ACK;
import Framework.DataTypes.Enums.ANIMSG;
import Framework.DataTypes.Enums.ANSTMSG;
import Framework.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import Framework.DataTypes.GenericExtentions;


public class REQUEST_ACK extends TxBaseFrame {
    String ID = "";
   public ACK jACK = ACK.NA;

    public REQUEST_ACK()
    {
        super();
    }

    public REQUEST_ACK(String _ID , ACK _ACK) {
        super();
        jANSTMSG = ANSTMSG.REQUEST_ACK;
        ID = _ID;
        jACK = _ACK;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.REQUEST_ACK.toString());
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
