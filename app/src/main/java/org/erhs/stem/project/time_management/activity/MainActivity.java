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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.domain.EventType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventAdapter eventAdapter;
    private List<Event> events = new ArrayList<>();

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

                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.edit_mode), getString(R.string.mode_add));
                bundle.putString(getString(R.string.event_type), EventType.fromEventType(EventType.STUDY));
                bundle.putString(getString(R.string.description), "This is test");
                bundle.putInt(getString(R.string.planned_start_hour), 8);
                bundle.putInt(getString(R.string.planned_start_minute), 0);
                bundle.putInt(getString(R.string.planned_end_hour), 9);
                bundle.putInt(getString(R.string.planned_end_minute), 0);
                intent.putExtras(bundle);

                intent.setClass(MainActivity.this, EventEditingActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView rvEvent = findViewById(R.id.rv_event);
        rvEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Event e1 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e2= Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e3 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e4 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e5 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e6 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e7 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e8 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());
        Event e9 = Event.createEvent("TEST", EventType.DINE, "", new Date(), new Date());

        e1.description = "HELLO";
        e2.description = "HELLO";
        e3.description = "HELLO";
        e4.description = "HELLO";
        e5.description = "HELLO";
        e6.description = "HELLO";
        e7.description = "HELLO";
        e9.description = "HELLO";
        e1.description = "HELLO";

        events.add(e1);
        events.add(e2);
        events.add(e3);
        events.add(e4);
        events.add(e5);
        events.add(e6);
        events.add(e7);
        events.add(e8);
        events.add(e9);

        eventAdapter = new EventAdapter(getApplicationContext(), events);
        rvEvent.setAdapter(eventAdapter);
        rvEvent.setLongClickable(true);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
