package org.erhs.stem.project.time_management.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "events")
@TypeConverters({EventType.class, Event.DateConverter.class})
public class Event {

    @PrimaryKey
    @NonNull
    public String id;

    public EventType type;
    public String typeDetail;

    public String description;

    public Date plannedStart;
    public Date plannedEnd;

    public Date actualStart;
    public Date actualEnd;

    public Event() {
        id = UUID.randomUUID().toString();
    }

    public static Event createEvent(EventType type, String typeDetail,
                                    Date plannedStart, Date plannedEnd) {
        Event event = new Event();
        event.type = type;
        event.typeDetail = typeDetail;
        event.plannedStart = plannedStart;
        event.plannedEnd = plannedEnd;
        return event;
    }

    public static class DateConverter {

        @TypeConverter
        public static Date toDate(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long fromDate(Date date) {
            return date == null ? null : date.getTime();
        }
    }
}
