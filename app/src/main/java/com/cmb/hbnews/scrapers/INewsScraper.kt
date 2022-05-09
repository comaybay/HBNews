package com.cmb.hbnews.scrapers

import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader

interface INewsScraper {
    suspend fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader>
    suspend fun getNewsFromUrl(url: String): News
}
