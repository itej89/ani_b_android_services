package FrameworkInterface.DataTypes.FrameParsers.RxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.DataTypes.FrameParsers.TxFrames.TxBaseFrame;
import FrameworkInterface.Enums.ANIMSG;
import FrameworkInterface.Enums.ANSTMSG;
import FrameworkInterface.Enums.CATEGORY_TYPES;

public class ALIVE extends RxBaseFrame {

    String ID = "";

    public ALIVE() {
        super();
        jANIMSG = ANIMSG.ALIVE;
    }

    public ALIVE(String _ID) {
        super();
        jANIMSG = ANIMSG.ALIVE;
        ID = _ID;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANIMSG", ANIMSG.ALIVE.toString());
            json.put("ID", ID);

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
