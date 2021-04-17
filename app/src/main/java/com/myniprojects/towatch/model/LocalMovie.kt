package com.myniprojects.towatch.model

import android.os.Parcelable
import com.myniprojects.towatch.utils.const.IMAGE_BASE_URL
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
    val voteCount: Int?
): Parcelable
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