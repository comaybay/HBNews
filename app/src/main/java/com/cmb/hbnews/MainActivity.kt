package com.cmb.hbnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val categoryFragment = CategoryFragment()
    private val userFragment = UserFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        val transaction = supportFragmentManager.beginTransaction()
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
    }
}
