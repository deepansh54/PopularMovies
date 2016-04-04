package com.deepansh.and.popularmovies.util;

import android.content.Context;
import android.content.res.Resources;

import com.deepansh.and.popularmovies.PopularMovies;

public abstract class UIUtils {

    public static Resources getResources() {
        return getAppContext().getResources();
    }

    public static Context getAppContext() {
        return PopularMovies.getInstance().getApplicationContext();
    }
}
