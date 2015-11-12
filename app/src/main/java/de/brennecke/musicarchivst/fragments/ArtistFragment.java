package de.brennecke.musicarchivst.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 10.11.2015.
 */
public class ArtistFragment extends Fragment {

    private View view;
    private ListView listView;
    private List<Album> artistList;
    private AlbumListAdapter albumListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        listView = (ListView)view.findViewById(R.id.artist_list_view);
        initList();
        return view;
    }

    private void initList(){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        artistList = sqLiteSourceAdapter.getAllArtists();
        sortArtistList();
        albumListAdapter = new AlbumListAdapter(getActivity(),artistList);
        listView.setAdapter(albumListAdapter);
    }

    private void sortArtistList(){
        List<Album> albumList = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();
        for(Album a : artistList){
            artistNames.add(a.getArtist());
        }
        Collections.sort(artistNames);

        for(String s:artistNames){
            for(Album a:artistList){
                if(a.getArtist().equals(s)){
                    albumList.add(a);
                    break;
                }
            }
        }

        artistList = albumList;
    }

    class AlbumListAdapter extends ArrayAdapter<Album> implements SectionIndexer {
        HashMap<String, Integer> alphaIndexer;
        List<String> addedArtist;
        String[] sections;

        public AlbumListAdapter(Context context, List<Album> items) {
            super(context, R.layout.image_tile, items);

            addedArtist = new ArrayList<>();
            alphaIndexer = new HashMap<String, Integer>();
            int size = items.size();

            for (int i = 0; i < size; i++) {
               String artist = items.get(i).getArtist();
                String ch =  artist.substring(0, 1);
                ch = ch.toUpperCase();
                alphaIndexer.put(ch, i);
            }

            Set<String> sectionLetters = alphaIndexer.keySet();

            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

            Collections.sort(sectionList);

            sections = new String[sectionList.size()];

            sectionList.toArray(sections);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.image_tile, null);
            TextView artist_field = (TextView) v.findViewById(R.id.artist_list_artist_name_text);
            artist_field.setText(artistList.get(position).getArtist());
            return v;
        }

        public int getPositionForSection(int section) {
            return alphaIndexer.get(sections[section]);
        }

        public int getSectionForPosition(int position) {
            return 1;
        }

        public Object[] getSections() {
            return sections;
        }
    }

}
