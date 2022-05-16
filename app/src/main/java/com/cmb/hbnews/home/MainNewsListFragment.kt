package com.cmb.hbnews.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.scrapers.NewsCategory
import com.cmb.hbnews.scrapers.NewsProvider
import com.cmb.hbnews.scrapers.NewsSource
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 */
class MainNewsListFragment : Fragment() {
    private var headers: ArrayList<NewsHeader> = arrayListOf()
    private var currentJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_news_list, container, false) as RecyclerView
        with(view) {
            layoutManager = LinearLayoutManager(context)
            adapter = MainNewsListAdapter(headers)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NewsProvider.addOnChangeListener { getNews() }
        getNews();
    }

    fun getNews() {
        currentJob?.cancel()
        headers.clear()

        val view = view as RecyclerView
        view.adapter?.notifyDataSetChanged()

        currentJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            yield()
            val news = NewsProvider.getNewsHeaders(NewsCategory.LATEST)
            yield()
            headers.addAll(news)
            withContext(Dispatchers.Main) { view.adapter?.notifyDataSetChanged() };
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MainNewsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
