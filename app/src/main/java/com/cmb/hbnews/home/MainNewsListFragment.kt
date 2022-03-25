package com.cmb.hbnews.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsHeader

/**
 * A fragment representing a list of Items.
 */
class MainNewsListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_news_list, container, false)

        val createMockNewsHeader1 = { NewsHeader("Quan chức Nga nói Phó tư lệnh Hạm đội Biển Đen tử trận ở Ukraine", "Nghị sĩ và thống đốc Nga xác nhận đại tá Andrey Paliy, phó tư lệnh Hạm đội Biển Đen, thiệt mạng trong giao tranh gần thành phố Mariupol, đông nam Ukraine.", "https://i1-vnexpress.vnecdn.net/2022/03/21/tuong-nga-jpeg-1647822727-5170-1647822830.jpg?w=220&h=132&q=100&dpr=1&fit=crop&s=C2ECsmGBFygosQAOBJ_zNg", R.drawable.ic_logo_thanhnien) };
        val createMockNewsHeader2 = { NewsHeader("Sâm Ngọc Linh 30 triệu đồng một lạng vẫn hút khách", "Những củ sâm Ngọc Linh trên 16 năm tuổi, nặng hơn 100 gram có giá bán 30 triệu đồng 100 gram (một lạng) vẫn được nhiều người đặt mua.", "https://i1-kinhdoanh.vnecdn.net/2022/03/07/snl-1646647644-1646647655-9036-1646647971.jpg?w=220&h=132&q=100&dpr=1&fit=crop&s=RH3lJ_AvENbcNYGq-3Svgg", R.drawable.ic_logo_vnexpress) };

        val news = listOf<NewsHeader>(
            createMockNewsHeader1(),
            createMockNewsHeader2(),
            createMockNewsHeader1(),
            createMockNewsHeader2(),
            createMockNewsHeader1(),
            createMockNewsHeader2(),
            createMockNewsHeader1(),
            createMockNewsHeader2()
        )

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MainNewsListAdapter(news)
            }
        }

        return view
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
