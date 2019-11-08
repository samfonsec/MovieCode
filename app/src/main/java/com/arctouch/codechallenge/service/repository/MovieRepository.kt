package com.arctouch.codechallenge.service.repository

import com.arctouch.codechallenge.service.api.ApiClient
import com.arctouch.codechallenge.service.model.GenreResponse
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.service.model.UpcomingMoviesResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieRepository : ApiClient() {

    /**
     * Call API to get movies genres
     *
     * @return Single of GenreResponse
     */
    fun getGenres(): Single<GenreResponse> {
        return Single.create { emitter ->
            apiService.genres(API_KEY, DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<GenreResponse>() {
                        override fun onSuccess(response: GenreResponse) {
                            emitter.onSuccess(response)
                        }

                        override fun onError(e: Throwable) {
                            emitter.onError(e)
                        }
                    })

        }
    }

    /**
     * Call API to get the upcoming movies
     *
     * @return Single of UpcomingMoviesResponse
     */
    fun getUpcomingMovies(page: Long): Single<UpcomingMoviesResponse> {
        return Single.create { emitter ->
            apiService.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<UpcomingMoviesResponse>() {
                        override fun onSuccess(response: UpcomingMoviesResponse) {
                            emitter.onSuccess(response)
                        }

                        override fun onError(e: Throwable) {
                            emitter.onError(e)
                        }
                    })

        }
    }

    /**
     * Call API to get a movie detail
     *
     * @return Single of Movie
     */
    fun getMovieDetail(id: Long): Single<Movie> {
        return Single.create { emitter ->
            apiService.movie(id, API_KEY, DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Movie>() {
                        override fun onSuccess(response: Movie) {
                            emitter.onSuccess(response)
                        }

                        override fun onError(e: Throwable) {
                            emitter.onError(e)
                        }
                    })

        }
    }
}