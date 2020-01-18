package org.erhs.stem.project.time_management.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.common.Config;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private ImageView eventType;

    private Button snooze;
    private Button action;

    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        final Event event = (Event) getIntent().getSerializableExtra(getString(R.string.event_serializable));

        eventType = findViewById(R.id.event_type);

        snooze = findViewById(R.id.snooze);
        action = findViewById(R.id.action);

        eventType.setImageDrawable(getResources().getDrawable(Utility.getEventImageId(event.type)));

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.alarm(getApplicationContext(), event, System.currentTimeMillis() + Config.SNOOZE_IN_MILLISECONDS);
                finish();
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.actualEnd = new Date(System.currentTimeMillis());
                EventRepository.updateEvent(getApplicationContext(), event);
                finish();
            }
        });

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        ringtone.play();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (ringtone.isPlaying()) {
            ringtone.stop();
        }
        super.onPause();
    }
}
