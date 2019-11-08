package com.arctouch.codechallenge.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.service.data.Cache
import com.arctouch.codechallenge.service.model.GenreResponse
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.service.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.service.repository.MovieRepository
import io.reactivex.observers.DisposableSingleObserver

class MovieDetailViewModel : ViewModel() {

    private val movieRepository = MovieRepository()
    val onDetailResult = MutableLiveData<Movie>()
    val onError = MutableLiveData<String>()

    fun getMovieDetail(id: Int) {
        movieRepository.getMovieDetail(id.toLong())
                .subscribe(object : DisposableSingleObserver<Movie>() {
                    override fun onSuccess(response: Movie) {
                        onDetailResult.postValue(response)
                    }

                    override fun onError(e: Throwable) {
                        onError.postValue(e.message)
                    }
                })
    }
}