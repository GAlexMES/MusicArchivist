package de.brennecke.musicarchivst.discogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import de.brennecke.musicarchivst.model.Album;

/**
 * Created by Alexander on 25.10.2015.
 */
public class ServiceHandler {

    private static final String QUERY_PLACEHOLDER = "%QUERYPLACEHOLDER%";
    private static final String URL = "https://api.discogs.com/database/search?q="+QUERY_PLACEHOLDER+"&token=";
    private static final String USER_TOKEN = "fGZuDSomRzFurkeAVMCYxZKnTWUWBqbPifrEUgwd";

    public static Album getDetailedInformation(String barcode) throws JSONException {
        Album retval = new Album();

        String requestURL = URL + USER_TOKEN;
        requestURL = requestURL.replace(QUERY_PLACEHOLDER, barcode);

        JSONObject request = new JSONObject(getContentFromURL(requestURL));
        JSONArray resultSet = request.getJSONArray("results");
        for(int i = 0; i<resultSet.length();i++){
            try{
                String resourceURL = resultSet.getJSONObject(i).getString("resource_url");

                JSONObject albumDetails = new JSONObject(getContentFromURL(resourceURL));
                retval.setTitle(albumDetails.getString("title"));
                retval.setGenre(albumDetails.getJSONArray("styles").getString(0));
                retval.setArtist(albumDetails.getJSONArray("artists").getJSONObject(0).optString("name"));
            }
            catch (Exception e){

            }
        }
        return retval;
    }

    private static String getContentFromURL(String url){
        String retval = "";
        try {
            URL requestURL = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            requestURL.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                retval = retval + inputLine;
            }

            in.close();
            return retval;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retval;
    }
}
