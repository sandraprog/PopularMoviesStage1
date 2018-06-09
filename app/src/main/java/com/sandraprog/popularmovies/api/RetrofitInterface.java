package com.sandraprog.popularmovies.api;

import com.sandraprog.popularmovies.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sandrapog on 05.06.2018.
 */

public interface RetrofitInterface {
    @GET("movie/popular")
    Call<MoviesList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesList> getTopRatedMovies(@Query("api_key") String apiKey);
}
