package com.deepansh.and.popularmovies;

import android.app.Application;

public class PopularMovies extends Application {

    private static PopularMovies sInstance;

    public static PopularMovies getInstance() {
        return sInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
