package com.myniprojects.towatch.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.myniprojects.towatch.R
import com.myniprojects.towatch.databinding.FragmentDetailsBinding
import com.myniprojects.towatch.model.LocalMovie
import com.myniprojects.towatch.utils.ext.setActionBarTitle
import com.myniprojects.towatch.utils.ext.tryShowSnackbarOK
import com.myniprojects.towatch.utils.helper.viewBinding
import com.myniprojects.towatch.utils.status.EventMessageStatus
import com.myniprojects.towatch.vm.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details)
{
    @Inject
    lateinit var glide: RequestManager

    private val viewModel: DetailsViewModel by activityViewModels()
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val args: DetailsFragmentArgs by navArgs()

    val movie: LocalMovie
        get() = args.movieToDisplay

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(movie.toString())
        (requireActivity() as AppCompatActivity).setActionBarTitle(
            if (!movie.releaseDate.isNullOrBlank())
                "${movie.title} (${movie.releaseDate!!.substring(0, 4)})"
            else
                movie.title
        )

        binding.movie = args.movieToDisplay
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupView()
        setupCollecting()
    }

    private fun setupView()
    {
        val path = movie.fullPath

        if (path != null)
        {
            binding.proBarImageLoading.isVisible = true
            glide
                .load(path)
                .listener(
                    object : RequestListener<Drawable>
                    {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean
                        {
                            binding.proBarImageLoading.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean
                        {
                            binding.proBarImageLoading.isVisible = false
                            return false
                        }

                    })
                .into(binding.imgPoster)
        }
        else
        {
            binding.imgPoster.isVisible = false
        }
    }

    private fun setupCollecting()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.addingState.collectLatest {
                when (it)
                {
                    EventMessageStatus.Sleep ->
                    {
                        binding.buttonsArea.isEnabled = true
                    }
                    EventMessageStatus.Loading ->
                    {
                        binding.buttonsArea.isEnabled = false
                    }
                    is EventMessageStatus.Success ->
                    {
                        binding.buttonsArea.isEnabled = true
                        requireContext().tryShowSnackbarOK(binding.cdRoot, it.successEvent)
                    }
                    is EventMessageStatus.Failed ->
                    {
                        binding.buttonsArea.isEnabled = true
                        requireContext().tryShowSnackbarOK(binding.cdRoot, it.errorEvent)
                    }
                }
            }
        }
    }
}