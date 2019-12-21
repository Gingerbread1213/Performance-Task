package org.erhs.stem.project.time_management.common;

import android.content.Context;

import androidx.preference.PreferenceManager;

import org.erhs.stem.project.time_management.R;

public abstract class Utility {

    public static String getSessionId(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.started), Config.EMPTY);
    }
}
