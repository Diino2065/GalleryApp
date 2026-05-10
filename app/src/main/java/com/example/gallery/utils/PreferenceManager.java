package com.example.gallery.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREFS_NAME = "gallery_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_GRID_MODE = "grid_mode";

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isGridMode() {
        return prefs.getBoolean(KEY_GRID_MODE, true);
    }

    public void setGridMode(boolean gridMode) {
        prefs.edit().putBoolean(KEY_GRID_MODE, gridMode).apply();
    }
}