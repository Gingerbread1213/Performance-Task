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
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.service.ApplicationMonitor;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    interface OnEditCallback {
        void onEdit(Event event);
    }

    interface OnDeleteCallback {
        void onDelete(Event event);
    }

    interface OnProcessCallback {
        void onProcess(Event event);
    }

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_MODIFY = 2;

    private EventAdapter eventAdapter;
    private List<Event> events = new ArrayList<>();

    private RecyclerView rvEvent;

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
                ApplicationMonitor.getInstance(getApplicationContext()).getAlarmRepository().cancelAll();
                NotificationManagerCompat.from(getApplicationContext()).cancelAll();

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
                startEventEditingActivity(Event.createDefaultEvent(
                        Utility.getSessionId(getApplicationContext())), REQUEST_CODE_ADD);
            }
        });

        rvEvent = findViewById(R.id.rv_event);
        rvEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        eventAdapter = new EventAdapter(getApplicationContext().getResources(), events,
                createOnEditCallback(), createOnDeleteCallback(), createOnProcessCallback());
        rvEvent.setAdapter(eventAdapter);

        EventRepository.getEventsBySessionId(getApplicationContext(), Utility.getSessionId(getApplicationContext()))
                .observe(this, new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {
                        MainActivity.this.events.clear();
                        MainActivity.this.events.addAll(events);
                        eventAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (resultCode == RESULT_OK) {
            Event event = (Event) resultIntent.getSerializableExtra(getString(R.string.event_serializable));
            if (requestCode == REQUEST_CODE_ADD) {
                int insertPos;
                for (insertPos = 0; insertPos < events.size(); insertPos++) {
                    if (events.get(insertPos).plannedStart.getTime() > event.plannedStart.getTime()) {
                        break;
                    }
                }
                events.add(insertPos, event);
                EventRepository.insertEvent(getApplicationContext(), event);
                eventAdapter.notifyItemInserted(insertPos);
                rvEvent.smoothScrollToPosition(insertPos);
            } else if (requestCode == REQUEST_CODE_MODIFY) {
                boolean plannedEndModified = false;
                int fromPos = -1;
                for (int i = 0; i < events.size(); i++) {
                    if (event.id.equals(events.get(i).id)) {
                        fromPos = i;
                        break;
                    }
                }
                if (fromPos != -1) {
                    plannedEndModified = events.get(fromPos).plannedEnd.getTime() != event.plannedEnd.getTime();
                    events.set(fromPos, event);
                    EventRepository.updateEvent(getApplicationContext(), event);
                    eventAdapter.notifyItemChanged(fromPos);
                    int toPos;
                    for (toPos = 0; toPos < events.size(); toPos++) {
                        if (events.get(toPos).plannedStart.getTime() > event.plannedStart.getTime()) {
                            break;
                        }
                    }
                    toPos = toPos > fromPos ? toPos - 1 : toPos;
                    Collections.swap(events, fromPos, toPos);
                    eventAdapter.notifyItemMoved(fromPos, toPos);
                    rvEvent.smoothScrollToPosition(toPos);

                    if (event.actualStart != null && event.actualEnd == null && plannedEndModified) {
                        ApplicationMonitor.getInstance(getApplicationContext()).getAlarmRepository().cancel(event.id);
                        NotificationManagerCompat.from(getApplicationContext()).cancel(event.id.hashCode());

                        int remindBeforePlannedEndInMilliseconds = Utility
                                .getRemindBeforePlannedEndInMilliseconds(getApplicationContext());
                        if (remindBeforePlannedEndInMilliseconds >= 0) {
                            Utility.alarm(getApplicationContext(), event, event.plannedEnd.getTime() - remindBeforePlannedEndInMilliseconds);
                        }
                    }
                }
            }
        }
    }

    private void startEventEditingActivity(Event event, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.event_serializable), event);
        intent.setClass(MainActivity.this, EventEditingActivity.class);
        startActivityForResult(intent, requestCode);
    }

    private OnEditCallback createOnEditCallback() {
        return new OnEditCallback() {
            @Override
            public void onEdit(Event event) {
                startEventEditingActivity(event, REQUEST_CODE_MODIFY);
            }
        };
    }

    private OnDeleteCallback createOnDeleteCallback() {
        return new OnDeleteCallback() {
            @Override
            public void onDelete(Event event) {
                ApplicationMonitor.getInstance(getApplicationContext()).getAlarmRepository().cancel(event.id);
                NotificationManagerCompat.from(getApplicationContext()).cancel(event.id.hashCode());
            }
        };
    }

    private OnProcessCallback createOnProcessCallback() {
        return new OnProcessCallback() {
            @Override
            public void onProcess(Event event) {
                // Event is not started, do nothing
                if (event.actualStart == null) return;

                // Event is done, cancel alarm and notification
                if (event.actualEnd != null) {
                    ApplicationMonitor.getInstance(getApplicationContext()).getAlarmRepository().cancel(event.id);
                    NotificationManagerCompat.from(getApplicationContext()).cancel(event.id.hashCode());
                    return;
                }

                int remindBeforePlannedEndInMilliseconds = Utility
                        .getRemindBeforePlannedEndInMilliseconds(getApplicationContext());
                if (remindBeforePlannedEndInMilliseconds >= 0) {
                    Utility.alarm(getApplicationContext(), event, event.plannedEnd.getTime());
                }
            }
        };
    }
}
