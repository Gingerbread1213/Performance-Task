package org.erhs.stem.project.time_management.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "TEST";
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "HELLO", Toast.LENGTH_LONG).show();
        /**
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Time is up")
                .setContentText("CLICK ME!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
         */
    }
}
