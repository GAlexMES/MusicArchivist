package de.brennecke.musicarchivst.sqlite;

/**
 * Created by Alexander on 28.10.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ALBUM = "album";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DISCOGS_ID = "discogs_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_COVERURL= "coverurl";
    public static final String COLUMN_BITMAP= "coverbitmap";
    public static final String COLUMN_GENRE= "genre";

    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MusicArchivist/albums.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_ALBUM + "(" +
            COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DISCOGS_ID + " integer , "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_ARTIST + " text, "
            + COLUMN_COVERURL + " text, "
            + COLUMN_GENRE + " text, "
            + COLUMN_BITMAP + " blob"
            + ");";

    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
        onCreate(db);
    }

}