package com.gaas.erik.simplepopularmovies;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gaas.erik.simplepopularmovies.adapters.MovieAdapter;
import com.gaas.erik.simplepopularmovies.data.MovieContract;
import com.gaas.erik.simplepopularmovies.data.MovieManager;
import com.gaas.erik.simplepopularmovies.models.Movie;
import com.gaas.erik.simplepopularmovies.models.Result;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_OVERVIEW = 1;
    public static final int COL_MOVIE_RELEASE_DATE = 2;
    public static final int COL_MOVIE_POSTER_PATH = 3;
    public static final int COL_MOVIE_TITLE = 4;
    public static final int COL_MOVIE_VOTE_AVERAGE = 5;

    private static final String[] SORT_OPTIONS = {"popularity.desc", "vote_average.desc"};
    public static final String MOVIE_PARCEL = "MovieToDetail";

    @Bind(R.id.poster_imgs)
    GridView mGridview;

    @Bind(R.id.sort_choices)
    Spinner spinner;

    @State
    ArrayList<Result> movieResults;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position <= 1) {
                            populateMovies(SORT_OPTIONS[position]);
                        } else if (position == 2) {
                            //Populate from database
                            getDbMovies();
                            setupGridView(movieResults);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });


        if (movieResults == null) {
            populateMovies(SORT_OPTIONS[0]);

        } else {
            setupGridView(movieResults);
        }



        return view;

    }

    private void populateMovies(String sortOrder) {
        MovieManager.getMovies(sortOrder, new Callback<Movie>() {
            @Override
            public void success(Movie movies, Response response) {
                movieResults = (ArrayList<Result>) movies.getResults();
                setupGridView(movieResults);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("RetroError", error.toString());
                Toast.makeText(getActivity(), "Can't retrieve movies. Consider looking at favorites!", Toast.LENGTH_LONG).show();


            }
        });
    }

    private void setupGridView(final ArrayList<Result> movieResults) {
        mGridview.setAdapter(new MovieAdapter(
                getActivity(),
                R.layout.movie_poster,
                movieResults
        ));

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailFragment detailFragment = new DetailFragment();
                Bundle args = new Bundle();

                if (getActivity().findViewById(R.id.tablet_details) != null) {
                    args.putParcelable(MOVIE_PARCEL, movieResults.get(position));
                    detailFragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.tablet_details, detailFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {

                    args.putParcelable(MOVIE_PARCEL, movieResults.get(position));
                    detailFragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, detailFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

    }

    private void getDbMovies() {
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor movieCursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        Log.v("Cursor problems", Integer.toString(movieCursor.getCount()));
        if (movieCursor.getCount() > 0) {
            ArrayList<Result> dbMovieResultArrayList = new ArrayList<Result>();
            while (movieCursor.moveToNext()) {
                Result dbResult = new Result();
                dbResult.setId(movieCursor.getInt(COL_MOVIE_ID));
                dbResult.setOverview(movieCursor.getString(COL_MOVIE_OVERVIEW));
                dbResult.setReleaseDate(movieCursor.getString(COL_MOVIE_RELEASE_DATE));
                dbResult.setPosterPath(movieCursor.getString(COL_MOVIE_POSTER_PATH));
                dbResult.setTitle(movieCursor.getString(COL_MOVIE_TITLE));
                dbResult.setVoteAverage(movieCursor.getString(COL_MOVIE_VOTE_AVERAGE));
                dbMovieResultArrayList.add(dbResult);
            }
            movieResults = dbMovieResultArrayList;
            movieCursor.close();


        } else {
            Toast.makeText(getActivity(), "You do not have any favorite movies", Toast.LENGTH_LONG).show();
        }




    }
}