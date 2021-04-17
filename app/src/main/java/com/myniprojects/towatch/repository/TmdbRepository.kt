package com.myniprojects.towatch.repository

import com.myniprojects.towatch.model.LocalMovie
import com.myniprojects.towatch.network.tmdb.TmdbService
import com.myniprojects.towatch.utils.ext.makeEvent
import com.myniprojects.towatch.utils.status.BaseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val tmdbService: TmdbService
)
{
    fun getMoviesByTitle(title: String): Flow<BaseStatus<List<LocalMovie>>> =
            flow {
                emit(BaseStatus.Loading)
                try
                {
                    val tmdbResponse = tmdbService.getMovieFromTitle(title)
                    emit(
                        BaseStatus.Success(
                            tmdbResponse.movies.mapNotNull {
                                it.toLocalMovie
                            }
                        )
                    )
                }
                catch (e: Exception)
                {
                    emit(BaseStatus.Failed(e.makeEvent))
                }
            }


    fun getTrending(): Flow<BaseStatus<List<LocalMovie>>> =
            flow {
                emit(BaseStatus.Loading)
                try
                {
                    val tmdbResponse = tmdbService.getTrendingMovies()
                    emit(
                        BaseStatus.Success(
                            tmdbResponse.movies.mapNotNull {
                                it.toLocalMovie
                            }
                        )
                    )
                }
                catch (e: Exception)
                {
                    emit(BaseStatus.Failed(e.makeEvent))
                }
            }

}