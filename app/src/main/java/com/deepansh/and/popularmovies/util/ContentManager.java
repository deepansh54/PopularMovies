package com.deepansh.and.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ContentManager {

    private static final String CONTENT_PREFS = "content_preferences";
    private static final String PREF_SORT_ORDER = "pref_sort";
    private static ContentManager sInstance;
    private final Context context;
    private String sortOrder;

    public ContentManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ContentManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ContentManager(context);
        }
        return sInstance;
    }

    public String getSortOrder() {
        if (sortOrder == null) {
            SharedPreferences contentPrefs = context.getSharedPreferences(CONTENT_PREFS, Context.MODE_PRIVATE);
            sortOrder = contentPrefs.getString(PREF_SORT_ORDER, Constants.SORT_BY_POPULARITY);
        }
        return sortOrder;
    }

    public void setSortOrder(String order) {
        sortOrder = order;
        context.getSharedPreferences(CONTENT_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_SORT_ORDER, order).apply();
    }
}
