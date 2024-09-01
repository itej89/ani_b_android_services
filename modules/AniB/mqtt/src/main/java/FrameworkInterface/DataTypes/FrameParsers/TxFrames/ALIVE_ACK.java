package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.DataTypes.FrameParsers.RxFrames.RxBaseFrame;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANIMSG;
import FrameworkInterface.Enums.ANSTMSG;

public class ALIVE_ACK extends TxBaseFrame {

    String ID = "";
   public ACK jACK = ACK.NA;

    public ALIVE_ACK()
    {
        super();
    }

    public ALIVE_ACK(String _ID, ACK _ACK) {
        super();
        jANSTMSG = ANSTMSG.ALIVE_ACK;
        ID = _ID;
        jACK = _ACK;
    }


    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.ALIVE_ACK.toString());
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
