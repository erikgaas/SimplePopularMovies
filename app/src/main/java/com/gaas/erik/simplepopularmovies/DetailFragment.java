package com.gaas.erik.simplepopularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaas.erik.simplepopularmovies.data.MovieContract;
import com.gaas.erik.simplepopularmovies.data.MovieManager;
import com.gaas.erik.simplepopularmovies.models.Result;
import com.gaas.erik.simplepopularmovies.models.Review;
import com.gaas.erik.simplepopularmovies.models.ReviewResult;
import com.gaas.erik.simplepopularmovies.models.Trailer;
import com.gaas.erik.simplepopularmovies.models.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by erik on 8/16/15.
 */
public class DetailFragment extends Fragment {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_OVERVIEW = 1;
    public static final int COL_MOVIE_RELEASE_DATE = 2;
    public static final int COL_MOVIE_POSTER_PATH = 3;
    public static final int COL_MOVIE_TITLE = 4;
    public static final int COL_MOVIE_VOTE_AVERAGE = 5;

    public static final int COL_TRAILER_ID = 0;
    public static final int COL_TRAILER_KEY = 1;
    public static final int COL_TRAILER_NAME = 2;
    public static final int COL_TRAILER_FOREIGN = 3;

    public static final int COL_REVIEW_ID = 0;
    public static final int COL_REVIEW_CONTENT = 1;
    public static final int COL_REVIEW_FOREIGN = 2;

    @Bind(R.id.movie_title)
    TextView movieTitle;

    @Bind(R.id.movie_poster_img)
    ImageView moviePoster;

    @Bind(R.id.vote_average)
    TextView voteAverage;

    @Bind(R.id.movie_release_date)
    TextView movieDate;

    @Bind(R.id.movie_plot)
    TextView moviePlot;

    @Bind(R.id.movie_trailers)
    LinearLayout movieTrailersLayout;

    @Bind(R.id.movie_reviews)
    LinearLayout movieReviewsLayout;

    @Bind(R.id.info_layout)
    LinearLayout infoLayout;

    @State
    Result movieResult;

    @State
    Trailer trailers;

    @State
    Review reviews;

    @State
    Boolean isFavorite;

