package com.cmb.hbnews.category

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
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 */
class CategoryNewsListFragment : Fragment() {
    private var newsCategory: NewsCategory = NewsCategory.LATEST
    private var headers: ArrayList<NewsHeader> = arrayListOf()
    private var currentJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            newsCategory = it.getSerializable(ARG_NEWS_CATEGORY) as NewsCategory
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_news_list, container, false) as RecyclerView

        with(view) {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoryNewsListAdapter(headers)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as RecyclerView

        NewsProvider.addOnChangeListener {
            currentJob?.cancel()
            headers.clear()
            view.adapter?.notifyDataSetChanged()

            currentJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                yield()
                val news = NewsProvider.getNewsHeaders(newsCategory)
                yield()
                headers.addAll(news)
                withContext(Dispatchers.Main) { view.adapter?.notifyDataSetChanged() };
            }
        }
    }

    companion object {
        const val ARG_NEWS_CATEGORY = "news-category"

        @JvmStatic
        fun newInstance(category: NewsCategory): CategoryNewsListFragment  {
             val instance = CategoryNewsListFragment()

             instance.apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_NEWS_CATEGORY, category)
                }
            }

            return instance
        }
    }
}
