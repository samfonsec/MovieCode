package com.arctouch.codechallenge.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.service.data.Cache
import com.arctouch.codechallenge.service.model.GenreResponse
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.service.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.service.repository.MovieRepository
import io.reactivex.observers.DisposableSingleObserver

class HomeViewModel : ViewModel() {

    private val movieRepository = MovieRepository()
    val onMovieUpcomingResult = MutableLiveData<ArrayList<Movie>>()
    val onError = MutableLiveData<String>()

    fun getGenres(page: Int) {
        movieRepository.getGenres()
                .subscribe(object : DisposableSingleObserver<GenreResponse>() {
                    override fun onSuccess(response: GenreResponse) {
                        Cache.cacheGenres(response.genres)
                        getMoviesUpcoming(page)
                    }

                    override fun onError(e: Throwable) {
                        onError.postValue(e.message)
                    }
                })
    }

    fun getMoviesUpcoming(page: Int) {
        movieRepository.getUpcomingMovies(page.toLong())
                .subscribe(object : DisposableSingleObserver<UpcomingMoviesResponse>() {
                    override fun onSuccess(response: UpcomingMoviesResponse) {
                        response.results.map { movie ->
                            movie.copy(genres = Cache.genres.filter {
                                movie.genreIds?.contains(it.id) == true
                            })
                        }.let {
                            val arrayList: ArrayList<Movie> = arrayListOf()
                            it.forEach { mv -> arrayList.add(mv) }
                            onMovieUpcomingResult.postValue(arrayList)
                        }
                    }

                    override fun onError(e: Throwable) {
                        onError.postValue(e.message)
                    }
                })
    }
}