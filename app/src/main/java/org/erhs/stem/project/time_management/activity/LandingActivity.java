package org.erhs.stem.project.time_management.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.service.ApplicationMonitor;

import java.util.UUID;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume() {
                ApplicationMonitor.getInstance(getApplicationContext()).setNotificationEnabled(false);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void onPause() {
                ApplicationMonitor.getInstance(getApplicationContext()).setNotificationEnabled(true);
            }
        });

        Button btnStart = findViewById(R.id.action_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.started), UUID.randomUUID().toString());
                editor.commit();

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
                intent.setClass(LandingActivity.this, StatisticsChartActivity.class);
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

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getString(getString(R.string.started), null) != null) {
            Intent intent = new Intent();
            intent.setClass(LandingActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
