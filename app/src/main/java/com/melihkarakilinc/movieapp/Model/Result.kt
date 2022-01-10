package com.melihkarakilinc.movieapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieData")
data class Result(
    val poster_path: String,
    val overview:String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    @PrimaryKey(autoGenerate = false)
    val id: Int

)