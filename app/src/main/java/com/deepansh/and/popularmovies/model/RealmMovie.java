package com.deepansh.and.popularmovies.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class RealmMovie extends RealmObject {

    @SerializedName("adult")
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @Ignore
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("id")
    private int id;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("title")
    private String title;
    @SerializedName("vide")
    private boolean video;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public static RealmMovie from(Context context, Movie movie) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmMovie realmMovie = realm.createObject(RealmMovie.class);
        realmMovie.setAdult(movie.isAdult());
        realmMovie.setBackdropPath(movie.getBackdropPath());
        realmMovie.setGenreIds(movie.getGenreIds());
        realmMovie.setId(movie.getId());
        realmMovie.setOriginalLanguage(movie.getOriginalLanguage());
        realmMovie.setOverview(movie.getOverview());
        realmMovie.setOriginalTitle(movie.getOriginalTitle());
        realmMovie.setPopularity(movie.getPopularity());
        realmMovie.setPosterPath(movie.getPosterPath());
        realmMovie.setReleaseDate(movie.getReleaseDate());
        realmMovie.setVideo(movie.isVideo());
        realmMovie.setVoteAverage(movie.getVoteAverage());
        realmMovie.setVoteCount(movie.getVoteCount());

        realm.commitTransaction();
        realm.close();

        return realmMovie;
    }
}
