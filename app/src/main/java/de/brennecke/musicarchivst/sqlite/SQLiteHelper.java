package de.brennecke.musicarchivst.sqlite;

/**
 * Created by Alexander on 28.10.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 3;

    public SQLiteHelper(Context context) {
        super(context,Queries.DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        onUpgrade(database,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 0:
                db.execSQL(Queries.DATABASE_CREATE);
            case 1:
                db.execSQL(Queries.ADD_FAVORITE_ALBUM);
            case 2:
                db.execSQL(Queries.ADD_TRACK_TABLE);
            case 3:

        }
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion + ".");
    }
}