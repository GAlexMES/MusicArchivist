package de.brennecke.musicarchivst.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.buttonlistener.MainActivityButtonListener;
import de.brennecke.musicarchivst.buttonlistener.NavigationDrawerListener;
import de.brennecke.musicarchivst.buttonlistener.SearchViewListener;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;


public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private String[] navDrawerItems = {"Artist","Album","Test"};
    private DrawerLayout drawerLayout;
    private ListView drawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationDrawer();
        initFABButtons();
        initCards(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setupSearchView();
        return true;
    }

    private void initNavigationDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        NavigationDrawerListener ndw = new NavigationDrawerListener(navDrawerItems);
        drawerList.setAdapter(ndw);
        drawerList.setOnItemClickListener(ndw);

    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void setupSearchView() {
        searchView.setIconifiedByDefault(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            // Try to use the "applications" global search provider
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            searchView.setSearchableInfo(info);
        }

        searchView.setOnQueryTextListener(new SearchViewListener());
        searchView.setOnCloseListener(new SearchViewListener());
    }

    private void initFABButtons(){

        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.scan_fab);
        if(scanButton != null) {
            scanButton.setOnClickListener(new MainActivityButtonListener(this));
        }

        FloatingActionButton typeCodeButton = (FloatingActionButton) findViewById(R.id.type_code);
        if(typeCodeButton != null) {
            typeCodeButton.setOnClickListener(new MainActivityButtonListener(this));
        }
    }

    private void initCards(Context context){
        ScrollView scrollView = (ScrollView) findViewById(R.id.album_scroll_view);
        LinearLayout linearLayoutAlbum = (LinearLayout) scrollView.findViewById(R.id.lastAddedAlbumsLayout);
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(context);
        sqLiteSourceAdapter.open();

        List<Album> albumList = sqLiteSourceAdapter.getAllAlbums();

        for(int i = 0; i<albumList.size()&&i<5; i++) {
            linearLayoutAlbum.addView(getAlbumCard(context,albumList.get(i)));
        }
    }

    private View getAlbumCard(Context context, Album album){
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.album_card, null);

        TextView artist_field = (TextView)v.findViewById(R.id.artist_name_text);
        TextView album_field = (TextView)v.findViewById(R.id.album_title_text);
        TextView genre_field = (TextView)v.findViewById(R.id.genre_text);
        ImageView imageView = (ImageView)v.findViewById(R.id.album_cover);

        artist_field.setText(album.getArtist());
        album_field.setText(album.getTitle());
        genre_field.setText((album.getGenre()));
        imageView.setImageBitmap(album.getCoverBitmap());
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
