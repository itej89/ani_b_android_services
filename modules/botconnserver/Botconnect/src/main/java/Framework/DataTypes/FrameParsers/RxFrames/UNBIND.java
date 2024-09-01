package Framework.DataTypes.FrameParsers.RxFrames;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Framework.DataTypes.Enums.ANIMSG;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GenericExtentions;

public class UNBIND extends RxBaseFrame {
    public String ID = "";
    public Map<LINK_ANCHORS, String> UNBIND_INFO = new HashMap<>();

    public UNBIND() {
        super();
        jANIMSG = ANIMSG.UNBIND;
    }

    public UNBIND(String _ID, Map<LINK_ANCHORS, String> _UNBIND_INFO) {
        super();
        jANIMSG = ANIMSG.UNBIND;
        ID = _ID;
        UNBIND_INFO = _UNBIND_INFO;
    }

    public String Json()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("ANIMSG", ANIMSG.UNBIND.toString());
            json.put("ID", ID);
            Map<String, String> _LINK_INFO = new HashMap<>();
            Iterator it = UNBIND_INFO.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<LINK_ANCHORS, String> pair = (Map.Entry) it.next();
                _LINK_INFO.put(pair.getKey().toString(), pair.getValue());
            }
            json.put("UNBIND_INFO", new JSONObject(_LINK_INFO));
            json.put("FRAME_ID", FRAME_ID);
        }
        catch (Exception e){}
        String jstrRet = json.toString();
        return jstrRet;
    }

    public void ParseJson(String Json)
    {
        super.ParseJson(Json);

        try {
            JSONObject jsonDictionary = GenericExtentions.parseJSONString(Json);

            ID = jsonDictionary.getString("ID");
            JSONObject jLINK_INFO = jsonDictionary.getJSONObject("UNBIND_INFO");
            Iterator<String> iter = jLINK_INFO.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = jLINK_INFO.getString(key);
                    UNBIND_INFO.put(LINK_ANCHORS.valueOf(key) , value);
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

