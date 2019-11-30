package org.erhs.stem.project.time_management.service;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.domain.EventDao;
import org.erhs.stem.project.time_management.domain.EventDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventRepository {

    public static LiveData<List<Event>> getEventsBySessionId(final Context context,
                                                             final String sessionId) {
        return getEventDao(context).getEventsBySessionId(sessionId);
    }

    public static LiveData<List<Event>> getEventsByDateRange(final Context context,
                                                             final Date start, final Date end) {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(start);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);

        return getEventDao(context).getEventsByDateRange(calStart.getTimeInMillis(),
                calEnd.getTimeInMillis());
    }

    public static void insertEvent(final Context context, final Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getEventDao(context).insertEvent(event);
                return null;
            }
        }.execute();
    }

    public static void deleteEvent(final Context context, final Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getEventDao(context).deleteEvent(event);
                return null;
            }
        }.execute();
    }

    public static void updateEvent(final Context context, final Event event) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getEventDao(context).updateEvent(event);
                return null;
            }
        }.execute();
    }

    private static EventDao getEventDao(Context context) {
        return EventDatabase.getInstance(context).eventDao();
    }
}
