package com.cmb.hbnews.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.reading_news
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_category_news.view.*
import kotlinx.android.synthetic.main.fragment_category_news.view.description
import kotlinx.android.synthetic.main.fragment_category_news.view.image_view
import kotlinx.android.synthetic.main.fragment_category_news.view.news_src
import kotlinx.android.synthetic.main.fragment_category_news.view.title
import kotlinx.android.synthetic.main.fragment_main_news.view.*


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CategoryNewsListAdapter(
    private val newsList: List<NewsHeader>
) : RecyclerView.Adapter<CategoryNewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_news, parent, false)
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

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, reading_news::class.java)

            intent.putExtra("title",holder.title.text)
            intent.putExtra("description",holder.description.text)
            intent.putExtra("newsImage",news.imgSrc)
            intent.putExtra("newsSrcImage",news.newsSrcLogoResource)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = newsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val description: TextView = view.description
        val newsImage: ImageView = view.image_view
        val newsSrcImage: ImageView = view.news_src
    }
}
