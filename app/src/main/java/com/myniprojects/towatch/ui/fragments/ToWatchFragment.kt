package com.myniprojects.towatch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.myniprojects.towatch.R
import com.myniprojects.towatch.databinding.FragmentTowatchBinding
import com.myniprojects.towatch.utils.helper.viewBinding
import com.myniprojects.towatch.vm.SavedMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ToWatchFragment : Fragment(R.layout.fragment_towatch)
{
    private val binding by viewBinding(FragmentTowatchBinding::bind)
    private val viewModel: SavedMoviesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(viewModel.toString())

        setupCollecting()
    }

    private fun setupCollecting()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.toWatchMovies.collectLatest {
                Timber.d("toWatchMovies: $it")
            }
        }
    }

}