package com.cmb.hbnews.scrapers

import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader

interface NewsScraper {
    fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader>
    fun getNewsFromUrl(url: String): News
}
