package com.example.takkumattsu.rxmovie

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MovieInfo::class], version = 1)
abstract class RxMovieDatabase: RoomDatabase() {
    abstract fun movieInfoDao(): MovieInfoDao
}