package com.gaas.erik.simplepopularmovies.data;

import com.gaas.erik.simplepopularmovies.models.Movie;
import com.gaas.erik.simplepopularmovies.models.Review;
import com.gaas.erik.simplepopularmovies.models.Trailer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by erik on 8/16/15.
 */
public interface MovieService {

    @GET("/discover/movie")
    void listMovies(@Query("sort_by") String sort, @Query("api_key") String api_key, Callback<Movie> cb);

    @GET("/movie/{id}/videos")
    void listTrailers(@Path("id") String movieId, @Query("api_key") String api_key, Callback<Trailer> cb);

    @GET("/movie/{id}/reviews")
    void listReview(@Path("id") String movieId, @Query("api_key") String api_key, Callback<Review> cb);


}

