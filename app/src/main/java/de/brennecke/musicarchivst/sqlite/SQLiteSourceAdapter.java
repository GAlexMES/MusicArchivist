package de.brennecke.musicarchivst.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.brennecke.musicarchivst.model.Album;

/**
 * Created by Alexander on 28.10.2015.
 */
public class SQLiteSourceAdapter {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_TITLE, SQLiteHelper.COLUMN_ARTIST, SQLiteHelper.COLUMN_BITMAP, SQLiteHelper.COLUMN_COVERURL, SQLiteHelper.COLUMN_GENRE, SQLiteHelper.COLUMN_DISCOGS_ID};

    public SQLiteSourceAdapter(Context context) {
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
        values.put(SQLiteHelper.COLUMN_ARTIST, album.getArtist());
        values.put(SQLiteHelper.COLUMN_TITLE, album.getTitle());
        values.put(SQLiteHelper.COLUMN_GENRE, album.getGenre());
        values.put(SQLiteHelper.COLUMN_COVERURL, album.getAlbumCoverURL());
        values.put(SQLiteHelper.COLUMN_BITMAP, bitmapToBlob(album.getCoverBitmap()));
        values.put(SQLiteHelper.COLUMN_DISCOGS_ID, album.getID());

        long insertId = database.insert(SQLiteHelper.TABLE_ALBUM, null, values);
    }

    public Album getAlbum(String artist, String title) {
        String condition = SQLiteHelper.COLUMN_ARTIST + "=" + artist + " AND "
                + SQLiteHelper.COLUMN_TITLE + "=" + title;

        Cursor cursor = database.query(SQLiteHelper.TABLE_ALBUM,
                allColumns, condition, null,
                null, null, null);

        cursor.moveToFirst();
        Album newAlbum = cursorToAlbum(cursor);
        cursor.close();
        return newAlbum;
    }

    public List<Album> getAllArtists() {
        List<String> artistNames = new ArrayList<>();
        List<Album> artistList = new ArrayList<Album>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_ALBUM, allColumns, "", null, null, null, null);

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

    public List<Album> getAlbums(String artistName){
        List<Album> albumList = new ArrayList<Album>();

        String condition = SQLiteHelper.COLUMN_ARTIST + "='" + artistName+"'";
        Cursor cursor = database.query(SQLiteHelper.TABLE_ALBUM,
                allColumns, condition, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album album = cursorToAlbum(cursor);
            albumList.add(album);
            cursor.moveToNext();
        }
        cursor.close();
        return albumList;
    }

    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<Album>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_ALBUM, allColumns, "", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album album = cursorToAlbum(cursor);
            albumList.add(album);
            cursor.moveToNext();
        }
        cursor.close();
        return albumList;
    }

    public boolean existsAlbumInDB(Album currentAlbum) {

        String condition = SQLiteHelper.COLUMN_ARTIST + "='" + currentAlbum.getArtist()+"' AND "+ SQLiteHelper.COLUMN_TITLE+"='"+currentAlbum.getTitle()+"'";
        Cursor cursor = database.query(SQLiteHelper.TABLE_ALBUM,
                allColumns, condition, null,
                null, null, null);
        return cursor.getCount() == 0 ?false:true;
    }

    private Album cursorToAlbum(Cursor cursor) {
        Album album = new Album();
        album.setID(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
        album.setTitle(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TITLE)));
        album.setArtist(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_ARTIST)));
        album.setAlbumCoverURL(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_COVERURL)));

        byte[] bitmapBlob = cursor.getBlob(cursor.getColumnIndex(SQLiteHelper.COLUMN_BITMAP));
        album.setCoverBitmap(blobToBitmap(bitmapBlob));

        album.setGenre(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_GENRE)));
        return album;
    }

    private Bitmap blobToBitmap(byte[] bytes) {
        BitmapFactory.Options option2 = new BitmapFactory.Options();
        option2.inPreferredConfig = Bitmap.Config.RGB_565;
        option2.inDither = false;
        option2.inPurgeable = true;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option2);
    }

    private byte[] bitmapToBlob(Bitmap bm) {
        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(16 * 1024);
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bufferStream);
        return bufferStream.toByteArray();
    }
}
