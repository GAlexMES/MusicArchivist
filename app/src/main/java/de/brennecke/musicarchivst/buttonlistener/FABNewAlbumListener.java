package de.brennecke.musicarchivst.buttonlistener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.EditAlbumDataActivity;
import de.brennecke.musicarchivst.activities.MainActivity;
import de.brennecke.musicarchivst.dialogs.BarcodeDownloadDialog;
import de.brennecke.musicarchivst.dialogs.TypeBarcodeDialog;

/**
 * Created by Alexander on 27.10.2015.
 */
public class FABNewAlbumListener implements View.OnClickListener {

    private MainActivity activity;
    private String scannerPackage = "com.google.zxing.integration.android";

    public FABNewAlbumListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_fab) {
            try {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                intent.setPackage("com.google.zxing.client.android");
                activity.startActivityForResult(intent, 0);
            } catch(ActivityNotFoundException anfe){
                BarcodeDownloadDialog.showDialog(activity);
            }
        } else if (v.getId() == R.id.type_code) {
            FragmentManager manager = activity.getFragmentManager();
            Fragment frag = manager.findFragmentByTag("fragment_edit_name");
            if (frag != null) {
                manager.beginTransaction().remove(frag).commit();
            }
            TypeBarcodeDialog editNameDialog = new TypeBarcodeDialog();
            editNameDialog.show(manager, "fragment_edit_name");

        } else if (v.getId() == R.id.type_information) {
            Intent intentMain = new Intent(activity, EditAlbumDataActivity.class);
            intentMain.putExtra("MODE", EditAlbumDataActivity.MODE.EDIT.ordinal());
            activity.startActivity(intentMain);
        }
    }
}
