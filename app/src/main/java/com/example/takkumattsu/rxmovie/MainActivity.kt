package com.example.takkumattsu.rxmovie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RawRes
import android.util.Log
import android.widget.ListView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    data class MovieInfo (
        val movieId: String,
        val title: String,
        val genres: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.list)
        val adapter = MovieAdapter(this)
        listView.adapter = adapter
        loadMovieCsv(R.raw.mini_movies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.add(Movie(it.title, it.genres))
                }
    }
    private fun loadMovieCsv(@RawRes from: Int): Observable<MovieInfo> {
        // csvを読み込む
        // 1行づつ読み込んだやつをMovie dataクラスにして
        // Observableで返す
        return Observable.create<MovieInfo> { emitter ->
            resources.openRawResource(R.raw.mini_movies)
                    .bufferedReader()
                    .use {
                        var line: String? = it.readLine()
                        while (line != null) {
                            // ヘッダー行を飛ばす
                            line = it.readLine()
                            line?.let {
                                val l = it.split(",")
                                emitter.onNext(MovieInfo(l[0],l[1],l[2]))
                            }
                        }
                        emitter.onComplete()
                    }
        }
    }
}
