package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.models.NewsItems.NewsItemImage
import com.cmb.hbnews.models.NewsItems.NewsItemText
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class ScraperVietnamnet : INewsScraper{
    override fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {
        var pageUrl = when (category) {
            NewsCategory.LATEST -> "https://vietnamnet.vn/tin-tuc-24h"
            NewsCategory.CURRENT_AFFAIRS -> "https://vietnamnet.vn/thoi-su"
            NewsCategory.BUSINESS -> "https://vietnamnet.vn/kinh-doanh"
            NewsCategory.SPORTS -> "https://vietnamnet.vn/the-thao"
            NewsCategory.ENTERTAINMENT -> "https://vietnamnet.vn/giai-tri"
            NewsCategory.TECHNOLOGY -> "https://vietnamnet.vn/cong-nghe"
            NewsCategory.LIFESTYLE -> "https://vietnamnet.vn/doi-song"
            NewsCategory.HEALTH -> "https://vietnamnet.vn/suc-khoe"
            NewsCategory.TRAVEL -> "https://vietnamnet.vn/du-lich"
            else -> throw NotImplementedError()
        }

        var doc: Document? = null
        while (doc == null) {
            try {
                doc = Jsoup.connect(pageUrl).get()
            }
            catch (e: Exception) {
                // không hiểu sao... lâu lâu bị exception, cần phải thử lại
            }
        }
        val newsHeaderElems = doc.select("div.feature-box")
        val newsHeaders = arrayListOf<NewsHeader>()

        for (elem in newsHeaderElems) {
            val origin = "https://vietnamnet.vn"
            val titleElem = elem.selectFirst(".feature-box__content--title > a")!!
            val imgElem = elem.selectFirst("div.feature-box__image > a > img")!!

            var imgSrc = imgElem.attr("data-src")
            if (imgSrc.isEmpty())
                imgSrc = imgElem.attr("src")

            newsHeaders.add(
                NewsHeader(
                    title = titleElem.text(),
                    description =  elem.selectFirst("div.feature-box__content--desc")!!.text(),
                    imgSrc = imgSrc,
                    date = "", //vietnamnet không hiển thị ngày đăng ở đầu báo
                    newsUrl = origin + titleElem.attr("href"),
                    newsSource = NewsSource.Vietnamnet,
                    newsSrcLogoResource = R.drawable.ic_logo_vietnamnet,
                )
            )
        }

        return newsHeaders
    }

    override fun getNewsFromUrl(url: String): News {
        var doc: Document? = null
        while (doc == null) {
            try {
                doc = Jsoup.connect(url).get()
            }
            catch (e: Exception) {
                // không hiểu sao... lâu lâu bị exception, cần phải thử lại
            }
        }
        val news = News();

        news.title = doc.selectFirst("h1.newsFeature__header-title")!!.text();
        news.description = doc.selectFirst("div.newFeature__main-textBold")!!.text();
        news.author = doc.selectFirst("div.maincontent div > p:last-of-type")!!.text();
        news.date = doc.selectFirst("div.breadcrumb-box__time")!!.text();

        // lí do dùng div.maincontent div do có trường hợp đặc biệt: https://vietnamnet.vn/nhung-cong-chua-hoang-tu-thanh-thuong-dan-van-bi-soi-xet-2010386.html
        val articleItems = doc.select("div.maincontent div > *");
        for (item in articleItems) {
            when (item.tagName()) {
                "p" -> when (item.selectFirst("strong") == null) {
                    true -> news.content.add(NewsItemText(item.text(), NewsItemText.TextType.P))
                    false -> news.content.add(NewsItemText(item.text(), NewsItemText.TextType.P_BOLD
                    ))
                }
                "figure" -> {
                    if (item.hasClass("image")) {
                        news.content.add(
                            NewsItemImage(
                                item.selectFirst("img")!!.attr("src"),
                                item.selectFirst("figcaption")!!.text()
                            )
                        )
                    }
                }
            }
        }
        return news;
    }

}
