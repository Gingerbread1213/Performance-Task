package org.erhs.stem.project.time_management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.erhs.stem.project.time_management.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button btnStart = findViewById(R.id.action_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnStatistics = findViewById(R.id.action_statistics);
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LandingActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        Button btnAbout = findViewById(R.id.action_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LandingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
