package de.brennecke.musicarchivst.ServiceHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import de.brennecke.musicarchivst.model.Album;

/**
 * Created by Alexander on 25.10.2015.
 */
public class DiscogsServiceHandler extends ServiceHandler {

    private static final String QUERY_PLACEHOLDER = "%QUERYPLACEHOLDER%";
    private static final String URL = "https://api.discogs.com/database/search?q="+QUERY_PLACEHOLDER+"&token=";
    private static final String USER_TOKEN = "fGZuDSomRzFurkeAVMCYxZKnTWUWBqbPifrEUgwd";

    public static Album getDetailedInformation(String barcode, DownloadTask downloadTask) throws JSONException {
        downloadTask.onProgressUpdate(10);

        Album retval = new Album();
        String requestURL = URL + USER_TOKEN;
        requestURL = requestURL.replace(QUERY_PLACEHOLDER, barcode);

        JSONObject request = new JSONObject(getContentFromURL(requestURL));
        downloadTask.onProgressUpdate(20);
        JSONArray resultSet = request.getJSONArray("results");
        for(int i = 0; i<resultSet.length();i++){
            try{
                downloadTask.onProgressUpdate(30);
                String resourceURL = resultSet.getJSONObject(i).getString("resource_url");
                JSONObject albumDetails = new JSONObject(getContentFromURL(resourceURL));
                downloadTask.onProgressUpdate(40);
                retval.setTitle(albumDetails.getString("title"));
                retval.setGenre(albumDetails.getJSONArray("styles").getString(0));
                retval.setArtist(albumDetails.getJSONArray("artists").getJSONObject(0).optString("name"));
                downloadTask.onProgressUpdate(50);
                String albumCoverURL = ITuensServiceHandler.getAlbumCover(retval.getArtist(), retval.getTitle());
                downloadTask.onProgressUpdate(70);
                retval.setAlbumCoverURL(albumCoverURL);
                retval.setCoverBitmap(loadAlbumCover(retval.getAlbumCoverURL()));
                return retval;
            }
            catch (Exception e){
            }
        }
        downloadTask.onProgressUpdate(99);
        return retval;
    }

    private static Bitmap loadAlbumCover(String url) {
        if (url != null && !url.equals("")) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            BitmapFactory.Options bmOptions;
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            Bitmap bm = loadBitmap(url, bmOptions);
            return bm;
        }
        return null;
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        java.net.URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
        }
        return inputStream;
    }
}
