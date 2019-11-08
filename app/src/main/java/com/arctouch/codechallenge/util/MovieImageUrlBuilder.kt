package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.service.api.ApiClient.Companion.API_KEY

private const val POSTER_URL = "https://image.tmdb.org/t/p/w154"
private const val BACKDROP_URL = "https://image.tmdb.org/t/p/w780"

class MovieImageUrlBuilder {

    fun buildPosterUrl(posterPath: String) = "$POSTER_URL$posterPath?api_key=$API_KEY"

    fun buildBackdropUrl(backdropPath: String) = "$BACKDROP_URL$backdropPath?api_key=$API_KEY"
}
