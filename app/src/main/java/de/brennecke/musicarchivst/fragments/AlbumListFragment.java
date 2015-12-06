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
import de.brennecke.musicarchivst.view.AlphabeticList;

/**
 * Created by Alexander on 05.12.2015.
 */
public class AlbumListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String artistName;
    private AlphabeticList alphabeticList;

    private String[] sortedAlbums;
    private Map<String,Album> albumMap;
    private View view;
    private List<Album> albumList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        albumList = getAlbumsFromDB();
        sortedAlbums = sortList();
        alphabeticList = new AlphabeticList(sortedAlbums,this);
        view = alphabeticList.createView(inflater,container, getActivity());
        return view;
    }

    private String[] sortList(){
        albumMap = new HashMap<>();
        for(Album a:albumList){
            albumMap .put(a.getTitle(), a);
        }
        List<String> albumNames = new ArrayList<>();
        albumNames.addAll(albumMap .keySet());
        Collections.sort(albumNames);
        return albumNames.toArray(new String[albumNames.size()]);
    }

    private List<Album> getAlbumsFromDB(){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        List<Album> retval = sqLiteSourceAdapter.getAlbums(artistName);
        return retval;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Album selected = albumMap.get(sortedAlbums[position]);
        Exchange.getInstance().setCurrentAlbum(selected);
        Intent intentMain = new Intent(getActivity(), ShowAlbumActivity.class);
        getActivity().startActivity(intentMain);
    }
}
