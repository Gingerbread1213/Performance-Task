package org.erhs.stem.project.time_management.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar topBar = findViewById(R.id.top_bar);
        setSupportActionBar(topBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton ibStatistics = findViewById(R.id.action_statistics);
        ibStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StatisticsChartActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ibSettings = findViewById(R.id.action_settings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button btnEnd = findViewById(R.id.action_end);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.started), null);
                editor.commit();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LandingActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ibAdd = findViewById(R.id.action_add);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EventEditingActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
