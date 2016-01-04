package de.brennecke.musicarchivst.servicehandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alexander on 04.01.2016.
 */
public class JSONHelper {

    public static String getString(String key,String defaultValue, JSONObject json){
        try{
            return json.getString(key);
        }
        catch(JSONException jse){
            return defaultValue;
        }
    }

    public static JSONArray getJSONArray(String key, JSONArray defaultValue, JSONObject json){
        try{
            return json.getJSONArray(key);
        }
        catch(JSONException jse){
            return defaultValue;
        }
    }

    public static Integer getInt(String key,int defaultValue, JSONObject json){
        try{
            return json.getInt(key);
        }
        catch(JSONException jse){
            return defaultValue;
        }
    }


}
