package de.brennecke.musicarchivst.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.buttonlistener.FABNewAlbumListener;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.model.Exchange;
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
        initAd();
        initCards();
        return view;
    }

    private void initAd(){
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.d("id",deviceId);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(deviceId)
                .build();
        mAdView.loadAd(request);
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
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
            Album album =  albumList.get(albumList.size()-1-i);
            linearLayoutAlbum.addView(getAlbumCard(getActivity(),album));
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

        AlbumCardListener acl =  new AlbumCardListener(album);
        v.setOnClickListener(acl);
        return v;
    }

    private class AlbumCardListener implements View.OnClickListener{

        Album album;
        public AlbumCardListener(Album album){
            this.album = album;
        }

        @Override
        public void onClick(View v) {
            Exchange.getInstance().setCurrentAlbum(album);
            Intent intentMain = new Intent(getActivity(), ShowAlbumActivity.class);
            getActivity().startActivity(intentMain);
        }
    }
}
