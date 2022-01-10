package com.melihkarakilinc.movieapp.LocalData.Repository

import androidx.lifecycle.LiveData
import com.melihkarakilinc.movieapp.LocalData.MovieDao
import com.melihkarakilinc.movieapp.Model.Result

class MovieRepository(private val movieDao: MovieDao) {

    val getAllData: LiveData<List<Result>> = movieDao.getAllData()

    suspend fun insertData(result: Result) {
        movieDao.insertData(result)
    }

    suspend fun deleteData(result: Result) {
        movieDao.deleteData(result)
    }
}