package org.erhs.stem.project.time_management.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import org.erhs.stem.project.time_management.common.Config;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.domain.EventType;
import org.erhs.stem.project.time_management.receiver.AlarmReceiver;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, PendingIntent> alarmPendingIntents = new HashMap<>();

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
            Bundle bundle = resultIntent.getExtras();
            if (bundle != null) {
                String eventId = bundle.getString(getString(R.string.event_id));
                EventType eventType = EventType.toEventType(
                        bundle.getString(getString(R.string.event_type), Config.EMPTY));
                String description = bundle.getString(getString(R.string.description), Config.EMPTY);
                int plannedStartHour = bundle.getInt(getString(R.string.planned_start_hour), 0);
                int plannedStartMinute = bundle.getInt(getString(R.string.planned_start_minute), 0);
                int plannedEndHour = bundle.getInt(getString(R.string.planned_end_hour), 0);
                int plannedEndMinute = bundle.getInt(getString(R.string.planned_end_minute), 0);

                if (requestCode == REQUEST_CODE_ADD) {
                    Date plannedStart = new Date();
                    plannedStart.setHours(plannedStartHour);
                    plannedStart.setMinutes(plannedStartMinute);
                    plannedStart.setSeconds(0);

                    Date plannedEnd = new Date();
                    plannedEnd.setHours(plannedEndHour);
                    plannedEnd.setMinutes(plannedEndMinute);
                    plannedEnd.setSeconds(0);

                    Event event = Event.createEvent(Utility.getSessionId(getApplicationContext()),
                            eventType, description, plannedStart, plannedEnd);

                    int insertPos;
                    for (insertPos = 0; insertPos < events.size(); insertPos++) {
                        if (events.get(insertPos).plannedStart.getTime() > plannedStart.getTime()) {
                            break;
                        }
                    }
                    events.add(insertPos, event);
                    EventRepository.insertEvent(getApplicationContext(), event);
                    eventAdapter.notifyItemInserted(insertPos);
                    rvEvent.smoothScrollToPosition(insertPos);
                } else if (requestCode == REQUEST_CODE_MODIFY) {
                    int fromPos = -1;
                    for (int i = 0; i < events.size(); i++) {
                        if (eventId.equals(events.get(i).id)) {
                            fromPos = i;
                            break;
                        }
                    }
                    if (fromPos != -1) {
                        Event event = events.get(fromPos);
                        event.type = eventType;
                        event.description = description;
                        event.plannedStart.setHours(plannedStartHour);
                        event.plannedStart.setMinutes(plannedStartMinute);
                        event.plannedStart.setSeconds(0);
                        event.plannedEnd.setHours(plannedEndHour);
                        event.plannedEnd.setMinutes(plannedEndMinute);
                        event.plannedEnd.setSeconds(0);
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
                    }
                }
            }
        }
    }

    private void startEventEditingActivity(Event event, int requestCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                bundle.putString(getString(R.string.edit_mode), getString(R.string.mode_add));
                break;
            case REQUEST_CODE_MODIFY:
                bundle.putString(getString(R.string.edit_mode), getString(R.string.mode_modify));
                break;
            default:
                break;
        }
        bundle.putString(getString(R.string.event_id), event.id);
        bundle.putString(getString(R.string.event_type), EventType.fromEventType(event.type));
        bundle.putString(getString(R.string.description), event.description);
        bundle.putInt(getString(R.string.planned_start_hour), event.plannedStart.getHours());
        bundle.putInt(getString(R.string.planned_start_minute), event.plannedStart.getMinutes());
        bundle.putInt(getString(R.string.planned_end_hour), event.plannedEnd.getHours());
        bundle.putInt(getString(R.string.planned_end_minute), event.plannedEnd.getMinutes());
        intent.putExtras(bundle);
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
                cancelAlarmAndNotification(event);
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
                    cancelAlarmAndNotification(event);
                    return;
                }

                long remindTime = event.plannedEnd.getTime() - Config.REMIND_BEFORE_PLANNED_END_IN_MILLISECONDS;

                // Event is in progress, however, it is too late to remind, do nothing
                if (remindTime < event.actualStart.getTime()) return;

                AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                        .getSystemService(Context.ALARM_SERVICE);

                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.session_id), Utility.getSessionId(getApplicationContext()));
                bundle.putString(getString(R.string.event_id), event.id);
                bundle.putString(getString(R.string.event_type), EventType.fromEventType(event.type));
                bundle.putString(getString(R.string.description), event.description);
                alarmIntent.putExtras(bundle);

                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), (int) System.currentTimeMillis(), alarmIntent,
                        PendingIntent.FLAG_ONE_SHOT);

                alarmPendingIntents.put(event.id, alarmPendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
                }
            }
        };
    }

    private void cancelAlarmAndNotification(Event event) {
        if (alarmPendingIntents.containsKey(event.id)) {
            alarmPendingIntents.get(event.id).cancel();
            alarmPendingIntents.remove(event.id);
        }
        NotificationManagerCompat.from(getApplicationContext()).cancel(event.id.hashCode());
    }
}
