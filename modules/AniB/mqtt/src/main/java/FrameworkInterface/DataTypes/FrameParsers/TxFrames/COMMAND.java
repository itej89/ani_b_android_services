package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ANSTMSG;
import FrameworkInterface.Enums.CATEGORY_TYPES;
import FrameworkInterface.Enums.COMMAND_TYPES;

public class COMMAND extends  TxBaseFrame {
    public String ID = "";
    public CATEGORY_TYPES TYPE = CATEGORY_TYPES.NA;
    public COMMAND_TYPES COMMAND = COMMAND_TYPES.NA;

    public COMMAND() {
        super();
        jANSTMSG = ANSTMSG.COMMAND;
    }

    public COMMAND(String _ID, CATEGORY_TYPES _TYPE, COMMAND_TYPES _COMMAND) {
        super();
        jANSTMSG = ANSTMSG.COMMAND;
        ID = _ID;
        TYPE = _TYPE;
        COMMAND = _COMMAND;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.CATEGORY.toString());
            json.put("ID", ID);
            json.put("CATEGORY", TYPE);
            json.put("COMMAND", COMMAND);

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
            COMMAND = COMMAND_TYPES.valueOf(jsonDictionary.getString("COMMAND"));
        }
        catch (Exception e)
        {}

    }
}
