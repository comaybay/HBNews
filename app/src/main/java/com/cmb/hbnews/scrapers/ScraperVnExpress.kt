package com.cmb.hbnews.scrapers

import android.util.Log
import com.cmb.hbnews.R
import com.cmb.hbnews.category.CategoryNewsListFragment
import com.cmb.hbnews.models.NewsHeader
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class ScraperVnExpress
{
    companion object {
//        private val client: OkHttpClient = OkHttpClient()

        fun getNewsHeaders(category: NewsCategory) : ArrayList<NewsHeader> {
            val client = OkHttpClient()

             var url ="https://vnexpress.net/microservice/home"
            if (category == NewsCategory.LATEST) {
                url = "https://gw.vnexpress.net/bt?site_id=1000000&category_id=1000000&showed_area=vne_topstory_beta&limit=10&data_select=article_id,article_type,title,share_url,thumbnail_url,publish_time,lead,privacy,original_cate,article_category&exclude_id=&thumb_size=300x180&thumb_quality=100&thumb_dpr=1,2&thumb_fit=crop"
            }

            var request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw IOException("Error getting news header from VnExpress")
            }

            if (category == NewsCategory.LATEST) {
               return parseNewsHeadersLatest(response);
            }

            var categoryCode = when (category) {
                NewsCategory.CURRENT_AFFAIRS -> "1001005"
                NewsCategory.BUSINESS  -> "1003159"
                NewsCategory.SPORTS  -> "1002565"
                NewsCategory.ENTERTAINMENT -> "1002691"
                NewsCategory.TECHNOLOGY -> "1002592"
                NewsCategory.LIFESTYLE -> "1002966"
                NewsCategory.HEALTH -> "1003750"
                NewsCategory.TRAVEL -> "1003231"
                else -> throw NotImplementedError()
            }

            return parseNewsHeaders(response, categoryCode)
        }

        private fun parseNewsHeaders(response: Response, categoryCode: String): ArrayList<NewsHeader>{
            val data = JSONObject(response.body!!.string())
            val rawNewsArray = data.getJSONObject("data").getJSONObject(categoryCode).getJSONArray("data")

            val newsHeaders = ArrayList<NewsHeader>();

            for (i in 0 until rawNewsArray.length() - 1) {
                val rawNews = rawNewsArray.getJSONObject(i)
                val imgSrc = rawNews.getString("thumbnail_url")
                newsHeaders.add(
                    NewsHeader(
                        title = rawNews.getString("title"),
                        description = rawNews.getString("lead"),
                        imgSrc =  if (imgSrc.isEmpty()) "empty" else imgSrc,
                        newsSrcLogoResource = R.drawable.ic_logo_vnexpress,
                        newsUrl = rawNews.getString("share_url")
                    )
                )
            }
            response.close();
            return newsHeaders
        }

        private fun parseNewsHeadersLatest(response: Response) : ArrayList<NewsHeader>{
            val data = JSONObject(response.body!!.string())
            val rawNewsArray = data.getJSONObject("data")
                                   .getJSONObject("vne_topstory_beta")
                                   .getJSONArray("data")

            val newsHeaders = ArrayList<NewsHeader>();

            for (i in 0 until rawNewsArray.length() - 1) {
                val rawNews = rawNewsArray.getJSONObject(i)
                val imgSrc = rawNews.getString("thumbnail_url")
                newsHeaders.add(
                    NewsHeader(
                        title = rawNews.getString("title"),
                        description = rawNews.getString("lead"),
                        imgSrc =  if (imgSrc.isEmpty()) "empty" else imgSrc,
                        newsSrcLogoResource = R.drawable.ic_logo_vnexpress,
                        newsUrl = rawNews.getString("share_url")
                    )
                )
            }
            response.close();
            return newsHeaders
        }

    }
}
