package org.erhs.stem.project.time_management.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import org.erhs.stem.project.time_management.R;

import java.util.HashMap;
import java.util.Map;

public class ApplicationMonitor {

    private volatile static ApplicationMonitor INSTANCE;

    private boolean notificationEnabled = true;
    private AlarmRepository alarmRepository = new AlarmRepository();

    private ApplicationMonitor(Context context) {
        createNotificationChannel(context);
    }

    public static ApplicationMonitor getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ApplicationMonitor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationMonitor(context);
                }
            }
        }
        return INSTANCE;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public AlarmRepository getAlarmRepository() {
        return alarmRepository;
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.channel_id),
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManagerCompat.from(context).createNotificationChannel(channel);
        }
    }

    public static class AlarmRepository {

        private Map<String, PendingIntent> alarmPendingIntents = new HashMap<>();

        public void addAlarm(String eventId, PendingIntent alarmPendingIntent) {
            alarmPendingIntents.put(eventId, alarmPendingIntent);
        }

        public void cancel(String eventId) {
            if (alarmPendingIntents.containsKey(eventId)) {
                alarmPendingIntents.get(eventId).cancel();
                alarmPendingIntents.remove(eventId);
            }
        }

        public void cancelAll() {
            for (PendingIntent alarmPendingIntent : alarmPendingIntents.values()) {
                alarmPendingIntent.cancel();
            }
            alarmPendingIntents.clear();
        }
    }
}
