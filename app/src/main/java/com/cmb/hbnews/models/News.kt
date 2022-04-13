package com.cmb.hbnews.models

import com.cmb.hbnews.models.NewsItems.NewsItem

class News {
    lateinit var title: String;
    lateinit var description: String;
    lateinit var author: String;
    lateinit var date: String;
    var content: ArrayList<NewsItem> = arrayListOf();
}
