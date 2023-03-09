package com.example.testandroid.data.repository

import com.example.testandroid.data.local.MovieDao
import com.example.testandroid.data.model.MovieType
import com.example.testandroid.data.model.toMovieEntityList
import com.example.testandroid.data.remote.RemoteDataSource
import com.example.testandroid.utils.PageUtils
import javax.inject.Inject
import com.example.testandroid.utils.performGetOperation

class MovieRepository @Inject constructor(
    private val localDataSource: MovieDao,
    private val remoteDataSource: RemoteDataSource) {

    init {
        //checkRequireNewPage(0)
    }
    companion object{
        const val PAGE_SIZE =20
        const val  PAGE_THRESHOLD=4
    }
    fun getPopularMovies(page:Int) = performGetOperation(
        databaseQuery = { localDataSource.getAllMovies(MovieType.POPULAR.value) },
        networkCall = { remoteDataSource.getPopularMovies(page) },
        saveCallResult = { localDataSource.insertAll(it.results.toMovieEntityList(MovieType.POPULAR.value)) }
    )

    fun getTopRatedMovies() = performGetOperation(
        databaseQuery = { localDataSource.getAllMovies(MovieType.TOP_RATED.value) },
        networkCall = { remoteDataSource.getTopRatedMovies() },
        saveCallResult = { localDataSource.insertAll(it.results.toMovieEntityList(MovieType.TOP_RATED.value)) }
    )

    fun getUpcomingMovies() = performGetOperation(
        databaseQuery = { localDataSource.getAllMovies(MovieType.UPCOMING.value) },
        networkCall = { remoteDataSource.getUpcomingMovies() },
        saveCallResult = { localDataSource.insertAll(it.results.toMovieEntityList(MovieType.UPCOMING.value)) }
    )

    /*fun checkRequireNewPage(lastVisible: Int) {
        val size = localDataSource.size()
    }*/
}