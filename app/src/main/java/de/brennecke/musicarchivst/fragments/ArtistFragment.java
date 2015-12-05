package de.brennecke.musicarchivst.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;
import de.brennecke.musicarchivst.view.AlphabeticList;

/**
 * Created by Alexander on 10.11.2015.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private AlphabeticList alphabeticList;
    private View view;
    private List<Album> albumList;

    private String[] artistNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getListFromDB();

        alphabeticList = new AlphabeticList(artistNames,this);
        view = alphabeticList.createView(inflater, container, getActivity());
        return view;
    }

    private void getListFromDB(){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        albumList = sqLiteSourceAdapter.getAllArtists();
        artistNames = getSortedArtistNames(albumList);
    }

    private String[] getSortedArtistNames(List<Album> albumList){
        List<String> artistList = new ArrayList<>();
        for (Album a: albumList){
            if(!artistList.contains(a.getArtist())){
                artistList.add(a.getArtist());
            }
        }
        Collections.sort(artistList);
        String[] retval = new String[artistList.size()];
        artistList.toArray(retval);
        return retval;
    }

    private List<Album> getAlbumForArtist(String artistName){
        List<Album> retval = new ArrayList<>();
        for(Album a:albumList){
            if(a.getArtist().equals(artistName)){
                retval.add(a);
            }
        }
        return retval;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedArtist = artistNames[position];
        List<Album> artistAlbums = getAlbumForArtist(selectedArtist);

        Fragment albumFragment = new AlbumListFragment();
        ((AlbumListFragment)albumFragment).setArtistName(selectedArtist);

        ((MainActivity) getActivity()).showFragment(albumFragment);
    }
}
