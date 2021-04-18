package com.myniprojects.towatch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.myniprojects.towatch.R
import com.myniprojects.towatch.adapters.movieadapter.MovieAdapter
import com.myniprojects.towatch.adapters.movieadapter.MovieClickListener
import com.myniprojects.towatch.databinding.FragmentRecyclerBinding
import com.myniprojects.towatch.model.LocalMovie
import com.myniprojects.towatch.utils.ext.exhaustive
import com.myniprojects.towatch.utils.ext.tryShowSnackbarOK
import com.myniprojects.towatch.utils.helper.viewBinding
import com.myniprojects.towatch.utils.status.BaseStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
abstract class RecyclerFragment : Fragment(R.layout.fragment_recycler)
{
    @Inject
    lateinit var movieAdapter: MovieAdapter

    abstract val moviesToDisplay: Flow<BaseStatus<List<LocalMovie>>>
    abstract fun actionToDetails(movie: LocalMovie)

    @get:DrawableRes
    abstract val imgEmpty: Int

    @get:StringRes
    abstract val txtEmpty: Int


    private val binding by viewBinding(FragmentRecyclerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupCollecting()
        setupView()
    }

    private fun setupCollecting()
    {
        lifecycleScope.launchWhenStarted {
            moviesToDisplay.collectLatest {
                when (it)
                {
                    BaseStatus.Sleep ->
                    {
                        binding.proBarLoading.isVisible = false
                        binding.rvMovies.isVisible = true
                        binding.linLayState.isVisible = false
                    }
                    BaseStatus.Loading ->
                    {
                        binding.proBarLoading.isVisible = true
                        binding.rvMovies.isVisible = false
                    }
                    is BaseStatus.Success ->
                    {
                        binding.proBarLoading.isVisible = false

                        binding.rvMovies.isVisible = it.data.isNotEmpty()
                        binding.linLayState.isVisible = it.data.isEmpty()
                        movieAdapter.submitList(it.data)

                    }
                    is BaseStatus.Failed ->
                    {
                        binding.proBarLoading.isVisible = false
                        binding.rvMovies.isVisible = true
                        binding.linLayState.isVisible = false
                        requireContext().tryShowSnackbarOK(binding.cdRoot, it.errorEvent)
                    }
                }.exhaustive
            }
        }
    }

    private fun setupView()
    {
        binding.imgEmpty.setImageResource(imgEmpty)
        binding.txtEmpty.setText(txtEmpty)

        val movieClickListener = MovieClickListener(
            movieClickListener = {
                actionToDetails(it)
            }
        )

        movieAdapter.movieClickListener = movieClickListener

        binding.rvMovies.adapter = movieAdapter
    }
}