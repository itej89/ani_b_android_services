package Framework.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANSTMSG;

public class UPLOAD_END extends TxBaseFrame {
    String ID = "";

    public UPLOAD_END() {
        super();
        jANSTMSG = ANSTMSG.UPLOAD_END;
    }

    public UPLOAD_END(String _ID) {
        super();
        jANSTMSG = ANSTMSG.UPLOAD_END;
        ID = _ID;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.UPLOAD_END.toString());
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
