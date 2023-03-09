package com.example.testandroid.data.remote

import com.example.testandroid.utils.Const
import com.example.testandroid.utils.BaseDataSource
import com.example.testandroid.utils.PageUtils
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiServices: ApiService) : BaseDataSource() {



    suspend fun getPopularMovies(pageUtils: Int) = getResult { apiServices.getPopularMovies(Const.API_KEY, pageUtils)}

    suspend fun getTopRatedMovies(pageUtils: Int) = getResult { apiServices.getTopRatedMovies(Const.API_KEY, pageUtils)}

    suspend fun getUpcomingMovies(pageUtils: Int) = getResult { apiServices.getUpcomingMovies(Const.API_KEY, pageUtils)}
}