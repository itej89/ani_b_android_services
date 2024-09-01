package Framework.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.Enums.ANSTMSG;
import Framework.DataTypes.Enums.CATEGORY_TYPES;

public class CATEGORY extends TxBaseFrame {

    String ID = "";
   public CATEGORY_TYPES TYPE = CATEGORY_TYPES.NA;


    public CATEGORY() {
        super();
        jANSTMSG = ANSTMSG.CATEGORY;
    }

    public CATEGORY(String _ID, CATEGORY_TYPES _TYPE) {
        super();
        jANSTMSG = ANSTMSG.CATEGORY;
        ID = _ID;
        TYPE = _TYPE;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.CATEGORY.toString());
            json.put("ID", ID);
            json.put("CATEGORY", TYPE);

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
        }
        catch (Exception e)
        {}
    }
}
