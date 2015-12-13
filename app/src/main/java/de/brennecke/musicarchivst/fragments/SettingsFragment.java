package de.brennecke.musicarchivst.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.dialogs.FavoriteAlbumSelectionDialog;

/**
 * Created by Alexander on 12.12.2015.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        view.findViewById(R.id.settings_favorite_album).setOnClickListener(this);

        return view;
    }

    public void showFavoriteAlbumSelection() {
        Log.d(TAG, "show favorite album selection");
        android.app.FragmentManager manager = getActivity().getFragmentManager();
        android.app.Fragment frag = manager.findFragmentByTag("dialog_favorite_album_selection");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        FavoriteAlbumSelectionDialog aboutDialog = new FavoriteAlbumSelectionDialog();
        aboutDialog.show(manager, "dialog_favorite_album_selection");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.settings_favorite_album){
            showFavoriteAlbumSelection();
        }
    }
}
