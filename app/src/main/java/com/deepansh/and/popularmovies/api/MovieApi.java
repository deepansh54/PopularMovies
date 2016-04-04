package com.deepansh.and.popularmovies.api;

import com.deepansh.and.popularmovies.model.MovieResult;
import com.deepansh.and.popularmovies.model.ReviewResult;
import com.deepansh.and.popularmovies.model.VideoResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieApi {

    @GET("/discover/movie")
    void discoverMovie(@Query("page") int page, @Query("sort_by") String sortBy, Callback<MovieResult> callback);

    @GET("/movie/{id}/videos")
    void trailers(@Path("id") int movieId, Callback<VideoResult> callback);

    @GET("/movie/{id}/reviews")
    void reviews(@Path("id") int movieId, Callback<ReviewResult> callback);
}
