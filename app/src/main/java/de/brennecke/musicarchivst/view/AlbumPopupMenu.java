package de.brennecke.musicarchivst.view;

import android.app.Activity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.model.Album;
import de.brennecke.musicarchivst.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 12.12.2015.
 */
public class AlbumPopupMenu {

    private static final String TAG = AlbumPopupMenu.class.getSimpleName();

    private Album album;
    private Activity activity;
    private View selectedView;

    public AlbumPopupMenu(Album album, View selectedView, Activity activity){
        this.album=album;
        this.activity=activity;
        this.selectedView=selectedView;
    }

    public void showMenu(){
        IconizedMenu popup = new IconizedMenu(activity, selectedView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.album_popup_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mark_album_as_favorite: markAsFavorite(); break;
                    case R.id.delete_album: deleteAlbum(); break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void deleteAlbum(){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(activity);
        sqLiteSourceAdapter.open();
        sqLiteSourceAdapter.deleteAlbum(album);
        Log.d(TAG, album.getTitle() + " shouled be deleted");
    }

    private void markAsFavorite(){
        Log.d(TAG, album.getTitle() + " should be marked as favorite");
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(activity);
        sqLiteSourceAdapter.open();
        sqLiteSourceAdapter.setFavoriteAlbum(album);
        ((MainActivity)activity).setImageToNavigationDrawer(album.getCoverBitmap());
    }
}
