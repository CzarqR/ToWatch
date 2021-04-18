package com.myniprojects.towatch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.myniprojects.towatch.R
import com.myniprojects.towatch.databinding.FragmentWatchedBinding
import com.myniprojects.towatch.utils.helper.viewBinding
import com.myniprojects.towatch.vm.SavedMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class WatchedFragment : Fragment(R.layout.fragment_watched)
{
    private val binding by viewBinding(FragmentWatchedBinding::bind)
    private val viewModel: SavedMoviesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setupCollecting()
    }

    private fun setupCollecting()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.watchedMovies.collectLatest {
                Timber.d("watchedMovies: $it")
            }
        }
    }
}