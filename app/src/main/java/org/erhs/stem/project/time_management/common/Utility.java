package org.erhs.stem.project.time_management.common;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.domain.EventType;
import org.erhs.stem.project.time_management.service.Receiver;
import org.erhs.stem.project.time_management.service.ApplicationMonitor;

import java.util.EnumMap;
import java.util.Map;

public abstract class Utility {

    private static int MINUTE_IN_MILLISECONDS = 60000;

    private static final Map<EventType, Integer> EVENT_IMAGES = new EnumMap(EventType.class);
    static {
        EVENT_IMAGES.put(EventType.DINING, R.drawable.ic_restaurant_black_24dp);
        EVENT_IMAGES.put(EventType.STUDY, R.drawable.ic_school_black_24dp);
        EVENT_IMAGES.put(EventType.READ, R.drawable.ic_book);
        EVENT_IMAGES.put(EventType.SPORT, R.drawable.ic_barbell);
        EVENT_IMAGES.put(EventType.MUSIC, R.drawable.ic_music);
        EVENT_IMAGES.put(EventType.HOUSEWORK, R.drawable.ic_house);
        EVENT_IMAGES.put(EventType.NAP, R.drawable.ic_sleep);
        EVENT_IMAGES.put(EventType.PET, R.drawable.ic_pet);
        EVENT_IMAGES.put(EventType.WORK, R.drawable.ic_work);
        EVENT_IMAGES.put(EventType.LEISURE, R.drawable.ic_game_console);
        EVENT_IMAGES.put(EventType.OTHER, R.drawable.ic_other);
        EVENT_IMAGES.put(EventType.SHOPPING, R.drawable.ic_shopping);
    }

    public static String getSessionId(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.started), context.getString(R.string.empty));
    }

    public static int getEventImageId(EventType eventType) {
        return EVENT_IMAGES.get(eventType);
    }

    public static int getRemindBeforePlannedEndInMilliseconds(Context context) {
        try {
            String reminderEntryValue = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(context.getString(R.string.reminder_key), null);
            return Integer.parseInt(reminderEntryValue) * MINUTE_IN_MILLISECONDS;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void alarm(Context context, Event event, long remindTime) {
        if (remindTime < event.actualStart.getTime()) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, Receiver.class);
        alarmIntent.putExtra(context.getString(R.string.receiver_type_serializable), Receiver.Type.REMIND);
        alarmIntent.putExtra(context.getString(R.string.event_serializable), event);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, event.id.hashCode(),
                alarmIntent, PendingIntent.FLAG_ONE_SHOT);

        ApplicationMonitor.getInstance(context).getAlarmRepository().addAlarm(event.id, alarmPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, remindTime, alarmPendingIntent);
        }
    }

    public static void setTheme(Context context, Activity activity) {
        String theme = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.theme_key), context.getString(R.string.theme_default_value));
        if (context.getString(R.string.theme_1).equals(theme)) {
            activity.setTheme(R.style.AppTheme);
        } else if (context.getString(R.string.theme_2).equals(theme)) {
            activity.setTheme(R.style.AppThemeAlt);
        }
        else if (context.getString(R.string.theme_3).equals(theme)) {
            activity.setTheme(R.style.AppThemeFresh);
        }
        else if (context.getString(R.string.theme_4).equals(theme)) {
            activity.setTheme(R.style.AppThemeSky);
        }
        else if (context.getString(R.string.theme_5).equals(theme)) {
            activity.setTheme(R.style.AppThemeWarm);
        }
    }}
