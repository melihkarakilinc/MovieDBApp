package com.melihkarakilinc.movieapp.LocalData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.melihkarakilinc.movieapp.Model.Result

@Dao
interface MovieDao {

    @Query("SELECT*FROM movieData")
    fun getAllData(): LiveData<List<Result>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(result: Result)

    @Delete
    suspend fun deleteData(result: Result)
}