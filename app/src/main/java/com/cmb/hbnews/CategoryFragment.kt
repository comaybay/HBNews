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
        view.viewPager.offscreenPageLimit = 8
        TabLayoutMediator(view.tabLayout, view.viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.setText("TIN MỚI")
                1 -> tab.setText("THỜI SỰ")
                2 -> tab.setText("KINH DOANH")
                3 -> tab.setText("THỂ THAO")
                4 -> tab.setText("GIẢI TRÍ")
                5 -> tab.setText("CÔNG NGHỆ")
                6 -> tab.setText("ĐỜI SỐNG")
                7 -> tab.setText("SỨC KHỎE")
                8 -> tab.setText("DU LỊCH")
                else -> throw NotImplementedError()
            }
        }).attach()
    }
}
