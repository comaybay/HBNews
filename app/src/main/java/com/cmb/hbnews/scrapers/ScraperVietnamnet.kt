package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader
import org.jsoup.Jsoup

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

        val doc = Jsoup.connect(pageUrl).get()
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
        TODO("Not yet implemented")
    }

}
