package com.myniprojects.towatch.adapters.movieadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.myniprojects.towatch.databinding.MovieItemBinding
import com.myniprojects.towatch.model.Movie

class MovieViewHolder private constructor(
    private val binding: MovieItemBinding
) : RecyclerView.ViewHolder(binding.root)
{
    companion object
    {
        fun create(parent: ViewGroup): MovieViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(
                binding
            )
        }
    }


    fun bind(
        glide: RequestManager,
        movieClickListener: MovieClickListener,
        movie: Movie
    )
    {
        with(binding)
        {
            glide
                .load(movie.fullPath)
                .into(imgPoster)
        }

        binding.movie = movie
        binding.movieClickListener = movieClickListener
        binding.executePendingBindings()
    }
}