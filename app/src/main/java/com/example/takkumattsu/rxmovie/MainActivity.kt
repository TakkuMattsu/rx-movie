package com.example.takkumattsu.rxmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

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
                    adapter.add(Movie(it.movieId, it.title, it.genres, it.isFav))
                }
        adapter.onClickFab
                .subscribe {
                    thread {
                        val dao = RxMovieApplication.database.movieInfoDao()
                        // TODO: 本来はこれを更新することでボタンの色を変えたりする
                        dao.insert(MovieInfo(it.movieId, it.title, it.genres, !it.isFav))
                    }
                }
        // やりたいこと
        // csv読み込む
        // データベースに読み込む
        // データベースに更新があったら一覧を更新
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        // 文字色、サイズの変更とプレースホルダーセット(任意)
        val searchView = menu!!.findItem(R.id.search_menu_search_view).actionView as SearchView
        searchView.queryTextChanges()
                .subscribe { listView.setFilterText(it.toString()) }
        menu.addSubMenu("Fabへ")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(this, FabActivity::class.java)
        startActivity(intent)
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
                                emitter.onNext(MovieInfo(l[0], l[1], l[2], false)) // CSVからの読み込みの際はfab情報がないため
                            }
                        }
                        emitter.onComplete()
                    }
        }
    }
}
