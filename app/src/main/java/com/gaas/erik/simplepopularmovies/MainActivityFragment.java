package com.gaas.erik.simplepopularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import com.gaas.erik.simplepopularmovies.adapters.MovieAdapter;
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

    private static final String[] SORT_OPTIONS = {"popularity.desc", "vote_average.desc"};

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        if (movieResults == null) {
            populateMovies(SORT_OPTIONS[0]);
        } else {
            mGridview.setAdapter(new MovieAdapter(
                    getActivity(),
                    R.layout.movie_poster,
                    movieResults
            ));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateMovies(SORT_OPTIONS[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;

    }

    private void populateMovies(String sortOrder) {
        MovieManager.getMovies(sortOrder, new Callback<Movie>() {
            @Override
            public void success(Movie movies, Response response) {
                movieResults = (ArrayList<Result>) movies.getResults();
                mGridview.setAdapter(new MovieAdapter(
                        getActivity(),
                        R.layout.movie_poster,
                        movieResults
                ));
                Log.v("uugghhh", movies.getResults().get(0).getPosterPath());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("RetroError", error.toString());

            }
        });
    }
}