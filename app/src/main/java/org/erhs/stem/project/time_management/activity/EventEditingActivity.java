package org.erhs.stem.project.time_management.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.EventType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventEditingActivity extends AppCompatActivity {

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mm a");

    private Spinner eventType;

    private EditText description;
    private EditText plannedStart;
    private EditText plannedEnd;

    private Date plannedStartTime;
    private Date plannedEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ArrayAdapter<EventType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventType.values());

        eventType = findViewById(R.id.event_type);
        description = findViewById(R.id.description);
        plannedStart = findViewById(R.id.planned_start);
        plannedEnd = findViewById(R.id.planned_end);

        plannedStartTime = new Date();
        plannedEndTime = new Date();

        eventType.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            eventType.setSelection(adapter.getPosition(
                    EventType.toEventType(bundle.getString(getString(R.string.event_type)))));

            description.setText(bundle.getString(getString(R.string.description)));

            plannedStartTime.setHours(bundle.getInt(getString(R.string.planned_start_hour)));
            plannedStartTime.setMinutes(bundle.getInt(getString(R.string.planned_end_minute)));

            plannedEndTime.setHours(bundle.getInt(getString(R.string.planned_end_hour)));
            plannedEndTime.setMinutes(bundle.getInt(getString(R.string.planned_end_minute)));

            plannedStart.setText(TIME_FORMATTER.format(plannedStartTime));
            plannedEnd.setText(TIME_FORMATTER.format(plannedEndTime));
        }

        plannedStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Date tmpDate = new Date(plannedStartTime.getTime());
                            tmpDate.setHours(hourOfDay);
                            tmpDate.setMinutes(minute);
                            if (tmpDate.getTime() <= plannedEndTime.getTime()) {
                                plannedStartTime.setTime(tmpDate.getTime());
                                plannedStart.setText(TIME_FORMATTER.format(plannedStartTime));
                            }
                            plannedStart.clearFocus();
                        }
                    }, plannedStartTime.getHours(), plannedStartTime.getMinutes(), false);
                    timePickerDialog.setTitle(getString(R.string.set_planned_start_time));
                    timePickerDialog.show();
                }
            }
        });

        plannedEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Date tmpDate = new Date(plannedEndTime.getTime());
                            tmpDate.setHours(hourOfDay);
                            tmpDate.setMinutes(minute);
                            if (tmpDate.getTime() >= plannedStartTime.getTime()) {
                                plannedEndTime.setTime(tmpDate.getTime());
                                plannedEnd.setText(TIME_FORMATTER.format(plannedEndTime));
                            }
                            plannedEnd.clearFocus();
                        }
                    }, plannedEndTime.getHours(), plannedEndTime.getMinutes(), false);
                    timePickerDialog.setTitle(getString(R.string.set_planned_end_time));
                    timePickerDialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                return true;
            case R.id.action_edit_done:
                Bundle origin = getIntent().getExtras();
                if (origin != null) {
                    bundle.putString(getString(R.string.event_id),
                            origin.getString(getString(R.string.event_id), ""));
                }
                bundle.putString(getString(R.string.event_type),
                        EventType.fromEventType((EventType) eventType.getSelectedItem()));
                bundle.putString(getString(R.string.description),
                        description.getText().toString());
                bundle.putInt(getString(R.string.planned_start_hour),
                        plannedStartTime.getHours());
                bundle.putInt(getString(R.string.planned_start_minute),
                        plannedStartTime.getMinutes());
                bundle.putInt(getString(R.string.planned_end_hour),
                        plannedEndTime.getHours());
                bundle.putInt(getString(R.string.planned_end_minute),
                        plannedEndTime.getMinutes());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
