package de.brennecke.musicarchivst.buttonlistener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.view.View;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.EditAlbumDataActivity;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.dialogs.TypeBarcodeDialog;

/**
 * Created by Alexander on 27.10.2015.
 */
public class FABNewAlbumListener implements View.OnClickListener {

    private MainActivity activity;

    public FABNewAlbumListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.scan_fab) {
            Intent intentMain = new Intent(activity, EditAlbumDataActivity.class);
            intentMain.putExtra("SHOW_SCANNER", true);
            activity.startActivity(intentMain);
        } else if (v.getId() == R.id.type_code) {
            FragmentManager manager = activity.getFragmentManager();
            Fragment frag = manager.findFragmentByTag("fragment_edit_name");
            if (frag != null) {
                manager.beginTransaction().remove(frag).commit();
            }
            TypeBarcodeDialog editNameDialog = new TypeBarcodeDialog();
            editNameDialog.show(manager, "fragment_edit_name");

        } else if (v.getId() == R.id.type_information) {

        }
    }
}
