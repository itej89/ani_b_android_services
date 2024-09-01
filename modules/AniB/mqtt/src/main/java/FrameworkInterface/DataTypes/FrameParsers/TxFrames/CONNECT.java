package FrameworkInterface.DataTypes.FrameParsers.TxFrames;

import org.json.JSONObject;

import Framework.DataTypes.GenericExtentions;
import FrameworkInterface.Enums.ACK;
import FrameworkInterface.Enums.ANSTMSG;

public class CONNECT extends TxBaseFrame {
    String ID = "";

    public CONNECT() {
        super();
        jANSTMSG = ANSTMSG.CONNECT;
    }

    public CONNECT(String _ID) {
        super();
        jANSTMSG = ANSTMSG.CONNECT;
        ID = _ID;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("ANSTMSG", ANSTMSG.DISCOVER.toString());
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
