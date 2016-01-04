package de.brennecke.musicarchivst.servicehandler;

import android.content.res.Resources;
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

import de.brennecke.musicarchivst.ApplicationContextHelper;
import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Track;
import de.brennecke.musicarchivst.model.Tracklist;

/**
 * Created by Alexander on 25.10.2015.
 */
public class DiscogsServiceHandler extends ServiceHandler {

    private static final String QUERY_PLACEHOLDER = "%QUERYPLACEHOLDER%";
    private static final String URL = "https://api.discogs.com/database/search?q=" + QUERY_PLACEHOLDER + "&token=";
    private static final String USER_TOKEN = "fGZuDSomRzFurkeAVMCYxZKnTWUWBqbPifrEUgwd";

    public static Album getDetailedInformation(String barcode, DownloadTask downloadTask) throws JSONException {
        downloadTask.onProgressUpdate(5);

        Album retval = new Album();
        String requestURL = URL + USER_TOKEN;
        requestURL = requestURL.replace(QUERY_PLACEHOLDER, barcode);

        JSONObject request = new JSONObject(getContentFromURL(requestURL));
        downloadTask.onProgressUpdate(10);
        JSONArray resultSet = request.getJSONArray("results");
        for (int i = 0; i < resultSet.length(); i++) {
            try {
                downloadTask.onProgressUpdate(15);
                String resourceURL = resultSet.getJSONObject(i).getString("resource_url");
                retval.setID(resultSet.getJSONObject(i).getLong("id"));
                JSONObject albumDetails = new JSONObject(getContentFromURL(resourceURL));
                retval = addDetailsToAlbum(retval, albumDetails, downloadTask);
                return retval;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        downloadTask.onProgressUpdate(99);
        return retval;
    }

    private static Album addDetailsToAlbum(Album album, JSONObject json, DownloadTask downloadTask) throws JSONException {
        Resources resources = ApplicationContextHelper.getAppContext().getResources();
        downloadTask.onProgressUpdate(20);


        String titlePlaceholder = resources.getString(R.string.album_placeholder_title);
        String albumTitle = JSONHelper.getString("title", titlePlaceholder, json);
        album.setTitle(albumTitle);

        JSONArray albumGenres = JSONHelper.getJSONArray("styles", null, json);
        String albumGenre = resources.getString(R.string.album_placeholder_genre);
        if (albumGenres != null) {
            albumGenre = albumGenres.getString(0);
        }
        album.setGenre(albumGenre);

        JSONArray albumArtists = JSONHelper.getJSONArray("artists", null, json);
        JSONObject firstArtist = albumArtists.getJSONObject(0);
        String artistPlaceholder = resources.getString(R.string.album_placeholder_artist);
        String albumArtist = JSONHelper.getString("name", "", firstArtist);
        album.setArtist(albumArtist);

        downloadTask.onProgressUpdate(30);
        String albumCoverURL = ITuensServiceHandler.getAlbumCover(albumArtist, albumTitle);
        downloadTask.onProgressUpdate(50);
        album.setAlbumCoverURL(albumCoverURL);
        album.setCoverBitmap(loadAlbumCover(album.getAlbumCoverURL()));
        downloadTask.onProgressUpdate(80);

        JSONArray albumTrackList = JSONHelper.getJSONArray("tracklist",null,json);
        Tracklist tracklist = getTracklist(albumTrackList);
        album.setTracklist(tracklist);

        return album;
    }

    private static Tracklist getTracklist(JSONArray tracklist) {
        Tracklist tracks = new Tracklist();

        if (tracklist==null){
            return tracks;
        }

        for (int i = 0; i < tracklist.length(); i++) {
            try {
                JSONObject track = tracklist.getJSONObject(i);
                tracks.addTrack(getTrack(track));
            } catch (JSONException jse) {
                jse.printStackTrace();
            }
        }
        return tracks;
    }

    private static Track getTrack(JSONObject track) throws JSONException {
        Track retval = new Track();

        String duration = JSONHelper.getString("duration", "", track);
        retval.setDuration(durationToSeconds(duration));

        int position= JSONHelper.getInt("position", 0, track);
        retval.setTrackNumber(position);

        String title = JSONHelper.getString("title","-",track);
        retval.setName(title);

        return retval;
    }

    private static int durationToSeconds(String duration) {
        String timeRegex = "^[0-9]+:[0-9]{2}$";
        boolean matches = duration.matches(timeRegex);
        if(!matches){
            return 0;
        }
        String[] splitted = duration.split(":");
        int seconds = Integer.valueOf(splitted[1]);
        int minutes = (60 * Integer.valueOf(splitted[0]));
        return minutes + seconds;
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
