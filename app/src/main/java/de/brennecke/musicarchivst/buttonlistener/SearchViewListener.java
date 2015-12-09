package de.brennecke.musicarchivst.buttonlistener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SearchView;

import java.util.List;

import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.fragments.AlbumListFragment;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 08.11.2015.
 */
public class SearchViewListener implements SearchView.OnQueryTextListener,   SearchView.OnCloseListener {

    private Activity activity;
    public SearchViewListener(Activity activity){
        this.activity=activity;
    }

    @Override
    public boolean onClose() {
        Log.i("searchView","closed");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(activity);
        sqLiteSourceAdapter.open();
        List<Album> searchResults =  sqLiteSourceAdapter.getSearchResult(query);
        Fragment albumListFragmnt = new AlbumListFragment();
        ((AlbumListFragment)albumListFragmnt).setAlbums(searchResults);
        ((MainActivity)activity).showFragment(albumListFragmnt);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("searchView",newText+"  wurde eingegeben");
        return false;
    }
}
