package com.cmb.hbnews.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.cmb.hbnews.R
import com.cmb.hbnews.home.MainNewsListFragment
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.scrapers.NewsCategory
import com.cmb.hbnews.scrapers.ScraperThanhNien
import com.cmb.hbnews.scrapers.ScraperVnExpress
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * A fragment representing a list of Items.
 */
class CategoryNewsListFragment : Fragment() {
    private var newsCategory: NewsCategory = NewsCategory.LATEST
    private var headers: ArrayList<NewsHeader> = arrayListOf()

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

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val vnExpressNewsHeaders = ScraperVnExpress.getNewsHeaders(newsCategory)
            val thanhNienNewsHeaders = ScraperThanhNien.getNewsHeaders(newsCategory)
            var allNewsHeaders = arrayListOf<NewsHeader>()
            var length = Math.max(vnExpressNewsHeaders.size, thanhNienNewsHeaders.size);
            for (i in 0..length) {
                val vnExpressNewsHeader = vnExpressNewsHeaders.getOrNull(i)
                if (vnExpressNewsHeader != null)
                    allNewsHeaders.add(vnExpressNewsHeader)

                val thanhNienNewsHeader = thanhNienNewsHeaders.getOrNull(i)
                if (thanhNienNewsHeader != null)
                    allNewsHeaders.add(thanhNienNewsHeader)
            }

            headers.clear()
            headers.addAll(allNewsHeaders)
            withContext(Dispatchers.Main) { view.adapter?.notifyDataSetChanged() };
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

        //https://stackoverflow.com/questions/55404428/how-to-combine-two-different-length-lists-in-kotlin
        private fun combine(vararg lists: List<*>) : List<Any> = mutableListOf<Any>().also {
            combine(it, lists.map(List<*>::iterator))
        }

        private tailrec fun combine(targetList: MutableList<Any>, iterators: List<Iterator<*>>) {
            iterators.asSequence()
                .filter(Iterator<*>::hasNext)
                .mapNotNull(Iterator<*>::next)
                .forEach { targetList += it }
            if (iterators.asSequence().any(Iterator<*>::hasNext))
                combine(targetList, iterators)
        }
    }
}
