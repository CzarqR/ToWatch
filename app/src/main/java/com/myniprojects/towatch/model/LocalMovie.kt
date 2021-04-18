package com.myniprojects.towatch.model

import android.os.Parcelable
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_BACKDROP_PATH_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_IS_WATCHED_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_OVERVIEW_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_POPULARITY_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_POSTER_PATH_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_RELEASE_DATE_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_TITLE_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_VOTE_AVERAGE_FIELD
import com.myniprojects.towatch.utils.const.DatabaseFields.MOVIE_VOTE_COUNT_FIELD
import com.myniprojects.towatch.utils.const.IMAGE_BASE_URL
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalMovie(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String?,
    val posterPath: String?,
    val popularity: Double?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    var isWatched: Boolean?
) : Parcelable
{

    @IgnoredOnParcel
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

    fun dbHashMap(): Map<String, Any?> = hashMapOf(
        MOVIE_TITLE_FIELD to title,
        MOVIE_OVERVIEW_FIELD to overview,
        MOVIE_BACKDROP_PATH_FIELD to backdropPath,
        MOVIE_POSTER_PATH_FIELD to posterPath,
        MOVIE_POPULARITY_FIELD to popularity,
        MOVIE_RELEASE_DATE_FIELD to releaseDate,
        MOVIE_VOTE_AVERAGE_FIELD to voteAverage,
        MOVIE_VOTE_COUNT_FIELD to voteCount,
        MOVIE_IS_WATCHED_FIELD to isWatched,
    ).filterValues {
        it != null
    }.mapValues {
        it.value as Any
    }

}