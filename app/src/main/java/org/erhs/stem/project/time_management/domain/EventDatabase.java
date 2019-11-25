package org.erhs.stem.project.time_management.domain;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "EventDatabase";

    private volatile static EventDatabase INSTANCE;

    public static EventDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (EventDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, EventDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract EventDao eventDao();
}
