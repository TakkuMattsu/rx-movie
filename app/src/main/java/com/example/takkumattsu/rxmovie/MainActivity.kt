package com.example.takkumattsu.rxmovie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.ListView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    data class MovieInfo (
        val movieId: String,
        val title: String,
        val genres: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById<ListView>(R.id.list)
        val adapter = MovieAdapter(this)
        listView.adapter = adapter
        listView.isTextFilterEnabled = true
        loadMovieCsv(R.raw.mini_movies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.add(Movie(it.title, it.genres))
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        // 文字色、サイズの変更とプレースホルダーセット(任意)
        val searchView = menu!!.findItem(R.id.search_menu_search_view).actionView as SearchView
        searchView.queryTextChanges()
                .subscribe { listView.setFilterText(it.toString()) }
        return true
    }

    private fun loadMovieCsv(@RawRes from: Int): Observable<MovieInfo> {
        // csvを読み込む
        // 1行づつ読み込んだやつをMovie dataクラスにして
        // Observableで返す
        return Observable.create<MovieInfo> { emitter ->
            resources.openRawResource(from)
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
