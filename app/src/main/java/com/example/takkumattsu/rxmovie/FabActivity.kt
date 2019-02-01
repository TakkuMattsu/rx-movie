package com.example.takkumattsu.rxmovie

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import io.reactivex.Observable

class FabActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.list)
        val adapter = MovieAdapter(this)
        listView.adapter = adapter
        loadFabMovie().subscribe {
            adapter.add(Movie(it.title, it.genres, it.isFab))
        }
    }

    private fun loadFabMovie(): Observable<MainActivity.MovieInfo> {
        // TODO: 未実装
        return Observable.just(MainActivity.MovieInfo("1", "モック", "ジャンル", true))
    }
}