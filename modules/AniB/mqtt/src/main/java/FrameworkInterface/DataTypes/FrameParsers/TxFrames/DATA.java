package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import android.util.Base64;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ANSTMSG;
import FrameworkInterface.Enums.CATEGORY_TYPES;

public class DATA extends TxBaseFrame {

    String ID = "";
    CATEGORY_TYPES TYPE = CATEGORY_TYPES.NA;
    byte[] DATA;

    public DATA() {
        super();
        jANSTMSG = ANSTMSG.DATA;
    }

    public DATA(String _ID, CATEGORY_TYPES _TYPE, byte[] _DATA) {
        super();
        jANSTMSG = ANSTMSG.DATA;
        ID = _ID;
        TYPE = _TYPE;
        DATA = _DATA;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANSTMSG", ANSTMSG.DATA.toString());
            json.put("ID", ID);
            json.put("CATEGORY", TYPE);
            String encodedData = Base64.encodeToString(DATA, 0);
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
            TYPE = CATEGORY_TYPES.valueOf(jsonDictionary.getString("ID"));
            String base64Data = jsonDictionary.getString("DATA");
            DATA = Base64.decode(base64Data ,0);
        }
        catch (Exception e)
        {}
    }
}
