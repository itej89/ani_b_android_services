package Framework.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANSTMSG;

public class REQEST_UPLOAD extends TxBaseFrame {
    String ID = "";
    Integer CHUNK_COUNT = 0;
    String MD5 = "";

    public REQEST_UPLOAD() {
        super();
        jANSTMSG = ANSTMSG.REQEST_UPLOAD;
    }

    public REQEST_UPLOAD(String _ID,Integer _CHUNKCount, String _MD5) {
        super();
        jANSTMSG = ANSTMSG.REQEST_UPLOAD;
        ID = _ID;
        CHUNK_COUNT = _CHUNKCount;
        MD5 = _MD5;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.REQEST_UPLOAD.toString());
            json.put("ID", ID);
            json.put("CHUNK_COUNT", CHUNK_COUNT.toString());
            json.put("MD5", MD5);
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
            CHUNK_COUNT = Integer.parseInt(jsonDictionary.getString("CHUNK_COUNT"));
            MD5 = jsonDictionary.getString("MD5");
        }
        catch (Exception e)
        {}

    }
}
