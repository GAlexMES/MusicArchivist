package de.brennecke.musicarchivst.sqlite;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Track;
import de.brennecke.musicarchivst.model.Tracklist;

/**
 * Created by Alexander on 28.10.2015.
 */
public class SQLiteSourceAdapter {

    private static final String TAG = SQLiteSourceAdapter.class.getSimpleName();

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private Activity activeActivity;



    public SQLiteSourceAdapter(Context context) {
        activeActivity = ((Activity)context);
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addAlbum(Album album) {
        ContentValues values = new ContentValues();
        values.put(Queries.COLUMN_DISCOGS_ID, album.getID());
        values.put(Queries.COLUMN_ARTIST, album.getArtist());
        values.put(Queries.COLUMN_TITLE, album.getTitle());
        values.put(Queries.COLUMN_GENRE, album.getGenre());

        String albumURL = String.valueOf(getDefault(album.getAlbumCoverURL(), ""));
        values.put(Queries.COLUMN_COVERURL, albumURL);

        byte[] bitmap = bitmapToBlob(album.getCoverBitmap());
        values.put(Queries.COLUMN_BITMAP, (byte[]) getDefault(bitmap, new byte[1]));

        long insertId = database.insert(Queries.TABLE_ALBUM, null, values);

        insertTracks(album.getTracklist(), album.getID());
    }

    private void insertTracks(Tracklist tracklist, long albumID) {
        for (Track t : tracklist.getTracks()) {
            ContentValues values = new ContentValues();
            values.put(Queries.COLUMN_ALBUM_ID, albumID);
            values.put(Queries.COLUMN_POSITION, t.getTrackNumber());
            values.put(Queries.COLUMN_TRACK_TITLE, t.getName());
            values.put(Queries.COLUMN_DURATION, t.getDuration());

            long insertID = database.insert(Queries.TABLE_TRACKS, null, values);
        }
    }

    private Object getDefault(Object value, Object defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Album getAlbum(String artist, String title) {
        String condition = Queries.COLUMN_ARTIST + "=" + artist + " AND "
                + Queries.COLUMN_TITLE + "=? AND " + Queries.COLUMN_DISCOGS_ID + "=?";

        String sqliteQuery = "SELECT * FROM " + Queries.TABLE_ALBUM + " INNER JOIN " + Queries.TABLE_TRACKS + condition;

        String[] selectionArguments = {title, Queries.COLUMN_ALBUM_ID};
        Cursor cursor = database.rawQuery(sqliteQuery, selectionArguments);

        cursor.moveToFirst();
        Album newAlbum = cursorToAlbum(cursor);
        newAlbum.setTracklist(cursorToTracklist(cursor));
        cursor.close();
        return newAlbum;
    }

    public List<Album> getSearchResult(String query) {
        List<Album> retval = new ArrayList<>();
        String condition = Queries.COLUMN_TITLE + " LIKE '%" + query + "%'";

        Cursor cursor = database.query(Queries.TABLE_ALBUM,
                Queries.TABLE_ALBUM_COLUMNS, condition, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retval.add(cursorToAlbum(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return retval;
    }

    public List<Album> getAllArtists() {
        List<String> artistNames = new ArrayList<>();
        List<Album> artistList = new ArrayList<Album>();
        Cursor cursor = database.query(Queries.TABLE_ALBUM, Queries.TABLE_ALBUM_COLUMNS, "", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album album = cursorToAlbum(cursor);
            String artist = album.getArtist();
            if (!artistNames.contains(artist)) {
                artistList.add(album);
                artistNames.add(artist);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return artistList;
    }

    public List<Album> getAlbums(String artistName) {
        List<Album> albumList = new ArrayList<Album>();

        String condition = Queries.COLUMN_ARTIST + "='" + artistName + "'";
        Cursor cursor = database.query(Queries.TABLE_ALBUM,
                Queries.TABLE_ALBUM_COLUMNS, condition, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album album = cursorToAlbum(cursor);
            albumList.add(album);
            cursor.moveToNext();
        }
        cursor.close();

        for (Album a : albumList) {
            Tracklist t = getTracklist(a.getID());
            a.setTracklist(t);
        }
        return albumList;
    }

    private Tracklist getTracklist(long albumID) {
        String condition = Queries.COLUMN_ALBUM_ID + "='" + albumID + "'";
        Cursor cursor = database.query(Queries.TABLE_TRACKS,
                Queries.TABLE_TRACKS_COLUMNS, condition, null,
                null, null, null);

        Tracklist retval= cursorToTracklist(cursor);
        cursor.close();
        return retval;
    }

    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<Album>();

        Cursor cursor = database.query(Queries.TABLE_ALBUM, Queries.TABLE_ALBUM_COLUMNS, "", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album album = cursorToAlbum(cursor);
            albumList.add(album);
            cursor.moveToNext();
        }
        cursor.close();

        for (Album a : albumList) {
            Tracklist t = getTracklist(a.getID());
            a.setTracklist(t);
        }
        return albumList;
    }

    public boolean existsAlbumInDB(Album currentAlbum) {
        String condition = Queries.COLUMN_ARTIST + "='" + currentAlbum.getArtist() + "' AND " + Queries.COLUMN_TITLE + "='" + currentAlbum.getTitle() + "'";
        Cursor cursor = database.query(Queries.TABLE_ALBUM,
                Queries.TABLE_ALBUM_COLUMNS, condition, null,
                null, null, null);
        return cursor.getCount() == 0 ? false : true;
    }

    public void setFavoriteAlbum(Album album) {
        ContentValues removeAllFavorites = new ContentValues();
        removeAllFavorites.put(Queries.COLUMN_FAVORITE_ALBUM, 0);
        String condition = "";
        database.update(Queries.TABLE_ALBUM, removeAllFavorites, null, null);

        ContentValues setFavorite = new ContentValues();
        setFavorite.put(Queries.COLUMN_FAVORITE_ALBUM, 1);
        condition = Queries.COLUMN_ID + "=" + album.getID();
        database.update(Queries.TABLE_ALBUM, setFavorite, condition, null);
    }

    public void deleteAlbum(Album album) {
        String condition = Queries.COLUMN_ID + "=" + album.getID();
        database.delete(Queries.TABLE_ALBUM, condition, null);
    }

    public Bitmap getFavoriteAlbumCover() {
        String condition = Queries.COLUMN_FAVORITE_ALBUM + "=1";

        String[] coloums = {Queries.COLUMN_FAVORITE_ALBUM, Queries.COLUMN_BITMAP};
        Cursor cursor = database.query(Queries.TABLE_ALBUM,
                coloums, condition, null,
                null, null, null);

        cursor.moveToFirst();
        try {
            byte[] bitmapBlob = cursor.getBlob(cursor.getColumnIndex(Queries.COLUMN_BITMAP));
            return blobToBitmap(bitmapBlob);
        } catch (CursorIndexOutOfBoundsException | NullPointerException npee) {
            return null;
        }
    }

    private Album cursorToAlbum(Cursor cursor) {
        Album album = new Album();
        album.setID(cursor.getLong(cursor.getColumnIndex(Queries.COLUMN_DISCOGS_ID)));
        album.setTitle(cursor.getString(cursor.getColumnIndex(Queries.COLUMN_TITLE)));
        album.setArtist(cursor.getString(cursor.getColumnIndex(Queries.COLUMN_ARTIST)));
        album.setAlbumCoverURL(cursor.getString(cursor.getColumnIndex(Queries.COLUMN_COVERURL)));

        byte[] bitmapBlob = cursor.getBlob(cursor.getColumnIndex(Queries.COLUMN_BITMAP));
        album.setCoverBitmap(blobToBitmap(bitmapBlob));

        album.setGenre(cursor.getString(cursor.getColumnIndex(Queries.COLUMN_GENRE)));

        return album;
    }

    private Tracklist cursorToTracklist(Cursor cursor) {
        Tracklist retval = new Tracklist();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Track t = cursorToTrack(cursor);
            retval.addTrack(t);
            cursor.moveToNext();
        }
        return retval;
    }

    private Track cursorToTrack(Cursor cursor) {
        Track t = new Track();
        t.setDuration(cursor.getInt(cursor.getColumnIndex(Queries.COLUMN_DURATION)));
        t.setTrackNumber(cursor.getInt(cursor.getColumnIndex(Queries.COLUMN_POSITION)));
        t.setName(cursor.getString(cursor.getColumnIndex(Queries.COLUMN_TRACK_TITLE)));
        return t;
    }

    private Bitmap blobToBitmap(byte[] bytes) {
        BitmapFactory.Options option2 = new BitmapFactory.Options();
        option2.inPreferredConfig = Bitmap.Config.RGB_565;
        option2.inDither = false;
        option2.inPurgeable = true;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option2);
    }

    private byte[] bitmapToBlob(Bitmap bm) {
        try {
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(16 * 1024);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bufferStream);
            return bufferStream.toByteArray();
        } catch (NullPointerException npee) {
            return null;
        }
    }

    public Uri getDatabaseURI() {
        String path = database.getPath();
        Log.d(TAG, path);
        return Uri.parse(path);
    }
}
