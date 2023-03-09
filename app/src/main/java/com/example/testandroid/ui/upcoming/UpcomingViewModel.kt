package com.example.testandroid.ui.upcoming

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
class UpcomingViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    private var currentPage = 1
    private val pageSize = 20 // por ejemplo, 20 elementos por página

    private val _upcomingMovies = MutableLiveData<Resource<List<MovieEntity>>>()
    val upcomingMovies: LiveData<Resource<List<MovieEntity>>> = _upcomingMovies
    init {
        loadNextPage()
    }
    fun loadNextPage() {
        _upcomingMovies.value = Resource.loading(null)

        viewModelScope.launch {
            repository.getUpcomingMovies(currentPage).observeForever { result ->
                when (result.resourceStatus) {
                    ResourceStatus.SUCCESS -> {
                        currentPage++
                        val oldList = _upcomingMovies.value?.data ?: emptyList()
                        val newList = oldList + result.data!!
                        _upcomingMovies.value = Resource.success(newList)

                    }
                    ResourceStatus.ERROR -> {
                        _upcomingMovies.value = Resource.error(result.message!!, null)
                    }
                    ResourceStatus.LOADING -> {
                        _upcomingMovies.value = Resource.loading(null)
                    }
                }
            }
        }
    }
}