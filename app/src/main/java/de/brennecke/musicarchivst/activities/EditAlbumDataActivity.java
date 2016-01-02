package de.brennecke.musicarchivst.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.dialogs.SimpleDialog;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Exchange;
import de.brennecke.musicarchivst.servicehandler.DownloadTask;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;
import de.brennecke.musicarchivst.view.TracklistHandler;

/**
 * Created by Alexander on 27.10.2015.
 */
public class EditAlbumDataActivity extends AppCompatActivity {
    private EditText artistTxt, albumTxt, genreTxt;

    private LinearLayout tracklistList;

    private static final String TAG = EditAlbumDataActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog;
    private ImageView albumCoverImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Album album;

    private boolean showExisting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        album = new Album();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_album);
        initUI();
        String barcode = getIntent().getStringExtra("BARCODE");
        if (barcode != null) {
            showResult(barcode);

        } else {
            showExisting = getIntent().getBooleanExtra("SHOW_EXISTING", false);
            if (showExisting) {
                album = Exchange.getInstance().getCurrentAlbum();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_album, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveAlbumToDatabase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "back to activity. request code:" + String.valueOf(requestCode));
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                showResult(contents);
            } else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void initUI() {
        initTextFields();
        initProgressDialog();
        albumCoverImage = (ImageView) findViewById(R.id.image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabSaveButton = (FloatingActionButton) findViewById(R.id.fab_save_album);
        if (fabSaveButton != null) {
            fabSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveAlbumToDatabase();
                }
            });
        }
        updateUI();
    }

    private void initTextFields() {
        artistTxt = (EditText) findViewById(R.id.artist_name_text);
        addInputMethod(artistTxt);
        albumTxt = (EditText) findViewById(R.id.album_name_text);
        addInputMethod(albumTxt);
        genreTxt = (EditText) findViewById(R.id.genre_text);
        addInputMethod(genreTxt);
        tracklistList = (LinearLayout) findViewById(R.id.tracklist_view);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(EditAlbumDataActivity.this);
        mProgressDialog.setMessage("Fetching data for your album!");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
    }

    private void updateUI() {
        collapsingToolbarLayout.setTitle(album.getArtist() + "-" + album.getTitle());
        artistTxt.setText(album.getArtist());
        albumTxt.setText(album.getTitle());
        genreTxt.setText(album.getGenre());
        TracklistHandler.update(tracklistList, album.getTracklist(), this);
        albumCoverImage.setImageBitmap(album.getCoverBitmap());
    }

    private void addInputMethod(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private boolean saveAlbumToDatabase() {
        Log.i(TAG, "saveAlbum");

        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(EditAlbumDataActivity.this);
        sqLiteSourceAdapter.open();
        Album currentAlbum = getCurrentEnteredValues();

        boolean enoughInputs = checkAlbum(currentAlbum);



        if(!enoughInputs){
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.not_enough_values), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (!showExisting) {
            boolean alreadyExist = sqLiteSourceAdapter.existsAlbumInDB(currentAlbum);
            if (!alreadyExist) {
                sqLiteSourceAdapter.addAlbum(currentAlbum);
                sqLiteSourceAdapter.close();
                Log.i(TAG, "album saved");

                CharSequence text = "Album saved!";

                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();

                finish();
            } else {
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_name");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }
                SimpleDialog sd = new SimpleDialog();
                sd.setActivity(this);
                try {
                    sd.setTitle(R.string.dialog_duplicated_album);
                    sd.setText(R.string.dialog_duplicated_album_info);
                    sd.show(manager, "fragment_edit_name");
                } catch (Exception e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
            }
        }
        return true;
    }

    private Album getCurrentEnteredValues() {
        Album retval = new Album(album);
        retval.setArtist(artistTxt.getText().toString());
        retval.setGenre(genreTxt.getText().toString());
        retval.setTitle(albumTxt.getText().toString());
        retval.setAlbumCoverURL(album.getAlbumCoverURL());
        retval.setCoverBitmap(album.getCoverBitmap());
        return retval;
    }

    private boolean checkAlbum(Album album){
        boolean titleAccepted = album.getTitle() != null && !album.getTitle().equals("");
        boolean artistAccepted = album.getArtist() != null && !album.getArtist().equals("");
        return titleAccepted && artistAccepted;
    }

    private void showResult(String barcode) {
        Log.d(TAG, "discogs search for: " + barcode);

        final DownloadTask downloadTask = new DownloadTask(EditAlbumDataActivity.this, mProgressDialog, this);
        downloadTask.execute(barcode);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    public void setAlbum(Album album) {
        this.album = album;
        updateUI();
    }
}
