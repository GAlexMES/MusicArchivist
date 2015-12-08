package de.brennecke.musicarchivst.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.EditAlbumDataActivity;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Exchange;

/**
 * Created by Alexander on 06.12.2015.
 */
public class ShowAlbumActivity extends AppCompatActivity {
    private TextView artistTxt, albumTxt, genreTxt;
    private ImageView albumCoverImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Album displayedAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayedAlbum = Exchange.getInstance().getCurrentAlbum();
        setContentView(R.layout.fragment_show_album);
        initUI();
    }

    private void initUI(){
        initTextFields();
        albumCoverImage = (ImageView) findViewById(R.id.image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fabSaveButton = (FloatingActionButton) findViewById(R.id.fab_edit_album);
        if(fabSaveButton != null) {
            fabSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEdit();
                }
            });
        }
        updateUI();
    }

    private void initTextFields(){
        artistTxt = (TextView) findViewById(R.id.artist_name_text);
        albumTxt = (TextView) findViewById(R.id.album_name_text);
        genreTxt = (TextView) findViewById(R.id.genre_text);
    }

    private void openEdit(){
        Log.d("fab","open edit presed");
        Intent intentMain = new Intent(this, EditAlbumDataActivity.class);
        intentMain.putExtra("SHOW_SCANNER", false);
        intentMain.putExtra("SHOW_EXISTING", true);
        startActivity(intentMain);
    }
    public void setDisplayedAlbum(Album a){
        displayedAlbum=a;
    }

    private void updateUI() {
        collapsingToolbarLayout.setTitle(displayedAlbum.getArtist() + "-" + displayedAlbum.getTitle());
        artistTxt.setText(displayedAlbum.getArtist());
        albumTxt.setText(displayedAlbum.getTitle());
        genreTxt.setText(displayedAlbum.getGenre());
        albumCoverImage.setImageBitmap(displayedAlbum.getCoverBitmap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                openEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
