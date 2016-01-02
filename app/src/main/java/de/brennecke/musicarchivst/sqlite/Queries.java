package de.brennecke.musicarchivst.sqlite;

import android.os.Environment;

/**
 * Created by Alexander on 13.12.2015.
 */
public class Queries {

    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MusicArchivist/albums.db";

    public static final String TABLE_ALBUM = "album";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DISCOGS_ID = "discogs_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_COVERURL = "coverurl";
    public static final String COLUMN_BITMAP = "coverbitmap";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_FAVORITE_ALBUM = "favoritealbum";

    public static final String[] TABLE_ALBUM_COLUMNS = {COLUMN_ID,
            COLUMN_TITLE, COLUMN_ARTIST, COLUMN_BITMAP, COLUMN_COVERURL, COLUMN_GENRE, COLUMN_DISCOGS_ID, COLUMN_FAVORITE_ALBUM};

    public static final String TABLE_TRACKS = "tracks";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_ALBUM_ID="album_id";
    public static final String COLUMN_TRACK_TITLE = "track_title";

    public static final String[] TABLE_TRACKS_COLUMNS = {COLUMN_POSITION,COLUMN_DURATION,COLUMN_TRACK_TITLE,COLUMN_ALBUM_ID};

    public static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_ALBUM + "(" +
            COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DISCOGS_ID + " integer , "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_ARTIST + " text, "
            + COLUMN_COVERURL + " text, "
            + COLUMN_GENRE + " text, "
            + COLUMN_BITMAP + " blob"
            + ");";

    public static final String ADD_FAVORITE_ALBUM = "ALTER TABLE " + TABLE_ALBUM + " ADD COLUMN " + COLUMN_FAVORITE_ALBUM + " INTEGER DEFAULT 0;";

    public static final String ADD_TRACK_TABLE = "CREATE TABLE "+ TABLE_TRACKS +" ('"+COLUMN_POSITION+"' INTEGER, '"+COLUMN_ALBUM_ID+"' INTEGER, '"+COLUMN_DURATION+"' INTEGER, '"+COLUMN_TRACK_TITLE+"' TEXT);";


}
