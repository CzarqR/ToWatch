package com.myniprojects.towatch.adapters.movieadapter

import com.myniprojects.towatch.model.LocalMovie

data class MovieClickListener(
    val movieClickListener: (LocalMovie) -> Unit
)
{
    fun movieClick(localMovie: LocalMovie) = movieClickListener(localMovie)
}