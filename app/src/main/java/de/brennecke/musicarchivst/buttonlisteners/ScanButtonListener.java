package de.brennecke.musicarchivst.buttonlisteners;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import de.brennecke.musicarchivst.R;

/**
 * Created by Alexander on 25.10.2015.
 */
public class ScanButtonListener implements View.OnClickListener {

    private Activity activity;

    public ScanButtonListener (Activity activity){
        this.activity = activity;
    }

    public void onClick(View v) {

        if (v.getId() == R.id.scan_fab) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
            scanIntegrator.initiateScan();
        }

    }
}
