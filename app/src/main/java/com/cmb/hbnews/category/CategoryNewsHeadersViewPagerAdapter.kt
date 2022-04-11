package com.cmb.hbnews.category

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cmb.hbnews.scrapers.NewsCategory

class CategoryNewsHeadersViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return NewsCategory.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CategoryNewsListFragment.newInstance(NewsCategory.LATEST)
            1 -> CategoryNewsListFragment.newInstance(NewsCategory.CURRENT_AFFAIRS)
            2 -> CategoryNewsListFragment.newInstance(NewsCategory.BUSINESS)
            3 -> CategoryNewsListFragment.newInstance(NewsCategory.SPORTS)
            4 -> CategoryNewsListFragment.newInstance(NewsCategory.ENTERTAINMENT)
            5 -> CategoryNewsListFragment.newInstance(NewsCategory.TECHNOLOGY)
            6 -> CategoryNewsListFragment.newInstance(NewsCategory.LIFESTYLE)
            7 -> CategoryNewsListFragment.newInstance(NewsCategory.HEALTH)
            8 -> CategoryNewsListFragment.newInstance(NewsCategory.TRAVEL)
            else -> throw NotImplementedError()
        }
    }
}
