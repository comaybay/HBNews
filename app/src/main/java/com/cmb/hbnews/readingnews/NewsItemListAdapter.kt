package com.cmb.hbnews.readingnews

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsItems.NewsItem
import com.cmb.hbnews.models.NewsItems.NewsItemImage
import com.cmb.hbnews.models.NewsItems.NewsItemText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_news.view.*
import kotlinx.android.synthetic.main.fragment_news_item_image.view.*
import kotlinx.android.synthetic.main.fragment_news_item_text.view.*


class NewsItemListAdapter(
    private val newsItems: List<NewsItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        //cho view type bằng với vị trí
        return position;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (newsItems[viewType].type) {
            NewsItem.Type.TEXT -> ViewHolderText(LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item_text, parent, false))
            NewsItem.Type.IMAGE -> ViewHolderImage(LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item_image, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = newsItems[position]
        when (item.type) {
            NewsItem.Type.TEXT -> {
                val textItem = item as NewsItemText
                val holderText = holder as ViewHolderText

                holderText.text.text = textItem.text

                fun makeBold() = holderText.text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                fun setTextSize(size: Float) = holderText.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

                when (textItem.textType) {
                    NewsItemText.TextType.P -> setTextSize(16f)
                    NewsItemText.TextType.P_BOLD -> { setTextSize(16f); makeBold() }
                    NewsItemText.TextType.H2 -> { setTextSize(24f); makeBold() }
                }
            }
            NewsItem.Type.IMAGE -> {
                val imageItem = item as NewsItemImage
                val holderText = holder as ViewHolderImage

                val imgSrc = if (imageItem.imgSrc.isNullOrEmpty()) "empty" else imageItem.imgSrc

                Glide.with(holder.itemView.getContext())
                     .load(imgSrc)
                     .placeholder(R.drawable.ic_image_not_found)
                     .into(holder.image);

                holderText.caption.text = imageItem.caption
            }
        }
    }

    override fun getItemCount(): Int = newsItems.size

    inner class ViewHolderText(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.news_item_text
    }

    inner class ViewHolderImage(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.news_item_image
        val caption: TextView = view.news_item_caption
    }
}
