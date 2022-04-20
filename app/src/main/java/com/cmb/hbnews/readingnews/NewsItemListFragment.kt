package com.cmb.hbnews.readingnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsItems.NewsItem

/**
 * A fragment representing a list of Items.
 */
class NewsItemListFragment(
    private val newsItem: ArrayList<NewsItem>
    ) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_item_list, container, false) as RecyclerView

        with(view) {
            layoutManager = LinearLayoutManager(context)
            adapter = NewsItemListAdapter(newsItem)
        }
        return view
    }
}
