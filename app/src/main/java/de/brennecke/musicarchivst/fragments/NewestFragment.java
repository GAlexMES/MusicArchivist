package de.brennecke.musicarchivst.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.buttonlistener.FABNewAlbumListener;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 09.11.2015.
 */
public class NewestFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newest_additions, container, false);
        initFABButtons();
        initCards();
        return view;
    }

    private void initFABButtons() {

        FloatingActionButton scanButton = (FloatingActionButton) view.findViewById(R.id.scan_fab);
        if (scanButton != null) {
            scanButton.setOnClickListener(new FABNewAlbumListener((MainActivity)getActivity()));
        }

        FloatingActionButton typeCodeButton = (FloatingActionButton) view.findViewById(R.id.type_code);
        if (typeCodeButton != null) {
            typeCodeButton.setOnClickListener(new FABNewAlbumListener((MainActivity)getActivity()));
        }
    }

    private void initCards() {
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.album_scroll_view);
        LinearLayout linearLayoutAlbum = (LinearLayout) scrollView.findViewById(R.id.lastAddedAlbumsLayout);
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(getActivity());
        sqLiteSourceAdapter.open();

        List<Album> albumList = sqLiteSourceAdapter.getAllAlbums();

        for (int i = 0; i < albumList.size() && i < 5; i++) {
            linearLayoutAlbum.addView(getAlbumCard(getActivity(), albumList.get(i)));
        }
    }

    private View getAlbumCard(Context context, Album album) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.album_card, null);

        TextView artist_field = (TextView) v.findViewById(R.id.artist_name_text);
        TextView album_field = (TextView) v.findViewById(R.id.album_title_text);
        TextView genre_field = (TextView) v.findViewById(R.id.genre_text);
        ImageView imageView = (ImageView) v.findViewById(R.id.album_cover);

        artist_field.setText(album.getArtist());
        album_field.setText(album.getTitle());
        genre_field.setText((album.getGenre()));
        imageView.setImageBitmap(album.getCoverBitmap());
        return v;
    }
}
