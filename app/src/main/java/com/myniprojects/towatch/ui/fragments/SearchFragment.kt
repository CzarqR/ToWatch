package com.myniprojects.towatch.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myniprojects.towatch.R
import com.myniprojects.towatch.adapters.movieadapter.MovieAdapter
import com.myniprojects.towatch.adapters.movieadapter.MovieClickListener
import com.myniprojects.towatch.databinding.FragmentSearchBinding
import com.myniprojects.towatch.utils.ext.exhaustive
import com.myniprojects.towatch.utils.ext.hideKeyboard
import com.myniprojects.towatch.utils.helper.viewBinding
import com.myniprojects.towatch.utils.status.BaseStatus
import com.myniprojects.towatch.vm.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchFragment : Fragment(R.layout.fragment_search)
{
    @Inject
    lateinit var movieAdapter: MovieAdapter

    private val viewModel: SearchViewModel by activityViewModels()

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private var menuItemSearch: MenuItem? = null
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupCollecting()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_search, menu)

        // region SearchView setup

        menuItemSearch = menu.findItem(R.id.itemSearch)
        searchView = menuItemSearch!!.actionView as SearchView

        // change searchText if it has been set
        viewModel.query.value?.let {
            menuItemSearch!!.expandActionView()
            searchView!!.setQuery(it, false)
            searchView!!.clearFocus()
        }

        searchView!!.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(textInput: String?): Boolean
                {
                    hideKeyboard()
                    viewModel.search(textInput)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean = true
            }
        )

        menuItemSearch!!.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener
            {
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = true

                /**
                 * collapsing search menu, show trending news again
                 */
                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean
                {
                    viewModel.search(null)
                    return true
                }
            }
        )

        // endregion
    }


    private fun setupView()
    {
        val movieClickListener = MovieClickListener(
            movieClickListener = {
                Timber.d("Movie $it was clicked")

                val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                    movieToDisplay = it,
                )
                findNavController().navigate(action)
            }
        )

        movieAdapter.movieClickListener = movieClickListener

        binding.rvMovies.adapter = movieAdapter
    }

    private fun setupCollecting()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.moviesToDisplay.collectLatest {
                Timber.d("Collected $it")
                when (it)
                {
                    BaseStatus.Sleep ->
                    {
                        binding.proBarLoading.isVisible = false
                        binding.rvMovies.isVisible = true
                    }
                    BaseStatus.Loading ->
                    {
                        binding.proBarLoading.isVisible = true
                        binding.rvMovies.isVisible = false
                    }
                    is BaseStatus.Success ->
                    {
                        binding.proBarLoading.isVisible = false
                        binding.rvMovies.isVisible = true

                        movieAdapter.submitList(it.data)
                    }
                    is BaseStatus.Failed ->
                    {

                    }
                }.exhaustive
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.miLogOut ->
            {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.sign_out))
                    .setMessage(resources.getString(R.string.log_out_confirmation))
                    .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                    }
                    .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                        viewModel.logOut()
                    }
                    .show()

                true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }

}