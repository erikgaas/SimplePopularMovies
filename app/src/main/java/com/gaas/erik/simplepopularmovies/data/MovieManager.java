package com.gaas.erik.simplepopularmovies.data;

import com.gaas.erik.simplepopularmovies.models.Movie;
import com.gaas.erik.simplepopularmovies.models.Review;
import com.gaas.erik.simplepopularmovies.models.Trailer;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by erik on 8/16/15.
 */
public class MovieManager {

    public static final String API_KEY = "YOUR API KEY HERE";

    public static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.themoviedb.org/3")
            .build();

    public static MovieService movieService = restAdapter.create(MovieService.class);

    public static void getMovies(String sortBy, Callback<Movie> cb) {
        movieService.listMovies(sortBy, API_KEY, cb);
    }

    public static void getTrailers(String movieId, Callback<Trailer> cb) {
        movieService.listTrailers(movieId, API_KEY, cb);
    }

    public static void getReviews(String movieId, Callback<Review> cb) {
        movieService.listReview(movieId, API_KEY, cb);
    }
}
