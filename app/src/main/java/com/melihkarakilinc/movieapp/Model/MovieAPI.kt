package com.melihkarakilinc.movieapp.Model

import com.melihkarakilinc.movieapp.ApiUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET(ApiUrl.MOVIE_URL)
    suspend fun getMovie(
        @Query("api_key") api_key: String = ApiUrl.API_KEY,
        @Query("page") page: Int
    ): Response<MovieModel>

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilar(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String = ApiUrl.API_KEY
    ): Response<MovieModel>

    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MovieAPI::class.java)
    }
}