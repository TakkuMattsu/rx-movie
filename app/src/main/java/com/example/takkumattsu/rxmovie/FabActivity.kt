package com.example.takkumattsu.rxmovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlin.concurrent.thread

class FabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.list)
        val adapter = MovieAdapter(this)
        adapter.onClickFab.subscribe {
            thread {
                val dao = RxMovieApplication.database.movieInfoDao()
                // TODO: 本来はこれを更新することでボタンの色を変えたりする
                dao.insert(MovieInfo(it.movieId, it.title, it.genres, !it.isFav))
            }
        }
        listView.adapter = adapter
        loadFabMovie()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.add(Movie(it.movieId, it.title, it.genres, it.isFav))
                }
    }

    private fun loadFabMovie(): Observable<MovieInfo> {
        return Observable.create<MovieInfo> { emitter ->
            thread {
                val movies = RxMovieApplication.database.movieInfoDao().getFavMovie()
                movies.forEach {
                    emitter.onNext(it)
                }
                emitter.onComplete()
            }
        }
    }
}