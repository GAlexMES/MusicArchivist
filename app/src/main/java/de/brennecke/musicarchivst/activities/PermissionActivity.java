package de.brennecke.musicarchivst.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander on 11.03.2017.
 */

public class PermissionActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        Toast.makeText(this, "Sorry, but we need that permission.", Toast.LENGTH_SHORT);
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startApplication();
                    return;
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
            }
        }
    }

    private void checkPermission(){
        String neededPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int permissionGranted = ContextCompat.checkSelfPermission(this, neededPermission);

        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{neededPermission}, MY_PERMISSIONS_REQUEST);
        } else {
            startApplication();
        }
    }

    private void startApplication() {
        Log.i("Permissions", "Has all permissions. Start MainActivity!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
