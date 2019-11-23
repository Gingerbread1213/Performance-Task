package org.erhs.stem.project.time_management.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.erhs.stem.project.time_management.R;

public class MainActivity extends AppCompatActivity {

    private Button btnData;
    private Button btnSettings;
    private TextView textView;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnData = findViewById(R.id.btnData);
        btnSettings = findViewById(R.id.btnSettings);
        textView = findViewById(R.id.txtTitle);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, DataActivity.class);
                //startActivity(intent);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                textView.setText(String.valueOf(pref.getInt("sounds", 0)));

            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
