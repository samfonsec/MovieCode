package com.arctouch.codechallenge.service.api

import com.arctouch.codechallenge.service.model.GenreResponse
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.service.model.UpcomingMoviesResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        /**
         * Api url to get the genre movie list
         */
        const val API_GET_GENRE_MOVIE_LIST = "genre/movie/list"

        /**
         * Api url to get the a movie upcoming
         */
        const val API_GET_MOVIE_UPCOMING = "movie/upcoming"

        /**
         * Api url to get a movie by its id
         */
        const val API_GET_MOVIE_BY_ID = "movie/{id}"
    }

    @GET(API_GET_GENRE_MOVIE_LIST)
    fun genres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Single<GenreResponse>

    @GET(API_GET_MOVIE_UPCOMING)
    fun upcomingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Long,
            @Query("region") region: String
    ): Single<UpcomingMoviesResponse>

    @GET(API_GET_MOVIE_BY_ID)
    fun movie(
            @Path("id") id: Long,
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Single<Movie>
}
