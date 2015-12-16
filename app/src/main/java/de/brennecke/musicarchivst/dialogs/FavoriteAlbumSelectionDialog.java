package de.brennecke.musicarchivst.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;
import de.brennecke.musicarchivst.view.GridViewAdapter;

/**
 * Created by Alexander on 12.12.2015.
 */
public class FavoriteAlbumSelectionDialog  extends DialogFragment implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private List<Album> albumList;

    private static final String TAG = FavoriteAlbumSelectionDialog.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_favorite_album_selection, container);

        gridView = (GridView) view.findViewById(R.id.gridView);
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();
        albumList = sqLiteSourceAdapter.getAllAlbums();

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.favorite_album_grid_item, (ArrayList)albumList);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        getDialog().setTitle(R.string.settings_favorite_album);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Album a = (Album) parent.getItemAtPosition(position);
        ((MainActivity)getActivity()).setImageToNavigationDrawer(a.getCoverBitmap());
        Log.d(TAG,a.getTitle()+" selected");
        this.dismiss();
    }
}
