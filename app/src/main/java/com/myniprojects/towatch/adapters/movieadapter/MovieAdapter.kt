package com.myniprojects.towatch.adapters.movieadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.myniprojects.towatch.model.Movie
import javax.inject.Inject

class MovieAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback)
{
    lateinit var movieClickListener: MovieClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder.create(parent)

    override fun onBindViewHolder(holderImage: MovieViewHolder, position: Int) =
            holderImage.bind(
                glide = glide,
                movieClickListener = movieClickListener,
                movie = getItem(position)
            )
}