package com.cmb.hbnews

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.cmb.hbnews.models.News
import com.cmb.hbnews.scrapers.ScraperThanhNien
import com.cmb.hbnews.scrapers.ScraperVnExpress
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reading_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class reading_news : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_news)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }
        image_back_btn.setOnClickListener {
            onBackPressed()
        }


        val tilte_reading:String = intent.getStringExtra("title").toString()
        val description:String = intent.getStringExtra("description").toString()
        val newsImage:String = intent.getStringExtra("newsImage").toString()
        val newsSrcLogoResource:Int = intent.getIntExtra("newsSrcLogoResource", 0)
        val date:String = intent.getStringExtra("date").toString()

        //
        title_reading.setText(tilte_reading)
        description_reading.setText(description)
        publishtime_reading.setText(date)
        //
        Picasso.get().load(newsImage)
            .placeholder(R.drawable.ic_image_not_found)
            .into(image_description)
        //
        var news: News
        lifecycleScope.launch(Dispatchers.IO) {
            val url = intent.getStringExtra("newsUrl").toString()
            news = when (newsSrcLogoResource) {
                R.drawable.ic_logo_vnexpress -> ScraperVnExpress.getNewsFromUrl(url)
                R.drawable.ic_logo_thanhnien -> ScraperThanhNien.getNewsFromUrl(url)
                else -> throw NotImplementedError()
            }

            withContext(Dispatchers.Main) {
                //TODO: dùng news để hiển thị nội dung
                news
            }
        }
    }
}
