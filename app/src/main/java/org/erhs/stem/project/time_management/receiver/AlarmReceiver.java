package org.erhs.stem.project.time_management.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.activity.MainActivity;
import org.erhs.stem.project.time_management.common.Utility;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "TEST";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String sessionId = bundle.getString(context.getString(R.string.session_id));
        String eventId = bundle.getString(context.getString(R.string.event_id));
        String description = bundle.getString(context.getString(R.string.description));

        // If event's session id is not matched with current session, do not send notification
        if (!Utility.getSessionId(context).equals(sessionId)) return;

        int notificationId = eventId.hashCode();

        Intent notifyIntent = new Intent(context, MainActivity.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context,
                (int) System.currentTimeMillis(), notifyIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_icon)
                .setContentTitle("Time is up")
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
    }

    private PendingIntent createSnoozePendingIntent(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
        //snoozeIntent.setAction(ACTION_SNOOZE);
        //snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        60 * 1000, snoozePendingIntent);

        return snoozePendingIntent;
    }
}
