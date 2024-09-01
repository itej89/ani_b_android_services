package Framework.DataTypes.FrameParsers.TxFrames;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Framework.DataTypes.Enums.ANSTMSG;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GenericExtentions;

public class STREAM extends TxBaseFrame {
    public String ID = "";
    public Map<LINK_ANCHORS, String> STREAM_DATA = new HashMap<>();

    public STREAM() {
        super();
        jANSTMSG = ANSTMSG.STREAM;
    }

    public STREAM(String _ID, Map<LINK_ANCHORS, String> _STREAM_DATA) {
        super();
        jANSTMSG = ANSTMSG.STREAM;
        ID = _ID;
        STREAM_DATA = _STREAM_DATA;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANSTMSG", ANSTMSG.DATA.toString());
            json.put("ID", ID);
            Map<String, String> _LINK_INFO = new HashMap<>();
            Iterator it = STREAM_DATA.entrySet().iterator();
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
            JSONObject jLINK_INFO = jsonDictionary.getJSONObject("STREAM_DATA");
            Iterator<String> iter = jLINK_INFO.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = jLINK_INFO.getString(key);
                    STREAM_DATA.put(LINK_ANCHORS.valueOf(key) , value);
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
