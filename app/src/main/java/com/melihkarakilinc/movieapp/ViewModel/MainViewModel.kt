package com.melihkarakilinc.movieapp.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melihkarakilinc.movieapp.LocalData.AppDatabase
import com.melihkarakilinc.movieapp.LocalData.Repository.MovieRepository
import com.melihkarakilinc.movieapp.Model.MovieAPI
import com.melihkarakilinc.movieapp.Model.Result
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val movieData = MutableLiveData<List<Result>>()
    val error = MutableLiveData<String>()
    var progres = MutableLiveData<Boolean>()
    var movieList = ArrayList<Result>()

    private val noteDao = AppDatabase.getDatabase(application).movieDao()
    private var repository: MovieRepository = MovieRepository(noteDao)
    val getAllData: LiveData<List<Result>> = repository.getAllData

    fun insertData(result: Result) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.insertData(result)
        }
    }

    fun deleteData(result: Result) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteData(result)
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        error.value = message
    }

    fun getData(page: Int): List<Result>? {
        progres.value = true
        var job: Job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = MovieAPI.service.getMovie(page = page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.addAll(response.body()?.results!!)
                    movieData.value = movieList
                    progres.value = false
                } else {
                    Log.e("Code",response.code().toString())
                    onError("Error : ${response.message()} ")
                }
            }
        }
        return movieData.value
    }
}