package com.example.testandroid.data.remote

import com.example.testandroid.data.model.GetMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): Response<GetMoviesResponse>

    @GET("top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Response<GetMoviesResponse>

    @GET("upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Response<GetMoviesResponse>
}