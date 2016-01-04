package de.brennecke.musicarchivst.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.buttonlistener.FABNewAlbumListener;
import de.brennecke.musicarchivst.buttonlistener.SearchViewListener;
import de.brennecke.musicarchivst.dialogs.AboutDialog;
import de.brennecke.musicarchivst.fragments.AlbumListFragment;
import de.brennecke.musicarchivst.fragments.ArtistFragment;
import de.brennecke.musicarchivst.fragments.NewestFragment;
import de.brennecke.musicarchivst.fragments.SettingsFragment;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;


public class MainActivity extends AppCompatActivity implements View.OnLayoutChangeListener{

    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private MenuItem currentSelectedItem;

    private String MINE_TEST_DEVICE_ID = "55EB28184A0F147FB6A8E2FF0DCC64A9";

    private boolean replaceNavDrawerHeader = true;
    private boolean initAds = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFABButtons();
        initNavigationDrawer(getApplicationContext());
        drawerToggle = setupDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
        MenuItem initItem = navigationDrawer.getMenu().getItem(0);
        selectDrawerItem(initItem);
        initAd();
        findViewById(android.R.id.content).addOnLayoutChangeListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
                Class fragmentClass = SettingsFragment.class;
                setFragment(fragmentClass);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(replaceNavDrawerHeader){
            SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(this);
            sqLiteSourceAdapter.open();
            Bitmap bm = sqLiteSourceAdapter.getFavoriteAlbumCover();
            replaceNavDrawerHeader = !setImageToNavigationDrawer(bm);
        }

        if(initAds){
            initAds=!initAd();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    public boolean setImageToNavigationDrawer(Bitmap bdm) {
        LinearLayout header = (LinearLayout) findViewById(R.id.navigation_drawer_header);
        if (header != null) {
            if (bdm != null) {
                Drawable backgroundImage = new BitmapDrawable(bdm);
                header.setBackground(backgroundImage);
            } else {
                header.setBackgroundResource(0);
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            return true;
        }
        return false;
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
        boolean changeTitle = true;

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
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_about:
                showAboutDialog();
                changeTitle = false;
                break;
            default:
                fragmentClass = NewestFragment.class;
        }

        if (fragmentClass != null) {
            setFragment(fragmentClass);
        }

        if (changeTitle) {
            setTitle(menuItem.getTitle());
            menuItem.setCheckable(true);
            menuItem.setChecked(true);
            menuItem.setChecked(true);
            if (currentSelectedItem != null) {
                currentSelectedItem.setChecked(false);
            }
            currentSelectedItem = menuItem;
        }
        drawerLayout.closeDrawers();
    }

    private void showAlbumList() {
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(this);
        sqLiteSourceAdapter.open();
        List<Album> albumList = sqLiteSourceAdapter.getAllAlbums();
        Fragment albumFragment = new AlbumListFragment();
        ((AlbumListFragment) albumFragment).setAlbums(albumList);
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

    private void initFABButtons() {
        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.scan_fab);
        if (scanButton != null) {
            scanButton.setOnClickListener(new FABNewAlbumListener(this));
        }

        FloatingActionButton typeCodeButton = (FloatingActionButton) findViewById(R.id.type_code);
        if (typeCodeButton != null) {
            typeCodeButton.setOnClickListener(new FABNewAlbumListener(this));
        }

        FloatingActionButton typeInformationButton = (FloatingActionButton) findViewById(R.id.type_information);
        if (typeInformationButton != null) {
            typeInformationButton.setOnClickListener(new FABNewAlbumListener(this));
        }
    }

    private boolean initAd(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if(mAdView==null){
            return false;
        }
        else {
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(MINE_TEST_DEVICE_ID)
                    .build();
            mAdView.loadAd(request);
            return true;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Intent intentMain = new Intent(this, EditAlbumDataActivity.class);
                intentMain.putExtra("BARCODE", contents);
                intentMain.putExtra("MODE", EditAlbumDataActivity.MODE.EDIT);
                startActivity(intentMain);
            } else {
                Toast toast = Toast.makeText(this, "Could not scan correctly!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

}