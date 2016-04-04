package com.deepansh.and.popularmovies.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getName().equals("io.realm.Realm")
                || clazz.getName().equals("io.realm.internal.Row");
    }
}
