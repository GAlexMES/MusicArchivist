package de.brennecke.musicarchivst.buttonlistener;

import android.util.Log;
import android.widget.SearchView;

/**
 * Created by Alexander on 08.11.2015.
 */
public class SearchViewListener implements SearchView.OnQueryTextListener,   SearchView.OnCloseListener {

    @Override
    public boolean onClose() {
        Log.i("searchView","closed");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("searchView",query +" wurde abgesendet");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("searchView",newText+"  wurde eingegeben");
        return false;
    }
}
