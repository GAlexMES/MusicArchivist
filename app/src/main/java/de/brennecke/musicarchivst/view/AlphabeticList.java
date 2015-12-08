package de.brennecke.musicarchivst.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import de.brennecke.musicarchivst.R;

/**
 * Created by Alexander on 05.12.2015.
 */
public class AlphabeticList implements View.OnClickListener {

    private ListView listView;
    private View view;
    private AdapterView.OnItemClickListener onItemClickListener;
    private String[] items;

    private HashMap<String, Integer> sections;

    public AlphabeticList(String[] items, AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
        this.items = items;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Activity activity){
        view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_artists);
        listView.setAdapter(new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, items));
        listView.setOnItemClickListener(onItemClickListener);
        getIndexList();
        displayIndex(activity);
        return view;
    }

    private void displayIndex( Activity activity) {
        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(sections.keySet());
        for (String index : indexList) {
            textView = (TextView) activity.getLayoutInflater().inflate(R.layout.item_row, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    private void getIndexList() {
        sections = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.length; i++) {
            String artistName = items[i];
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
