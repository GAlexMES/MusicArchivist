package de.brennecke.musicarchivst;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alexander on 04.01.2016.
 */
public class ApplicationContextHelper extends Application{
    private static Context context;

    public void onCreate() {
        super.onCreate();
        ApplicationContextHelper.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ApplicationContextHelper.context;
    }
}
