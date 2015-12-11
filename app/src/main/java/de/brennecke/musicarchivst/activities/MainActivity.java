package de.brennecke.musicarchivst.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.buttonlistener.SearchViewListener;
import de.brennecke.musicarchivst.dialogs.AboutDialog;
import de.brennecke.musicarchivst.fragments.AlbumListFragment;
import de.brennecke.musicarchivst.fragments.ArtistFragment;
import de.brennecke.musicarchivst.fragments.NewestFragment;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;


public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationDrawer(getApplicationContext());
        drawerToggle = setupDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        MenuItem initItem = navigationDrawer.getMenu().getItem(0);
        selectDrawerItem(initItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setupSearchView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void initNavigationDrawer(Context context) {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        navigationDrawer = (NavigationView) findViewById(R.id.navigationView);
        navigationDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        navigationDrawer.setCheckedItem(R.id.nav_newest);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_newest:
                fragmentClass = NewestFragment.class;
                break;
            case R.id.nav_artist:
                fragmentClass = ArtistFragment.class;
                break;
            case R.id.nav_albums:
                showAlbumList();
                break;
            case R.id.nav_settings:
                fragmentClass = NewestFragment.class;
                break;
            case R.id.nav_about:
                showAboutDialog();
                break;
            default:
                fragmentClass = NewestFragment.class;
        }

        if (fragmentClass != null) {
            setFragment(fragmentClass);
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private void showAlbumList() {
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(this);
        sqLiteSourceAdapter.open();
        List<Album> albumList = sqLiteSourceAdapter.getAllAlbums();
        Fragment albumFragment = new AlbumListFragment();
        ((AlbumListFragment)albumFragment).setAlbums(albumList);
        showFragment(albumFragment);
    }

    private void showAboutDialog() {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.Fragment frag = manager.findFragmentByTag("dialog_about");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(manager, "fragment_about_name");

    }

    public void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showFragment(fragment);
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_drawer_content, fragment).commit();
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

        searchView.setOnQueryTextListener(new SearchViewListener(this));
        searchView.setOnCloseListener(new SearchViewListener(this));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Intent intentMain = new Intent(this, EditAlbumDataActivity.class);
                intentMain.putExtra("BARCODE", contents);
                startActivity(intentMain);
            }
            else{
                Toast toast = Toast.makeText(this, "Could not scan correctly!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

}