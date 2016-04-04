package com.deepansh.and.popularmovies.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deepansh.and.popularmovies.R;
import com.deepansh.and.popularmovies.model.Movie;
import com.deepansh.and.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private List<Movie> movies;
    private MovieGridFragment.Callbacks itemCallback;

    public MovieAdapter(List<Movie> movieList) {
        movies = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false), itemCallback);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.updateData(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void setCallbacks(MovieGridFragment.Callbacks callbacks) {
        this.itemCallback = callbacks;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cover_iv)
        ImageView coverIv;
        private Movie movie;

        public MovieViewHolder(View itemView, final MovieGridFragment.Callbacks itemCallback) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCallback.onItemSelected(movie);
                }
            });
        }

        public void updateData(Movie movie) {
            Picasso.with(itemView.getContext())
                    .load(Constants.STATIC_IMAGE_ENDPOINT + Constants.LIST_IMAGE_SIZE + movie.getPosterPath())
                    .fit()
                    .centerCrop()
                    .into(coverIv);
            this.movie = movie;
        }
    }
}
