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

    private static final int DATABASE_VERSION = 2;

    public SQLiteHelper(Context context) {
        super(context,Queries.DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Queries.DATABASE_CREATE);
        database.execSQL(Queries.ADD_FAVORITE_ALBUM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL(Queries.ADD_FAVORITE_ALBUM);
            case 2:

        }
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion + ".");
    }
}