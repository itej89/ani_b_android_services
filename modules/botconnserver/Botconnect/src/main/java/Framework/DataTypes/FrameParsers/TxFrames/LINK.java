package Framework.DataTypes.FrameParsers.TxFrames;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Framework.DataTypes.Enums.ANSTMSG;
import Framework.DataTypes.Enums.BIND_TYPES;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GenericExtentions;
import Framework.DataTypes.LinkInformation;

public class LINK extends TxBaseFrame {
    public String ID = "";
    public Map<LINK_ANCHORS, LinkInformation> LINK_INFO = new HashMap<>();

    public LINK() {
        super();
        jANSTMSG = ANSTMSG.LINK;
    }

    public LINK(String _ID, Map<LINK_ANCHORS, LinkInformation> _LINK_INFO) {
        super();
        jANSTMSG = ANSTMSG.LINK;
        ID = _ID;
        LINK_INFO = _LINK_INFO;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANSTMSG", ANSTMSG.LINK.toString());
            json.put("ID", ID);
            Map<String, String> _LINK_INFO = new HashMap<>();
            Iterator it = LINK_INFO.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<LINK_ANCHORS, String> pair = (Map.Entry) it.next();
                _LINK_INFO.put(pair.getKey().toString(), pair.getValue());
            }
            json.put("LINK_INFO", new JSONObject(_LINK_INFO));
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
            JSONObject jLINK_INFO = jsonDictionary.getJSONObject("LINK_INFO");
            Iterator<String> iter = jLINK_INFO.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject jLINK_INFORMATION = jLINK_INFO.getJSONObject(key);
                    String BindType = jLINK_INFORMATION.getString("BIND_TYPE");
                    String Data = jLINK_INFORMATION.getString("DATA");
                    LINK_INFO.put(LINK_ANCHORS.valueOf(key) , new LinkInformation(LINK_ANCHORS.valueOf(key), BIND_TYPES.valueOf(BindType), Data));
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
        }
        catch (Exception e)
        {
            Log.e("DATA", e.getMessage());
        }
    }
}
