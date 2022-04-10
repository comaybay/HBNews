package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.NewsHeader
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class ScraperThanhNien {
    companion object {
        fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {

            val pageUrl = when (category) {
                NewsCategory.LATEST -> "https://thanhnien.vn/tin-24h.html"
                NewsCategory.CURRENT_AFFAIRS -> "https://thanhnien.vn/thoi-su/"
                NewsCategory.BUSINESS -> "https://thanhnien.vn/tai-chinh-kinh-doanh/"
                NewsCategory.SPORTS -> "https://thanhnien.vn/the-thao/latest.html"
                NewsCategory.ENTERTAINMENT -> "https://thanhnien.vn/giai-tri/"
                NewsCategory.TECHNOLOGY -> "https://thanhnien.vn/cong-nghe-game/tin-tuc/"
                NewsCategory.LIFESTYLE -> "https://thanhnien.vn/doi-song/"
                NewsCategory.HEALTH -> "https://thanhnien.vn/suc-khoe/"
                NewsCategory.TRAVEL -> "https://thanhnien.vn/du-lich/"
                else -> throw NotImplementedError()
            }

            val doc = Jsoup.connect(pageUrl).get()

            val newsHeadersElems = when (category) {
              NewsCategory.TECHNOLOGY -> doc.select("div.zone--timeline > article")
              else -> doc.select("div.zone--timeline > div > article")
            }

            return when (category) {
                NewsCategory.LATEST -> parseNewsHeadersLatest(newsHeadersElems)
                else -> parseNewsHeaders(newsHeadersElems)
            }
        }

        private fun parseNewsHeaders(newsHeadersElems: Elements): ArrayList<NewsHeader> {
            var newsHeaders = arrayListOf<NewsHeader>()
            for (elem in newsHeadersElems) {
                newsHeaders.add(
                    NewsHeader(
                        title = elem.selectFirst("a.story__title")!!.text(),
                        description = elem.selectFirst("div.summary")!!.text(),
                        imgSrc = elem.selectFirst("a.story__thumb > img")!!.attr("data-src"),
                        newsSrcLogoResource = R.drawable.ic_logo_thanhnien
                    )
                )
            }
            return newsHeaders
        }

        private fun parseNewsHeadersLatest(newsHeadersElems: Elements): ArrayList<NewsHeader> {
            var newsHeaders = arrayListOf<NewsHeader>()
            for (elem in newsHeadersElems) {
                var title = elem.selectFirst("a.story__title")!!.text()
                var imgElem = elem.selectFirst("a.story__thumb > img")!!
                var imageSrc = imgElem.attr("data-src")
                if (imageSrc.isEmpty())
                    imageSrc = imgElem.attr("src")

                newsHeaders.add(
                    NewsHeader(
                        title = title,
                        description = title,
                        imgSrc = elem.selectFirst("a.story__thumb > img")!!.attr("src"),
                        newsSrcLogoResource = R.drawable.ic_logo_thanhnien
                    )
                )
            }
            return newsHeaders
        }
    }
}