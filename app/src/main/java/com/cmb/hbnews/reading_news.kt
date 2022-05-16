package com.cmb.hbnews

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.cmb.hbnews.models.News
import com.cmb.hbnews.readingnews.NewsItemListFragment
import com.cmb.hbnews.scrapers.NewsProvider
import com.cmb.hbnews.scrapers.NewsSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
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
        val date:String = intent.getStringExtra("date").toString()
        val url = intent.getStringExtra("newsUrl").toString()
        val newsSource = intent.getSerializableExtra("newsSource") as NewsSource


        text_go_back.setText(intent.getStringExtra("goBackTitle"))

//        Picasso.get().load(newsImage)
//            .placeholder(R.drawable.ic_image_not_found)
//            .into(image_description)
        Glide.with(this).load(newsImage).into(image_description);
        title_reading.setText(tilte_reading)
        description_reading.setText(description)
        publishtime_reading.setText(date)
        val currentDateTime: String = SimpleDateFormat("HH:mm-dd/MM/yyyy").format(Date())
        val titile: String = tilte_reading.replace(".", "")
        Log.d("Replace",titile)
        if(firebaseAuth.currentUser != null) {
            userID = firebaseAuth.currentUser!!.uid
            database = FirebaseDatabase.getInstance().getReference("Saved")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(userID).child(titile).exists()) {
                        image_bookmark.setImageResource(R.drawable.ic__bookmark_72_fill);
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}

            }
            )
        }
        image_bookmark.setOnClickListener() {
            if(firebaseAuth.currentUser != null)
            {
                userID = firebaseAuth.currentUser!!.uid
                database= FirebaseDatabase.getInstance().getReference("Saved")
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(userID).child(tilte_reading).exists()) {
                            val builder = AlertDialog.Builder(this@reading_news)
                            builder.setTitle("Xóa bài viết đã lưu")
                            builder.setMessage("Bạn có chắc chắn không?")
                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                image_bookmark.setImageResource(R.drawable.icons8_bookmark_72___);
                                database.child(userID).child(titile).removeValue()
                                Toast.makeText(this@reading_news, "Đã xóa thành công", Toast.LENGTH_SHORT).show()
                            }
                            builder.setNegativeButton(android.R.string.no) { dialog, which ->

                            }
                            builder.show()
                        }
                        else {
                            val update = userData(
                                date,
                                description,
                                newsImage,
                                newsSource.toString(),
                                url,
                                titile,
                                currentDateTime,

                            )
                            database.child(userID).child(tilte_reading).setValue(update)
                            image_bookmark.setImageResource(R.drawable.ic__bookmark_72_fill);
                            Toast.makeText(this@reading_news, "Đã thêm thành công", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            }
            else
            {
                val builder = AlertDialog.Builder(this@reading_news)
                builder.setTitle("⚠️Bài viết này hay quá phải hong?")
                builder.setMessage("👉Nhưng bạn chưa tạo tài khoản, nếu bạn đã có tài khoản nhấn đăng nhập để lưu lại bài viết mình thích nhé ❤️❤️!!")
                builder.setPositiveButton("Đăng Kí") { dialog, which ->
                    startActivity(Intent(this, SignUp::class.java))
                }

                builder.setNegativeButton("Đăng Nhập") { dialog, which ->
                    startActivity(Intent(this, Login::class.java))
                }

                builder.setNeutralButton(android.R.string.no) { dialog, which ->

                }
                builder.show()
            }
        }
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
                    newsSource.toString(),
                    url,
                    titile,
                    currentDateTime,
                )
                database.child(userID).child(titile).setValue(update)
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
