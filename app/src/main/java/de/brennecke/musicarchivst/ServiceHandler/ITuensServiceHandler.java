package de.brennecke.musicarchivst.servicehandler;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Alexander on 27.10.2015.
 */
public class ITuensServiceHandler extends ServiceHandler {

    private final static String TERM_PLACEHOLDER =  "%ARTISTANDALBUM%";
    private final static String REQUEST_URL = "https://itunes.apple.com/search?term="+TERM_PLACEHOLDER+"&country=DE&entity=album";

    public static String getAlbumCover(String artist, String albumName) throws JSONException {
        String retval = "";
        String searchTerm = artist+"+"+albumName;
        searchTerm = searchTerm.replace(" ","+");
        Log.i("searchTerm",searchTerm);
        String requestURL = REQUEST_URL.replace(TERM_PLACEHOLDER,searchTerm);

        JSONObject result = new JSONObject(getContentFromURL(requestURL));
        int resultCount = result.getInt("resultCount");

        if(resultCount>0){
            JSONObject albumDetails = result.getJSONArray("results").getJSONObject(0);
            String coverURL = albumDetails.getString("artworkUrl100");
            retval = optimizeImageURL(coverURL,600);
        }
        Log.i("albumURL",retval);
        return retval;
    }

    private static String optimizeImageURL(String coverURL, int imageSize){
        String[] splittedtURL = coverURL.split(Pattern.quote("/"));

        String changedImageSize = splittedtURL[splittedtURL.length-1].replace("100x100",imageSize+"x"+imageSize);
        coverURL = splittedtURL[0];
        for(int i = 1;i<splittedtURL.length-1;i++){
            coverURL = coverURL+"/"+splittedtURL[i];
        }
        return coverURL+"/"+changedImageSize;

    }
}
