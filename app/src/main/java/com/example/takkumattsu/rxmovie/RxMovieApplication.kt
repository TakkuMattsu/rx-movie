package com.example.takkumattsu.rxmovie

import android.app.Application
import androidx.room.Room

class RxMovieApplication : Application() {
    companion object {
        lateinit var database: RxMovieDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room
                .databaseBuilder(this, RxMovieDatabase::class.java, "movies.db")
                .build()
    }
}