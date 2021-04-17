package com.myniprojects.towatch.network.tmdb

import com.myniprojects.towatch.utils.const.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService
{
    /**
     * Get movies by title: https://developers.themoviedb.org/3/search/search-movies
     */
    @GET("search/movie?language=en-US&api_key=${API_KEY}")
    suspend fun getMovieFromTitle(
        @Query("query") title: String,
    ): TmdbResponse

    /**
     * Get trending movies in current week: https://developers.themoviedb.org/3/trending/get-trending
     */
    @GET("trending/movie/week?api_key=${API_KEY}")
    suspend fun getTrendingMovies(): TmdbResponse
}