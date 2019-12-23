package org.erhs.stem.project.time_management.common;

import android.content.Context;

import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.EventType;

import java.util.EnumMap;
import java.util.Map;

public abstract class Utility {

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
}
