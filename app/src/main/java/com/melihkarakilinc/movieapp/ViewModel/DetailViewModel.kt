package com.melihkarakilinc.movieapp.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.melihkarakilinc.movieapp.Model.MovieAPI
import com.melihkarakilinc.movieapp.Model.Result
import kotlinx.coroutines.*

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val similarData = MutableLiveData<List<Result>>()
    val error = MutableLiveData<String>()
    var progres = MutableLiveData<Boolean>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        error.value = message
    }

    fun getData(id: Int): List<Result>? {
        progres.value = true
        var job: Job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = MovieAPI.service.getSimilar(id=id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    similarData.value = response.body()?.results
                    progres.value = false
                } else {
                    Log.e("Code",response.code().toString())
                    onError("Error : ${response.message()} ")
                }
            }
        }
        return similarData.value
    }


}