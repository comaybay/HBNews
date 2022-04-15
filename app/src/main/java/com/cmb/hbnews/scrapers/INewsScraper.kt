package com.cmb.hbnews.scrapers

import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader

interface INewsScraper {
    fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader>
    fun getNewsFromUrl(url: String): News
}
