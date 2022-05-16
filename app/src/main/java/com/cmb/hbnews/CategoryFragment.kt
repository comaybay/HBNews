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
import com.cmb.hbnews.scrapers.NewsProvider
import com.cmb.hbnews.scrapers.NewsSource
import com.google.android.material.tabs.TabLayoutMediator
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
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.viewPager.adapter = CategoryNewsHeadersViewPagerAdapter(this)
        view.viewPager.offscreenPageLimit = 7
        TabLayoutMediator(view.tabLayout, view.viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.setText("THỜI SỰ")
                1 -> tab.setText("KINH DOANH")
                2 -> tab.setText("THỂ THAO")
                3 -> tab.setText("GIẢI TRÍ")
                4 -> tab.setText("CÔNG NGHỆ")
                5 -> tab.setText("ĐỜI SỐNG")
                6 -> tab.setText("SỨC KHỎE")
                7 -> tab.setText("DU LỊCH")
                else -> throw NotImplementedError()
            }
        }).attach()
    }
}
