package com.cmb.hbnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.cmb.hbnews.scrapers.NewsProvider
import com.cmb.hbnews.scrapers.NewsSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_user.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val categoryFragment = CategoryFragment()
    private val userFragment = UserFragment()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.news_sources, NewsSourcesFragment(::onNewsSourcesChange))
            .add(R.id.fragment, homeFragment)
            .add(R.id.fragment, categoryFragment)
            .add(R.id.fragment, userFragment)
            .show(homeFragment)
            .hide(categoryFragment)
            .hide(userFragment)
            .commit()

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_category -> replaceFragment(categoryFragment)
                R.id.ic_person -> replaceFragment(userFragment)
            }
            true
        }
    }

    fun onNewsSourcesChange(newsSources: ArrayList<NewsSource>) {
        NewsProvider.setNewsSources(newsSources)

        val userID = firebaseAuth.currentUser?.uid
        if (userID == null)
            return;

        val user = Firebase.firestore.collection("users").document(userID)
        val data = HashMap<String, Any>()
        data["prefNewsSources"] = newsSources.map { ns -> ns.name }.joinToString(",")
        user.update(data)
    }

    private fun replaceFragment(fragment: Fragment)
    {
        if(fragment!= null)
        {
            val transaction = supportFragmentManager.beginTransaction()
            .hide(homeFragment)
            .hide(categoryFragment)
            .hide(userFragment)
            .show(fragment)
            .commit()
        }

        if (fragment == userFragment) {
            toolbar.visibility = View.GONE
            news_sources.visibility = View.GONE
        }
        else {
            toolbar.visibility = View.VISIBLE
            news_sources.visibility = View.VISIBLE
        }

        section_title.text = if (fragment == homeFragment) "Tin nổi bật" else "Tin chuyên mục"
    }
}
