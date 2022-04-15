package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.*
import com.cmb.hbnews.models.NewsItems.NewsItemImage
import com.cmb.hbnews.models.NewsItems.NewsItemText
import okhttp3.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ScraperVnExpress : INewsScraper
{
    private val client: OkHttpClient = OkHttpClient()

    override fun getNewsHeaders(category: NewsCategory) : ArrayList<NewsHeader> {
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
                    date = SimpleDateFormat("HH:mm dd/MM/yyyy").format(Date(rawNews.getLong("publish_time") * 1000)),
                    newsUrl = rawNews.getString("share_url"),
                    newsSource = NewsSource.VnExpress,
                    newsSrcLogoResource = R.drawable.ic_logo_vnexpress
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
                    date = SimpleDateFormat("HH:mm dd/MM/yyyy").format(Date(rawNews.getLong("publish_time") * 1000)),
                    imgSrc =  if (imgSrc.isEmpty()) "empty" else imgSrc,
                    newsUrl = rawNews.getString("share_url"),
                    newsSource = NewsSource.VnExpress,
                    newsSrcLogoResource = R.drawable.ic_logo_vnexpress
                )
            )
        }
        response.close();
        return newsHeaders
    }

    override fun getNewsFromUrl(url: String): News {
        val doc = Jsoup.connect(url).get()
        val news = News();
        news.title = doc.selectFirst("h1.title-detail")!!.text();
        news.description = doc.selectFirst("p.description")!!.text();
        news.author = doc.selectFirst("p > strong")!!.text();
        news.date = doc.selectFirst("span.date")!!.text();

        val articleItems = doc.select("article.fck_detail > *");

        return when (doc.body().className() == "photo-dark") {
            true -> parseNewsPhotoDark(articleItems, news)
            false -> parseNewsDefault(articleItems, news)
        }
    }

    private fun parseNewsDefault(articleItems: Elements, news: News): News {
        for (item in articleItems) {
            when (item.tagName()) {
                "p" -> news.content.add(NewsItemText(item.text(), NewsItemText.TextType.P))
                "figure" -> news.content.add(
                    NewsItemImage(
                        item.selectFirst("img")!!.attr("data-src"),
                        item.selectFirst("figcaption")!!.text()
                    )
                )
            }
        }
        return news;
    }

    /**
     * parse trường hợp đặc biệt của tin báo (tin có background màu đen)
     * VD: https://vnexpress.net/galaxy-m23-dien-thoai-5g-re-nhat-cua-samsung-4450079.html
     */
    private fun parseNewsPhotoDark(articleItems: Elements, news: News): News {
        val extractParagraph = { item: Element ->
            for (p in item.select("div.desc_cation > p")) {
                news.content.add(NewsItemText(p.text(), NewsItemText.TextType.P))
            }
        }

        for (item in articleItems) {
            when (item.classNames().first()) {
                "item_slide_show" -> {
                    for (img in item.select("img")) {
                        news.content.add(NewsItemImage(img.attr("data-src"),""))
                    }

                    extractParagraph(item);
                }

                "wrap_pic" -> {
                    for (img in item.select("img")) {
                        news.content.add(NewsItemImage(img.attr("src"),""))
                    }

                    extractParagraph(item)
                }
            }
        }
        return news;
    }
}
