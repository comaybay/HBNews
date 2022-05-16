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
        return 8;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CategoryNewsListFragment.newInstance(NewsCategory.CURRENT_AFFAIRS)
            1 -> CategoryNewsListFragment.newInstance(NewsCategory.BUSINESS)
            2 -> CategoryNewsListFragment.newInstance(NewsCategory.SPORTS)
            3 -> CategoryNewsListFragment.newInstance(NewsCategory.ENTERTAINMENT)
            4 -> CategoryNewsListFragment.newInstance(NewsCategory.TECHNOLOGY)
            5 -> CategoryNewsListFragment.newInstance(NewsCategory.LIFESTYLE)
            6 -> CategoryNewsListFragment.newInstance(NewsCategory.HEALTH)
            7 -> CategoryNewsListFragment.newInstance(NewsCategory.TRAVEL)
            else -> throw NotImplementedError()
        }
    }
}
