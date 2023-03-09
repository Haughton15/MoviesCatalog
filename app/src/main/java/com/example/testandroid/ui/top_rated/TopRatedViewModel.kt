package com.example.testandroid.ui.top_rated

import androidx.lifecycle.*
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopRatedViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    private var currentPage = 1
    private val pageSize = 20 // por ejemplo, 20 elementos por página

    private val _topRatedMovies = MutableLiveData<Resource<List<MovieEntity>>>()
    val topRatedMovies: LiveData<Resource<List<MovieEntity>>> = _topRatedMovies
    init {
        loadNextPage()
    }
    fun loadNextPage() {
        _topRatedMovies.value = Resource.loading(null)

        viewModelScope.launch {
            repository.getTopRatedMovies(currentPage).observeForever { result ->
                when (result.resourceStatus) {
                    ResourceStatus.SUCCESS -> {
                        currentPage++
                        val oldList = _topRatedMovies.value?.data ?: emptyList()
                        val newList = oldList + result.data!!
                        _topRatedMovies.value = Resource.success(newList)

                    }
                    ResourceStatus.ERROR -> {
                        _topRatedMovies.value = Resource.error(result.message!!, null)
                    }
                    ResourceStatus.LOADING -> {
                        _topRatedMovies.value = Resource.loading(null)
                    }
                }
            }
        }
    }
}