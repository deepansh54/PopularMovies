package com.deepansh.and.popularmovies.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepansh.and.popularmovies.R;
import com.deepansh.and.popularmovies.model.Movie;
import com.deepansh.and.popularmovies.model.RealmMovie;
import com.deepansh.and.popularmovies.model.Video;
import com.deepansh.and.popularmovies.model.ReviewResult;
import com.deepansh.and.popularmovies.model.VideoResult;
import com.deepansh.and.popularmovies.api.ApiHelper;
import com.deepansh.and.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailFragment extends Fragment {
    public static final String ARG_MOVIE = "movie";

    @Bind(R.id.cover_iv)
    ImageView coverIv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.date_tv)
    TextView releaseDateTv;
    @Bind(R.id.rating_tv)
    TextView ratingTv;
    @Bind(R.id.overview_tv)
    TextView overviewTv;
    @Bind(R.id.trailer_rv)
    RecyclerView trailersRv;
    @Bind(R.id.reviews_rv)
    RecyclerView reviewsRv;
    @Bind(R.id.reviews_cv)
    CardView reviewsCv;
    @Bind(R.id.trailers_cv)
    CardView trailersCv;

    private Movie movie;
    private Palette colorPalette;
    private Handler handler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private TrailerAdapter trailersAdapter;
    private ReviewAdapter reviewsAdapter;
    private List<Video> trailers;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().containsKey(ARG_MOVIE)) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity())
                .load(Constants.STATIC_IMAGE_ENDPOINT + Constants.DETAILS_IMAGE_SIZE + movie.getPosterPath())
                .fit()
                .centerCrop()
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Palette.Builder builder = new Palette.Builder(source);
                        colorPalette = builder.generate();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                titleTv.setTextColor(colorPalette.getDarkVibrantColor(Color.BLACK));
                            }
                        });
                        return source;
                    }

                    @Override
                    public String key() {
                        return "palette";
                    }
                })
                .into(coverIv);


        titleTv.setText(movie.getOriginalTitle());

        Date releaseDate = movie.getReleaseDate();
        releaseDateTv.setText(dateFormat.format(releaseDate));

        SpannableStringBuilder ratingBuilder = new SpannableStringBuilder("Rating:");
        ratingBuilder.append(' ').append(String.valueOf(movie.getVoteAverage())).append("/10 (").append(String.valueOf(movie.getVoteCount())).append(')');
        ratingBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ratingTv.setText(ratingBuilder);

        if (movie.getOverview() != null) {
            SpannableStringBuilder overviewBuilder = new SpannableStringBuilder("Overview:\n\n");
            overviewBuilder.append(movie.getOverview());
            overviewBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            overviewTv.setText(overviewBuilder);
        } else {
            overviewTv.setText(R.string.empty);
        }

        trailersRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trailersAdapter = new TrailerAdapter();
        trailersRv.setAdapter(trailersAdapter);

        reviewsRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        reviewsAdapter = new ReviewAdapter();
        reviewsRv.setAdapter(reviewsAdapter);
        loadDetails();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem favItem = menu.findItem(R.id.action_fav);
        if (favItem != null) {
            favItem.setIcon(isFav(movie) ? R.drawable.ic_fav_full : R.drawable.ic_fav_empty);
            favItem.setTitle(isFav(movie) ? R.string.action_rm_fav : R.string.action_add_fav);
        }

        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareItem.setVisible(trailers != null && trailers.size() > 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                onFavClicked(movie);
                return true;
            case R.id.action_share:
                onShareClicked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onShareClicked() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_trailer_title_text));
        share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailers.get(0).getKey());

        startActivity(Intent.createChooser(share, getString(R.string.share_trailer_title)));
    }

    private void onFavClicked(Movie movie) {
        if (isFav(movie)) {
            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();
            realm.where(RealmMovie.class).equalTo("id", movie.getId()).findFirst().removeFromRealm();
            realm.commitTransaction();
            realm.close();
        } else {
            RealmMovie.from(getActivity(), movie);
        }
        getActivity().supportInvalidateOptionsMenu();
    }

    private boolean isFav(Movie movie) {
        return Realm.getInstance(getActivity()).where(RealmMovie.class).equalTo("id", movie.getId()).findFirst() != null;
    }

    private void loadDetails() {
        loadTrailers();
        loadReviews();
    }

    private void loadTrailers() {
        ApiHelper.getApi().trailers(movie.getId(), new Callback<VideoResult>() {
            @Override
            public void success(VideoResult videoResult, Response response) {
                if (videoResult.getResults().size() > 0) {
                    trailersCv.setVisibility(View.VISIBLE);
                    trailers = videoResult.getResults();
                    getActivity().supportInvalidateOptionsMenu();
                }
                trailersAdapter.setVideos(videoResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() == null) {
                    return;
                }
                Snackbar.make(getView(), getString(R.string.load_trailers_error_message), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadReviews() {
        ApiHelper.getApi().reviews(movie.getId(), new Callback<ReviewResult>() {
            @Override
            public void success(ReviewResult reviewResult, Response response) {
                if (reviewResult.getResults().size() > 0) {
                    reviewsCv.setVisibility(View.VISIBLE);
                }
                reviewsAdapter.setReviews(reviewResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() == null) {
                    return;
                }
                Snackbar.make(getView(), getString(R.string.load_trailers_error_message), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
