package com.cmb.hbnews.category

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cmb.hbnews.scrapers.NewsCategory

class CategoryNewsHeadersViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior){
    override fun getCount(): Int {
        return NewsCategory.values().size
    }

    override fun getItem(position: Int): Fragment {
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

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "TIN MỚI"
            1 -> "THỜI SỰ"
            2 -> "KINH DOANH"
            3 -> "THỂ THAO"
            4 -> "GIẢI TRÍ"
            5 -> "CÔNG NGHỆ"
            6 -> "ĐỜI SỐNG"
            7 -> "SỨC KHỎE"
            8 -> "DU LỊCH"
            else -> throw NotImplementedError()
        }
    }
}
