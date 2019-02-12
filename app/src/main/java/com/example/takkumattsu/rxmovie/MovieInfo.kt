package com.example.takkumattsu.rxmovie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieInfo (
        @PrimaryKey @ColumnInfo(name = "movie_id")
        val movieId: String,
        val title: String,
        val genres: String,
        @ColumnInfo(name = "is_fav")
        val isFav: Boolean
)