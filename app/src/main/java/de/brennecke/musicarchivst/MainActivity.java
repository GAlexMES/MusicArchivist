package de.brennecke.musicarchivst;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import de.brennecke.musicarchivst.buttonlisteners.ScanButtonListener;
import de.brennecke.musicarchivst.discogs.ServiceHandler;
import de.brennecke.musicarchivst.model.Album;


public class MainActivity extends AppCompatActivity {

    private TextView artistTxt, albumTxt, genreTxt;

    private ProgressDialog mProgressDialog;

    private String artistName = "";
    private String albumName = "";
    private String genre = "";

    private void updateTextView(){
        artistTxt.setText(artistName);
        albumTxt.setText(albumName);
        genreTxt.setText(genre);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistTxt = (TextView) findViewById(R.id.artist_name_text);
        albumTxt = (TextView) findViewById(R.id.album_name_text);
        genreTxt = (TextView) findViewById(R.id.genre_text);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.scan_fab);
        if(scanButton != null) {
            scanButton.setOnClickListener(new ScanButtonListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String barcode = scanningResult.getContents();
            showResult(barcode);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showResult(String barcode){
        Log.d("discogs", "search for: " + barcode);

        final DownloadTask downloadTask = new DownloadTask(MainActivity.this);
        downloadTask.execute(barcode);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private Album result;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = ServiceHandler.getDetailedInformation(params[0]);
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
                Toast.makeText(context, "Information received", Toast.LENGTH_SHORT).show();
                artistName = this.result.getArtist();
                albumName = this.result.getTitle();
                genre = this.result.getGenre();
                updateTextView();
            }
            else{
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            }

        }
    }


}
