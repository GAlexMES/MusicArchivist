package de.brennecke.musicarchivst.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Exchange;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;
import de.brennecke.musicarchivst.view.AlbumPopupMenu;
import de.brennecke.musicarchivst.view.AlphabeticList;

/**
 * Created by Alexander on 05.12.2015.
 */
public class AlbumListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = AlbumListFragment.class.getSimpleName();

    private String artistName;
    private AlphabeticList alphabeticList;

    private String[] sortedAlbums;
    private Map<String, Album> albumMap;
    private View view;
    private List<Album> albumList;

    public AlbumListFragment() {
        albumList = new ArrayList<>();
        albumMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (albumList.size() == 0) {
            albumList = getAlbumsFromDB();
        }
        sortedAlbums = sortList();
        alphabeticList = new AlphabeticList(sortedAlbums, this, this);
        view = alphabeticList.createView(inflater, container, getActivity());
        return view;
    }

    public void setAlbums(List<Album> albums) {
        for (Album a : albums) {
            albumMap.put(a.getTitle(), a);
            albumList.add(a);
        }
    }

    private String[] sortList() {
        for (Album a : albumList) {
            albumMap.put(a.getTitle(), a);
        }
        List<String> albumNames = new ArrayList<>();
        albumNames.addAll(albumMap.keySet());
        Collections.sort(albumNames);
        return albumNames.toArray(new String[albumNames.size()]);
    }

    private List<Album> getAlbumsFromDB() {
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        List<Album> retval = sqLiteSourceAdapter.getAlbums(artistName);
        return retval;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Album selected = albumMap.get(sortedAlbums[position]);
        Exchange.getInstance().setCurrentAlbum(selected);
        Intent intentMain = new Intent(getActivity(), ShowAlbumActivity.class);
        getActivity().startActivity(intentMain);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Album selected = albumMap.get(sortedAlbums[position]);
        AlbumPopupMenu apm = new AlbumPopupMenu(selected,view,getActivity());
        apm.showMenu();
        return true;
    }
}
