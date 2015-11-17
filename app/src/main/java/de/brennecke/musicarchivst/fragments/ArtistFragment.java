package de.brennecke.musicarchivst.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 10.11.2015.
 */
public class ArtistFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView listView;
    private List<Album> albumList;

    private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections;
    private String[] artistNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        initList();

        listView = (ListView) view.findViewById(R.id.list_artists);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, artistNames));

        displayIndex();
        return view;
    }

    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(sections.keySet());
        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_row, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }


    private void initList(){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        albumList = sqLiteSourceAdapter.getAllArtists();
        artistNames = getSortedArtistNames(albumList);
        getIndexList(artistNames);
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

    private void getIndexList(String[] artists) {
        sections = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < artists.length; i++) {
            String artistName = artists[i];
            String index = artistName.substring(0, 1);

            if (sections.get(index) == null)
                sections.put(index, i);
        }
    }

    @Override
    public void onClick(View v) {
        TextView selectedIndex = (TextView) v;
        listView.setSelection(sections.get(selectedIndex.getText()));
    }
}
