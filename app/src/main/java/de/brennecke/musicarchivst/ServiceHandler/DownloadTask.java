package de.brennecke.musicarchivst.ServiceHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import org.json.JSONException;

import de.brennecke.musicarchivst.activities.EditAlbumDataActivity;
import de.brennecke.musicarchivst.model.Album;

/**
 * Created by Alexander on 27.10.2015.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private Album result;

    private EditAlbumDataActivity editAlbumDataActivity;
    private ProgressDialog mProgressDialog;

    public DownloadTask(Context context, ProgressDialog mProgressDialog, EditAlbumDataActivity eda) {
        this.context = context;
        this.mProgressDialog = mProgressDialog;
        this.editAlbumDataActivity =eda;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            result = DiscogsServiceHandler.getDetailedInformation(params[0],this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (this.result != null) {
            editAlbumDataActivity.updateUI(this.result);
            Toast.makeText(context, "Information received", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        }

    }
}