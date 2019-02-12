package com.example.takkumattsu.rxmovie

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.w3c.dom.Text

data class Movie(
        val movieId: String,
        val title: String,
        val genres: String,
        val isFav: Boolean
){
    override fun toString(): String {
        return title
    }
}

class MovieAdapter(context: Context): ArrayAdapter<Movie>(context, 0) {

    private val onClickFabObserver = PublishSubject.create<Movie>()
    val onClickFab: Observable<Movie> = onClickFabObserver

    data class ViewHolder(
            val title: TextView,
            val genres: TextView,
            val fabBtn: Button
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.movie_item, null)
        val holder = (view.tag as? ViewHolder) ?: {
            val viewHolder = ViewHolder(
                    view.findViewById(R.id.title),
                    view.findViewById(R.id.genres),
                    view.findViewById(R.id.fab)
            )
            view.tag = viewHolder
            viewHolder
        }()
        val item = getItem(position)
        holder.title.text = item.title
        holder.genres.text = item.genres
        holder.fabBtn.setOnClickListener {
            onClickFabObserver.onNext(item)
        }
        holder.fabBtn.setBackgroundColor(if (item.isFav) Color.YELLOW else Color.WHITE)
        return view
    }
}