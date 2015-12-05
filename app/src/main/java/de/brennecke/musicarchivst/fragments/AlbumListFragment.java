package de.brennecke.musicarchivst.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;
import de.brennecke.musicarchivst.view.AlphabeticList;

/**
 * Created by Alexander on 05.12.2015.
 */
public class AlbumListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String artistName;
    private AlphabeticList alphabeticList;

    ArrayList<String> albumNames;

    private View view;
    private List<Album> albumList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        albumList = getAlbumsFromDB();
        getAlbumNames();
        String[] albums = albumNames.toArray(new String[albumNames.size()]);
        alphabeticList = new AlphabeticList(albums,this);
        view = alphabeticList.createView(inflater,container, getActivity());
        return view;
    }

    private void getAlbumNames(){
        albumNames = new ArrayList<>();
        for(Album a : albumList){
            albumNames.add(a.getTitle());
        }
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

    }
}
