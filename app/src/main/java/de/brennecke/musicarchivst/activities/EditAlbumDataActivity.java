package de.brennecke.musicarchivst.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.ServiceHandler.DownloadTask;
import de.brennecke.musicarchivst.model.Album;

/**
 * Created by Alexander on 27.10.2015.
 */
public class EditAlbumDataActivity extends AppCompatActivity {
    private EditText artistTxt, albumTxt, genreTxt;

    private ProgressDialog mProgressDialog;

    private ImageView albumCoverImage;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Album album;

    private boolean scannerShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        album = new Album();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_album_layout);
        albumCoverImage = (ImageView) findViewById(R.id.image);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        artistTxt = (EditText) findViewById(R.id.artist_name_text);
        addInputMethod(artistTxt);
        albumTxt = (EditText) findViewById(R.id.album_name_text);
        addInputMethod(albumTxt);
        genreTxt = (EditText) findViewById(R.id.genre_text);
        addInputMethod(genreTxt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(EditAlbumDataActivity.this);
        mProgressDialog.setMessage("Fetching data for your album!");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        boolean showScanner = getIntent().getBooleanExtra("SHOW_SCANNER", false);
        if (showScanner && !scannerShowed) {
            scannerShowed = true;
            openScan();
        }

        updateTextView();
    }

    private void addInputMethod(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(m != null){
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(m != null){
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateTextView() {
        collapsingToolbarLayout.setTitle(album.getArtist() + "-" + album.getTitle());
        artistTxt.setText(album.getArtist());
        albumTxt.setText(album.getTitle());
        genreTxt.setText(album.getGenre());
        albumCoverImage.setImageBitmap(album.getCoverBitmap());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("backToActivity", String.valueOf(requestCode));
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

    public void updateUI(Album album){
        this.album = album;
        updateTextView();
    }

    private void showResult(String barcode) {
        Log.d("discogs", "search for: " + barcode);

        final DownloadTask downloadTask = new DownloadTask(EditAlbumDataActivity.this, mProgressDialog, this);
        downloadTask.execute(barcode);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    public void openScan() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(EditAlbumDataActivity.this);
        scanIntegrator.initiateScan();
    }


}
