package com.myniprojects.towatch.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbResponse(
    val page: Int,
    @Json(name = "results")
    val movies: List<Movie>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)