package com.myniprojects.towatch.model


import com.myniprojects.towatch.utils.const.IMAGE_BASE_URL
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val id: Int,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val title: String,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
)
{
    val fullPath: String?
        get()
        {
            return when
            {
                backdropPath != null ->
                {
                    "$IMAGE_BASE_URL$backdropPath"
                }
                posterPath != null ->
                {
                    "$IMAGE_BASE_URL$posterPath"
                }
                else ->
                {
                    null
                }
            }
        }

}