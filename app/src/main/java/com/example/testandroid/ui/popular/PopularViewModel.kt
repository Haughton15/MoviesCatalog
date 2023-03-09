package com.example.testandroid.ui.popular

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.paging.*
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class PopularViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {
    /*fun notifylastVisible(lastVisible:Int) {
        repository.checkRequireNewPage(lastVisible)
    }*/
    private var isLoading = false
    private var currentPage = 1

    val fetchPopularMovies: LiveData<Resource<List<MovieEntity>>> = repository.getPopularMovies(1)


    private val pageSize = 20 // tamaño de la página

    // crear una instancia de la clase PagingSource
    private val popularPagingSource = object : PagingSource<Int, MovieEntity>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
            val page = params.key ?: 1 // página inicial
            var data = emptyList<MovieEntity>() // lista de películas
            val response = repository.getPopularMovies(page)
            data = response.value!!.data!!
            return LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        }

        override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
            return state.anchorPosition
        }
    }

    // crear una instancia de la clase Pager
    val popularMovies = Pager(
        config = PagingConfig(pageSize),
        pagingSourceFactory = { popularPagingSource }
    ).liveData

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 20)
    ) {
        PopularViewModel(repository).popularPagingSource
    }.flow
        .cachedIn(viewModelScope)

}
