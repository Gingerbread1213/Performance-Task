package org.erhs.stem.project.time_management.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.activity.MainActivity;
import org.erhs.stem.project.time_management.common.Config;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;

import java.util.Date;

public class Receiver extends BroadcastReceiver {

    public enum Type {
        REMIND,
        SNOOZE,
        ACTION
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Type type = (Type) intent.getSerializableExtra(context.getString(R.string.receiver_type_serializable));
        Event event = (Event) intent.getSerializableExtra(context.getString(R.string.event_serializable));

        // If event's session id is not matched with current session, do not send notification
        if (!Utility.getSessionId(context).equals(event.sessionId)) return;

        switch (type) {
            case REMIND:
                if (ApplicationMonitor.getInstance(context).isNotificationEnabled()) {
                    int notificationId = event.id.hashCode();

                    Intent notifyIntent = new Intent(context, MainActivity.class);
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(context,
                            (int) System.currentTimeMillis(), notifyIntent, PendingIntent.FLAG_ONE_SHOT);

                    Intent snoozeIntent = new Intent(context, Receiver.class);
                    snoozeIntent.putExtra(context.getString(R.string.receiver_type_serializable), Type.SNOOZE);
                    snoozeIntent.putExtra(context.getString(R.string.event_serializable), event);
                    PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context,
                            (int) System.currentTimeMillis(), snoozeIntent, PendingIntent.FLAG_ONE_SHOT);

                    Intent actionIntent = new Intent(context, Receiver.class);
                    actionIntent.putExtra(context.getString(R.string.receiver_type_serializable), Type.ACTION);
                    actionIntent.putExtra(context.getString(R.string.event_serializable), event);
                    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
                            (int) System.currentTimeMillis(), actionIntent, PendingIntent.FLAG_ONE_SHOT);

                    NotificationCompat.Builder builder = new NotificationCompat
                            .Builder(context, context.getString(R.string.channel_id))
                            .setSmallIcon(R.drawable.logo_icon)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), Utility.getEventImageId(event.type)))
                            .setContentTitle("Time is up")
                            .setContentText(event.description)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(notifyPendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .addAction(0, context.getString(R.string.snooze), snoozePendingIntent)
                            .addAction(0, context.getString(R.string.action), actionPendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat.from(context).notify(notificationId, builder.build());
                } else {
                    // Pop up a dialog
                }
                break;
            case SNOOZE:
                NotificationManagerCompat.from(context).cancel(event.id.hashCode());
                Utility.alarm(context, event, System.currentTimeMillis() + Config.SNOOZE_IN_MILLISECONDS);
                break;
            case ACTION:
                NotificationManagerCompat.from(context).cancel(event.id.hashCode());
                event.actualEnd = new Date(System.currentTimeMillis());
                EventRepository.updateEvent(context, event);
                break;
            default:
                break;
        }
    }
}
