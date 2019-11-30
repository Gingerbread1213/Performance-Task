package org.erhs.stem.project.time_management.activity;

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

public class EventEditingActivity extends AppCompatActivity {

    private Spinner eventType;
    private EditText description;
    private TimePicker plannedStart;
    private TimePicker plannedEnd;

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

        eventType.setAdapter(adapter);

        plannedStart.setIs24HourView(false);
        plannedEnd.setIs24HourView(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            eventType.setSelection(adapter.getPosition(
                    EventType.toEventType(bundle.getString(getString(R.string.event_type)))));

            description.setText(bundle.getString(getString(R.string.description)));

            plannedStart.setCurrentHour(bundle.getInt(getString(R.string.planned_start_hour)));
            plannedStart.setCurrentMinute(bundle.getInt(getString(R.string.planned_end_minute)));

            plannedEnd.setCurrentHour(bundle.getInt(getString(R.string.planned_end_hour)));
            plannedEnd.setCurrentMinute(bundle.getInt(getString(R.string.planned_end_minute)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit_done:
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    if (getString(R.string.mode_add).equals(
                            bundle.getString(getString(R.string.edit_mode)))) {
                        //TODO
                    } else {
                        //TODO
                    }
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
