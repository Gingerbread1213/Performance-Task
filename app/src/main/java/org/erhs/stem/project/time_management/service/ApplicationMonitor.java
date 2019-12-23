package org.erhs.stem.project.time_management.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import org.erhs.stem.project.time_management.R;

public class ApplicationMonitor {

    private volatile static ApplicationMonitor INSTANCE;

    private boolean notificationEnabled = true;

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

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.channel_id),
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManagerCompat.from(context).createNotificationChannel(channel);
        }
    }
}
