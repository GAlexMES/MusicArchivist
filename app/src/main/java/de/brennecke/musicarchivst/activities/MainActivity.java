package de.brennecke.musicarchivst.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.getbase.floatingactionbutton.FloatingActionButton;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.buttonlistener.MainActivityButtonListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFABButtons();
    }

    private void initFABButtons(){

        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.scan_fab);
        if(scanButton != null) {
            scanButton.setOnClickListener(new MainActivityButtonListener(this));
        }

        FloatingActionButton typeCodeButton = (FloatingActionButton) findViewById(R.id.type_code);
        if(typeCodeButton != null) {
            typeCodeButton.setOnClickListener(new MainActivityButtonListener(this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
