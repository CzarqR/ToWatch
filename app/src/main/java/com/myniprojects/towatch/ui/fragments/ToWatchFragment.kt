package com.myniprojects.towatch.ui.fragments

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.myniprojects.towatch.R
import com.myniprojects.towatch.model.LocalMovie
import com.myniprojects.towatch.utils.status.BaseStatus
import com.myniprojects.towatch.vm.SavedMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class ToWatchFragment : RecyclerFragment()
{
    private val viewModel: SavedMoviesViewModel by activityViewModels()

    override val moviesToDisplay: Flow<BaseStatus<List<LocalMovie>>>
        get() = viewModel.toWatchMovies

    override val imgEmpty: Int
        get() = R.drawable.ic_outline_movie_24
    override val txtEmpty: Int
        get() = R.string.empty_result_towatch

    override fun actionToDetails(movie: LocalMovie)
    {
        findNavController().navigate(
            ToWatchFragmentDirections.actionToWatchFragmentToDetailsFragment(movieToDisplay = movie)
        )
    }
}