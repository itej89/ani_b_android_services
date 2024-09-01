package Framework.DataTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericExtentions {

    public  static long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public  static ArrayList<Byte> ArrayToArrayList(byte[] arrayData)
    {
        try {
            ArrayList<Byte> arrayListData = new ArrayList<Byte>();
            for (byte t : arrayData) {
                // Add each element into the list
                arrayListData.add(t);
            }
            return  arrayListData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    public  static  byte[] ArrayListToArray(ArrayList<Byte> arrayListData)
    {
        try {
            byte[] pData = new byte[arrayListData.size()];
            for (int i = 0; i < arrayListData.size(); i++) {
                pData[i] = (byte) arrayListData.get(i);
            }

            return  pData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }


    public  static  JSONObject parseJSONString(String jsonString)
    {
        try {
            return  new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  null;
    }

    public static String removeSpecialCharsFromString(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

}
