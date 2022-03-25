package com.cmb.hbnews.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cmb.hbnews.R

import com.cmb.hbnews.models.NewsHeader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_news.view.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MainNewsListAdapter(
    private val newsList: List<NewsHeader>
) : RecyclerView.Adapter<MainNewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.description.text = news.description
        Picasso.get().load(news.imgSrc)
            .placeholder(R.drawable.ic_image_not_found)
            .into(holder.newsImage)

        holder.newsSrcImage.setImageResource(news.newsSrcLogoResource)
    }

    override fun getItemCount(): Int = newsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val description: TextView = view.description
        val newsImage: ImageView = view.image_view
        val newsSrcImage: ImageView = view.news_src
    }
}