    public DetailFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_share, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (trailers.getTrailerResults().size() > 0) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailers.getTrailerResults().get(0).getKey());
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                    return true;
                } else {
                    Toast.makeText(getActivity(), "No trailers to share", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Icepick.restoreInstanceState(this, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);



        if (movieResult == null) {
            Bundle args = this.getArguments();
            movieResult = args.getParcelable(MainActivityFragment.MOVIE_PARCEL);

            tryDatabaseForContent();

            if (isFavorite.equals(Boolean.FALSE)) {

                MovieManager.getTrailers(Integer.toString(movieResult.getId()), new Callback<Trailer>() {
                    @Override
                    public void success(Trailer trailer, Response response) {
                        trailers = trailer;
                        populateTrailers(trailers);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v("Trailer Error", error.toString());
                        Toast.makeText(getActivity(), "Can't get trailers. Sorry about that...", Toast.LENGTH_LONG).show();
                    }
                });
                MovieManager.getReviews(Integer.toString(movieResult.getId()), new Callback<Review>() {
                    @Override
                    public void success(Review review, Response response) {
                        reviews = review;
                        populateReviews(reviews);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v("Review Error", error.toString());
                        Toast.makeText(getActivity(), "Couldn't get Reviews. Sorry about that...", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                populateTrailers(trailers);
                populateReviews(reviews);
            }



        } else {
            Log.v("Are we here?", "populates stuff");
            populateTrailers(trailers);
            populateReviews(reviews);
        }


        movieTitle.setText(movieResult.getTitle());
        Picasso.with(getActivity()).load(BASE_IMAGE_URL + movieResult.getPosterPath()).into(moviePoster);
        voteAverage.setText(movieResult.getVoteAverage());
        movieDate.setText(movieResult.getReleaseDate());

        //Add favorite button
        final Button dynamicFavorite = new Button(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (isFavorite) {
            dynamicFavorite.setText("Remove from favorites");
        } else {
            dynamicFavorite.setText("Add to favorites");
        }

        dynamicFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    deleteCurrentMovieInfoDb(dynamicFavorite);

                } else {
                    addCurrentMovieInfoToDb(dynamicFavorite);
                }

            }
        });

        infoLayout.addView(dynamicFavorite, lp);


        moviePlot.setText(movieResult.getOverview());

        return view;
    }

    private void populateTrailers(Trailer trailers) {
        for (final TrailerResult trailerResult : trailers.getTrailerResults()) {
            TextView tv = new TextView(getActivity());
            tv.setText(trailerResult.getName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Here we need to provide an intent to view the trailers in youtube.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerResult.getKey())));
                }
            });
            movieTrailersLayout.addView(tv);
        }
    }

    private void populateReviews(Review reviews) {
        for (final ReviewResult reviewResult : reviews.getResults()) {
            TextView tv = new TextView(getActivity());
            tv.setText(reviewResult.getContent());
            movieReviewsLayout.addView(tv);
        }
    }

    private void addCurrentMovieInfoToDb(Button dynamicFavorite) {
        ContentResolver resolver = getActivity().getContentResolver();

        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, Integer.toString(movieResult.getId()));
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movieResult.getOverview());
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieResult.getReleaseDate());
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movieResult.getPosterPath());
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieResult.getTitle());
        movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieResult.getVoteAverage());
        resolver.insert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);

        for (TrailerResult trailer : trailers.getTrailerResults()) {
            ContentValues trailerContentValues = new ContentValues();
            trailerContentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailer.getId());
            trailerContentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, trailer.getKey());
            trailerContentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, trailer.getName());
            trailerContentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_FOREIGN, Integer.toString(movieResult.getId()));
            resolver.insert(MovieContract.TrailerEntry.CONTENT_URI, trailerContentValues);
        }

        for (ReviewResult review : reviews.getResults()) {
            ContentValues reviewContentValues = new ContentValues();
            reviewContentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
            reviewContentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getContent());
            reviewContentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_FOREIGN, Integer.toString(movieResult.getId()));
            resolver.insert(MovieContract.ReviewEntry.CONTENT_URI, reviewContentValues);
        }
        isFavorite = Boolean.TRUE;
        dynamicFavorite.setText("Remove from favorites");

    }

    private void deleteCurrentMovieInfoDb(Button dynamicFavorite) {
        ContentResolver resolver = getActivity().getContentResolver();
        resolver.delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Integer.toString(movieResult.getId())});


        resolver.delete(MovieContract.TrailerEntry.CONTENT_URI,
                MovieContract.TrailerEntry.COLUMN_TRAILER_FOREIGN + " = ?",
                new String[]{Integer.toString(movieResult.getId())});


        resolver.delete(MovieContract.ReviewEntry.CONTENT_URI,
                MovieContract.ReviewEntry.COLUMN_REVIEW_FOREIGN + " = ?",
                new String[]{Integer.toString(movieResult.getId())});

        dynamicFavorite.setText("Add to Favorites");
        isFavorite = Boolean.FALSE;
    }

    private void tryDatabaseForContent() {
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor movieCursor = resolver.query(MovieContract.MovieEntry.buildMovieId(Integer.toString(movieResult.getId())), null, null, null, null);
        isFavorite = movieCursor.getCount() == 1;
        if (isFavorite) {
            Cursor trailerCursor = resolver.query(MovieContract.TrailerEntry.buildTrailerId(Integer.toString(movieResult.getId())), null, null, null, null);
            Cursor reviewCursor = resolver.query(MovieContract.ReviewEntry.buildReviewId(Integer.toString(movieResult.getId())), null, null, null, null);

            Trailer dbActualTrailer = new Trailer();
            ArrayList<TrailerResult> dbTrailerArrayList = new ArrayList<TrailerResult>();
            while (trailerCursor.moveToNext()) {
                TrailerResult dbTrailer = new TrailerResult();
                dbTrailer.setId(trailerCursor.getString(COL_TRAILER_ID));
                dbTrailer.setKey(trailerCursor.getString(COL_TRAILER_KEY));
                dbTrailer.setName(trailerCursor.getString(COL_TRAILER_NAME));
                dbTrailerArrayList.add(dbTrailer);
            }
            dbActualTrailer.setResults(dbTrailerArrayList);

            Review dbReview = new Review();
            ArrayList<ReviewResult> dbReviewResultArrayList = new ArrayList<ReviewResult>();
            while (reviewCursor.moveToNext()) {
                ReviewResult dbReviewResult = new ReviewResult();
                dbReviewResult.setId(reviewCursor.getString(COL_REVIEW_ID));
                dbReviewResult.setContent(reviewCursor.getString(COL_REVIEW_CONTENT));
                dbReviewResultArrayList.add(dbReviewResult);
            }
            dbReview.setResults(dbReviewResultArrayList);


            trailers = dbActualTrailer;
            reviews = dbReview;
            trailerCursor.close();
            reviewCursor.close();



        }
        movieCursor.close();






    }



}