package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.models.NewsItems.NewsItemImage
import com.cmb.hbnews.models.NewsItems.NewsItemText
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class ScraperVietnamnet : INewsScraper{
    override suspend fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {
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

        yield()
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

    override suspend fun getNewsFromUrl(url: String): News {
        var doc: Document? = null
        while (doc == null) {
            try {
                yield()
                doc = Jsoup.connect(url).get()
            }
            catch (e: Exception) {
                // không hiểu sao... lâu lâu bị exception, cần phải thử lại
            }
        }
        val news = News();

        // trường hợp trên: https://vietnamnet.vn/he-lo-thi-sinh-thi-hoa-hau-an-chay-truong-tu-nam-16-tuoi-ve-dep-choi-co-vua-gioi-2011148.html
        // trường hợp dưới (báo video): https://vietnamnet.vn/trung-quan-giam-can-cap-toc-vi-k-icm-2011512.html
        val titleElem = doc.selectFirst("h1.newsFeature__header-title")
                        ?: doc.selectFirst("div.video-detail__text > h1")!!

        val descElem = doc.selectFirst("div.newFeature__main-textBold")
                        ?: doc.selectFirst("div > p.text-bold")!!;

        val authorElem = doc.selectFirst("div.maincontent div > p:last-of-type")?.selectFirst("strong")
            ?:  doc.selectFirst("div.maincontent > p:last-of-type")?.selectFirst("strong")

        news.title = titleElem.text();
        news.description = descElem.text();
        news.author = authorElem?.text() ?: "";
        news.date = doc.selectFirst("div.breadcrumb-box__time")?.text() ?: "";

        // lí do dùng div.maincontent div do có trường hợp đặc biệt: https://vietnamnet.vn/nhung-cong-chua-hoang-tu-thanh-thuong-dan-van-bi-soi-xet-2010386.html
        var articleItems = doc.select("div.maincontent div > *");

        if (articleItems.size == 0) {
            // trường hợp báo video: https://vietnamnet.vn/trung-quan-giam-can-cap-toc-vi-k-icm-2011512.html
            articleItems = doc.select("div.maincontent > *");
        }

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
