package com.myniprojects.towatch.adapters.movieadapter

import androidx.recyclerview.widget.DiffUtil
import com.myniprojects.towatch.model.LocalMovie

object MovieDiffCallback : DiffUtil.ItemCallback<LocalMovie>()
{
    override fun areItemsTheSame(oldItem: LocalMovie, newItem: LocalMovie): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LocalMovie, newItem: LocalMovie): Boolean =
            oldItem == newItem
}