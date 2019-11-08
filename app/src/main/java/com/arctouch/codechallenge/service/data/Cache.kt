package com.arctouch.codechallenge.service.data

import com.arctouch.codechallenge.service.model.Genre

object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        Cache.genres = genres
    }
}
