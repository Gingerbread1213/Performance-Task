package org.erhs.stem.project.time_management.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.service.ApplicationMonitor;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.util.List;

public class SettingsActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.setTheme(getApplicationContext(), this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        ApplicationMonitor.getInstance(getApplicationContext()).getActivityRepository().add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        ApplicationMonitor.getInstance(getApplicationContext()).getActivityRepository().remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.reminder_key).equals(key)) {
            ApplicationMonitor.getInstance(getApplicationContext()).getAlarmRepository().cancelAll();
            final int remindBeforePlannedEndInMilliseconds = Utility.getRemindBeforePlannedEndInMilliseconds(getApplicationContext());
            if (remindBeforePlannedEndInMilliseconds >= 0) {
                EventRepository.getEventsBySessionId(getApplicationContext(), Utility.getSessionId(getApplicationContext()))
                        .observe(this, new Observer<List<Event>>() {
                            @Override
                            public void onChanged(List<Event> events) {
                                for (Event event : events) {
                                    if (event.actualStart != null && event.actualEnd == null) {
                                        Utility.alarm(getApplicationContext(), event, event.plannedEnd.getTime() - remindBeforePlannedEndInMilliseconds);
                                    }
                                }
                            }
                        });
            }
        } else if (getString(R.string.theme_key).equals(key)) {
            ApplicationMonitor.getInstance(getApplicationContext())
                    .getActivityRepository()
                    .recreate();
        }
    }
}
