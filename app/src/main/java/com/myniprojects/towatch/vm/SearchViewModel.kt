package com.myniprojects.towatch.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.towatch.model.LocalMovie
import com.myniprojects.towatch.repository.FirebaseRepository
import com.myniprojects.towatch.repository.TmdbRepository
import com.myniprojects.towatch.utils.status.BaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tmdbRepository: TmdbRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel()
{
    val user = firebaseRepository.loggedUser

    private val _tmdbResponseSearch: MutableStateFlow<BaseStatus<List<LocalMovie>>> = MutableStateFlow(
        BaseStatus.Sleep
    )

    private val _tmdbResponseTrending: MutableStateFlow<BaseStatus<List<LocalMovie>>> = MutableStateFlow(
        BaseStatus.Sleep
    )

    private val _query: MutableStateFlow<String?> = MutableStateFlow(null)
    val query = _query.asStateFlow()

    private var searchJob: Job? = null

    @ExperimentalCoroutinesApi
    val moviesToDisplay: Flow<BaseStatus<List<LocalMovie>>> = combine(
        query,
        _tmdbResponseSearch,
        _tmdbResponseTrending
    ) { q, search, trending ->
        if (q == null)
        {
            trending
        }
        else
        {
            search
        }
    }

    fun search(title: String?)
    {
        searchJob?.cancel()

        val t = title?.trim()

        if (!t.isNullOrBlank())
        {
            searchJob = viewModelScope.launch {
                tmdbRepository.getMoviesByTitle(t).collectLatest {
                    _tmdbResponseSearch.value = it
                }
            }
        }

        _query.value = t
    }

    fun logOut() = firebaseRepository.logOut()

    init
    {
        search("inception")
        viewModelScope.launch {
            tmdbRepository.getTrending().collectLatest {
                _tmdbResponseTrending.value = it
            }
        }
    }
}