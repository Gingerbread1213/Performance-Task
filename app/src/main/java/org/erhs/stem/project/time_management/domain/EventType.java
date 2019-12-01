package org.erhs.stem.project.time_management.domain;

import androidx.room.TypeConverter;

public enum EventType {

    DINING("dining"),
    STUDY("study"),
    READ("read"),
    WORK("work"),
    SPORT("sport"),
    LEISURE("leisure"),
    NAP("nap"),
    MUSIC("music"),
    HOUSEWORK("housework"),
    PET("pet"),
    SHOPPING("shopping"),
    OTHER("other");


    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @TypeConverter
    public static EventType toEventType(String value) {
        for (EventType eventType : EventType.values()) {
            if (eventType.value.equalsIgnoreCase(value)) {
                return eventType;
            }
        }
        return null;
    }

    @TypeConverter
    public static String fromEventType(EventType eventType) {
        return eventType == null ? null : eventType.value;
    }
}
