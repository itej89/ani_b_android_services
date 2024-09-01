package Framework.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANSTMSG;
import Framework.DataTypes.Enums.CATEGORY_TYPES;

public class DATA extends TxBaseFrame {

    public String ID = "";
    public int BLOCK_COUNT = 0;
    public CATEGORY_TYPES TYPE = CATEGORY_TYPES.NA;
    public byte[] DATA;

    public DATA() {
        super();
        jANSTMSG = ANSTMSG.DATA;
    }

    public DATA(String _ID, CATEGORY_TYPES _TYPE, byte[] _DATA, int _BLOCK_COUNT) {
        super();
        jANSTMSG = ANSTMSG.DATA;
        ID = _ID;
        TYPE = _TYPE;
        DATA = _DATA;
        BLOCK_COUNT = _BLOCK_COUNT;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANSTMSG", ANSTMSG.DATA.toString());
            json.put("ID", ID);
            json.put("CATEGORY", TYPE);
            String encodedData = Base64.encodeToString(DATA, 0);
            json.put("BLOCK_COUNT", BLOCK_COUNT);
            json.put("DATA", encodedData);

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
            TYPE = CATEGORY_TYPES.valueOf(jsonDictionary.getString("CATEGORY"));
            String base64Data = jsonDictionary.getString("DATA");
            BLOCK_COUNT = jsonDictionary.getInt("BLOCK_COUNT");

            DATA = Base64.decode(base64Data ,0);

        }
        catch (Exception e)
        {
            Log.e("DATA", e.getMessage());
        }
    }
}
