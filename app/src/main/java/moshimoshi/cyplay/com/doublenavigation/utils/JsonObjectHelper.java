package moshimoshi.cyplay.com.doublenavigation.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 25-Mar-19.
 */

public class JsonObjectHelper {

    static public String getValue(Object object, String str) {
        String result = null;
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(object));

            String[] s = str.split("__");
            Object o = jsonObject;
            for (int i = 0; i < s.length; i++) {
                if (o instanceof JSONObject) {
                    o = ((JSONObject)o).get(s[i]);
                }
            }
            if (o instanceof String) {
                result = (String) o;
            }

        } catch (JSONException e) {
            return null;
        }

        return result;
    }
}
