package com.cmb.hbnews

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cmb.hbnews.models.News
import com.cmb.hbnews.readingnews.NewsItemListFragment
import com.cmb.hbnews.scrapers.NewsProvider
import com.cmb.hbnews.scrapers.NewsSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reading_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import userData
import java.text.SimpleDateFormat
import java.util.*

class reading_news : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private var userID: String =""
    private lateinit var database: DatabaseReference

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
        firebaseAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        val tilte_reading:String = intent.getStringExtra("title").toString()
        val description:String = intent.getStringExtra("description").toString()
        val newsImage:String = intent.getStringExtra("newsImage").toString()
        val newsSrcLogoResource:Int = intent.getIntExtra("newsSrcLogoResource", 0)
        val date:String = intent.getStringExtra("date").toString()
        val url = intent.getStringExtra("newsUrl").toString()
        val newsSource = intent.getSerializableExtra("newsSource") as NewsSource
        val newSourceString:String = newsSource.toString()
        Picasso.get().load(newsImage)
            .placeholder(R.drawable.ic_image_not_found)
            .into(image_description)
        title_reading.setText(tilte_reading)
        description_reading.setText(description)
        publishtime_reading.setText(date)
        val currentDateTime: String = SimpleDateFormat("HH:mm-dd/MM/yyyy").format(Date())
        var news: News
        lifecycleScope.launch(Dispatchers.IO) {

            news = NewsProvider.getNewsFromUrl(newsSource, url)
            if (firebaseAuth.currentUser != null) {
                userID = firebaseAuth.currentUser!!.uid
                database = FirebaseDatabase.getInstance().getReference("History")
                val update = userData(
                    date,
                    news.description,
                    newsImage,
                    newSourceString,
                    url,
                    news.title,
                    currentDateTime
                )
                database.child(userID).child(news.title).setValue(update)
            }
            withContext(Dispatchers.Main) {
                author_reading.setText(news.author)
                publishtime_reading.setText(news.date)
                description_reading.setText(news.description)
                title_reading.setText(news.title)

                supportFragmentManager.beginTransaction()
                    .add(R.id.reading_news_fcv, NewsItemListFragment(news.content))
                    .commit()
            }
        }
    }
}
