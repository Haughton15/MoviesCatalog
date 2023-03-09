package com.example.testandroid.ui.popular

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource

class MoviesPagingSource(private val repository: MovieRepository) : PagingSource<Int, MovieEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        val page = params.key ?: 1
        val response = repository.getPopularMovies(page)
        return when (response.value?.resourceStatus) {
            ResourceStatus.SUCCESS -> {
                val movies = response.value!!.data ?: emptyList()
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movies.isEmpty()) null else page + 1
                )
            }
            ResourceStatus.ERROR -> {
                LoadResult.Error(response.value!!.message?.let { Exception(it) }!!)
            }
            else -> {
                LoadResult.Error(Exception("Unknown error"))}
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        return state.anchorPosition
    }
}