package com.example.takkumattsu.rxmovie

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface MovieInfoDao {
    @Query("SELECT * FROM MovieInfo")
    fun getAll(): List<MovieInfo>
    @Query("SELECT * From MovieInfo WHERE is_fav=1")
    fun getFavMovie(): List<MovieInfo>
    @Update
    fun update(movieInfo: MovieInfo)
    @Insert
    fun insert(movieInfo: MovieInfo)
}