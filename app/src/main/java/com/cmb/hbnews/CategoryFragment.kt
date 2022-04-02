package com.cmb.hbnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cmb.hbnews.category.CategoryNewsHeadersViewPagerAdapter
import com.cmb.hbnews.home.MainNewsListFragment
import com.cmb.hbnews.scrapers.NewsCategory
import kotlinx.android.synthetic.main.fragment_category.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        view.viewPager.adapter = CategoryNewsHeadersViewPagerAdapter(getParentFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        view.tabLayout.setupWithViewPager(view.viewPager)

        return view
    }

    private fun navigateNews(category: NewsCategory) {

    }
}
