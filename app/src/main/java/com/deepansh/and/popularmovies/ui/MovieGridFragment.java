package com.deepansh.and.popularmovies.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.deepansh.and.popularmovies.R;
import com.deepansh.and.popularmovies.model.Movie;
import com.deepansh.and.popularmovies.model.RealmMovie;
import com.deepansh.and.popularmovies.model.MovieResult;
import com.deepansh.and.popularmovies.api.ApiHelper;
import com.deepansh.and.popularmovies.util.Constants;
import com.deepansh.and.popularmovies.util.ContentManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends Fragment {

    private static final String LIST_STATE = "list_state";
    private static final String DATA = "data";
    @Bind(R.id.movie_grid_rv)
    RecyclerView movieGridRv;
    private int currentPage = 0;

    List<Movie> movies = new ArrayList<>();

    private View.OnClickListener retryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
        }
    };
    private MovieAdapter moviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            if (ContentManager.getInstance(getActivity()).getSortOrder().equals(Constants.SORT_BY_FAV)) {
                reloadData();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        movieGridRv.setLayoutManager(gridLayoutManager);
        movieGridRv.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                MovieGridFragment.this.currentPage = currentPage;
                loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
            }
        });
        moviesAdapter = new MovieAdapter(movies);
        movieGridRv.setAdapter(moviesAdapter);
        moviesAdapter.setCallbacks((MovieGridActivity) getActivity());
        reloadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Realm.getInstance(getActivity()).addChangeListener(realmChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Realm.getInstance(getActivity()).removeAllChangeListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE, listState);
        outState.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) movies);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String currentSort = ContentManager.getInstance(getActivity()).getSortOrder();
        switch (currentSort) {
            case Constants.SORT_BY_POPULARITY:
                menu.findItem(R.id.action_sort_popularity).setChecked(true);
                break;
            case Constants.SORT_BY_HIGHEST_RATED:
                menu.findItem(R.id.action_sort_rating).setChecked(true);
                break;
            case Constants.SORT_BY_FAV:
                menu.findItem(R.id.action_sort_fav).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String currentSort = ContentManager.getInstance(getActivity()).getSortOrder();
        String selectedSort = null;
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                selectedSort = Constants.SORT_BY_POPULARITY;
                break;
            case R.id.action_sort_rating:
                selectedSort = Constants.SORT_BY_HIGHEST_RATED;
                break;
            case R.id.action_sort_fav:
                selectedSort = Constants.SORT_BY_FAV;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (!currentSort.equals(selectedSort)) {
            ContentManager.getInstance(getActivity()).setSortOrder(selectedSort);
            reloadData();
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void reloadData() {
        movies.clear();
        if (ContentManager.getInstance(getActivity()).getSortOrder().equals(Constants.SORT_BY_FAV)) {
            loadFavMovies();
        } else {
            currentPage = 1;
            loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
        }
    }

    private void loadFavMovies() {
        for (RealmMovie realmMovie : Realm.getInstance(getActivity()).where(RealmMovie.class).findAll()) {
            movies.add(Movie.from(realmMovie));
        }
        moviesAdapter.notifyDataSetChanged();
    }


    private void loadMovies(int page, String sortBy) {
        ApiHelper.getApi().discoverMovie(page, sortBy, new Callback<MovieResult>() {
            @Override
            public void success(MovieResult movieResult, Response response) {
                movies.addAll(movieResult.getResults());
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(getView(), getString(R.string.movies_load_error_text), Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW).setAction(getString(R.string.retry), retryClickListener).show();
            }
        });
    }

    public interface Callbacks {
        void onItemSelected(Movie movie);
    }
}
