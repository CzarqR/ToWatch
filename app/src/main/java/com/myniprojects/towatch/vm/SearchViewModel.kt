package com.myniprojects.towatch.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.towatch.model.TmdbResponse
import com.myniprojects.towatch.repository.TmdbRepository
import com.myniprojects.towatch.utils.status.BaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TmdbRepository
) : ViewModel()
{
    private val _tmdbResponse: MutableStateFlow<BaseStatus<TmdbResponse>> = MutableStateFlow(
        BaseStatus.Sleep
    )
    val weatherResponse = _tmdbResponse.asStateFlow()

    fun launch(title: String)
    {
        viewModelScope.launch {
            repository.getMoviesByTitle(title).collectLatest {
                _tmdbResponse.value = it
            }
        }
    }
}