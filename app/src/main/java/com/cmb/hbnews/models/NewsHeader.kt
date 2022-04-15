package com.cmb.hbnews.models

import com.cmb.hbnews.scrapers.NewsSource

class NewsHeader constructor(
    val title: String,
    val description: String,
    val imgSrc: String,
    val date: String,
    val newsUrl: String,
    val newsSource: NewsSource,
    val newsSrcLogoResource: Int,
)
