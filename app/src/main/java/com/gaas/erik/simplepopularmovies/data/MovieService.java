package com.gaas.erik.simplepopularmovies.data;

import com.gaas.erik.simplepopularmovies.models.Movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by erik on 8/16/15.
 */
public interface MovieService {

    @GET("/discover/movie")
    void listMovies(@Query("sort_by") String sort, @Query("api_key") String api_key, Callback<Movie> cb);
}
