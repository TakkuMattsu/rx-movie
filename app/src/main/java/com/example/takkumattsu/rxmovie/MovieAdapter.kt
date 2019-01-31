package com.example.takkumattsu.rxmovie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.TextView
import org.w3c.dom.Text

data class Movie(
        val title: String,
        val genres: String
)

class MovieAdapter(context: Context): ArrayAdapter<Movie>(context, 0) {

    data class ViewHolder(
            val title: TextView,
            val genres: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, null)
        val holder = (view.tag as? ViewHolder) ?: {
            val viewHolder = ViewHolder(
                    view.findViewById(android.R.id.text1),
                    view.findViewById(android.R.id.text2)
            )
            view.tag = viewHolder
            viewHolder
        }()
        val item = getItem(position)
        holder.title.text = item.title
        holder.genres.text = item.genres
        return view
    }
}