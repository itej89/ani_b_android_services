package Framework.DataTypes.FrameParsers.RxFrames;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Framework.DataTypes.Enums.ANIMSG;
import Framework.DataTypes.Enums.ANSTMSG;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GenericExtentions;

public class REQUEST extends RxBaseFrame {
    public String ID = "";
    public Map<LINK_ANCHORS, String> REQUEST_DATA = new HashMap<>();

    public REQUEST() {
        super();
        jANIMSG = ANIMSG.REQUEST;
    }

    public REQUEST(String _ID, Map<LINK_ANCHORS, String> _REQUEST_DATA) {
        super();
        jANIMSG = ANIMSG.REQUEST;
        ID = _ID;
        REQUEST_DATA = _REQUEST_DATA;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANIMSG", ANIMSG.REQUEST.toString());
            json.put("ID", ID);
            Map<String, String> _LINK_INFO = new HashMap<>();
            Iterator it = REQUEST_DATA.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<LINK_ANCHORS, String> pair = (Map.Entry) it.next();
                _LINK_INFO.put(pair.getKey().toString(), pair.getValue());
            }
            json.put("REQUEST_DATA", new JSONObject(_LINK_INFO));
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
            JSONObject jLINK_INFO = jsonDictionary.getJSONObject("REQUEST_DATA");
            Iterator<String> iter = jLINK_INFO.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = jLINK_INFO.getString(key);
                    REQUEST_DATA.put(LINK_ANCHORS.valueOf(key) , value);
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
