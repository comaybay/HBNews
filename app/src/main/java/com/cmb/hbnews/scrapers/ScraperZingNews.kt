package com.cmb.hbnews.scrapers

import com.cmb.hbnews.R
import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader
import com.cmb.hbnews.models.NewsItems.NewsItemImage
import com.cmb.hbnews.models.NewsItems.NewsItemText
import kotlinx.coroutines.yield
import org.jsoup.Jsoup

class ScraperZingNews : INewsScraper {

    override suspend fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {
        val pageUrl = when (category) {
            NewsCategory.LATEST -> "https://zingnews.vn"
            NewsCategory.CURRENT_AFFAIRS -> "https://zingnews.vn/thoi-su.html"
            NewsCategory.BUSINESS -> "https://zingnews.vn/kinh-doanh-tai-chinh.html"
            NewsCategory.SPORTS -> "https://zingnews.vn/the-thao.html"
            NewsCategory.ENTERTAINMENT -> "https://zingnews.vn/giai-tri.html"
            NewsCategory.TECHNOLOGY -> "https://zingnews.vn/cong-nghe.html"
            NewsCategory.LIFESTYLE -> "https://zingnews.vn/doi-song.html"
            NewsCategory.HEALTH -> "https://zingnews.vn/suc-khoe.html"
            NewsCategory.TRAVEL -> "https://zingnews.vn/du-lich.html"
        }

        yield()
        val doc = Jsoup.connect(pageUrl).get()

        val newsHeaderElems = when (category) {
            NewsCategory.LATEST -> doc.select("section#section-latest article.article-item")
            else -> doc.select("section#news-latest article.article-item")
        }
        val newsHeaders = arrayListOf<NewsHeader>()

        for (elem in newsHeaderElems) {
            if (elem.hasClass("type-video"))
                continue;

            val origin = "https://zingnews.vn"
            val titleElem = elem.selectFirst("p.article-title > a")!!
            val descElem = elem.selectFirst("p.article-summary")!!
            val imgElem = elem.selectFirst("p.article-thumbnail > a > img")!!
            val dateElem =elem.selectFirst("span.article-publish")!!
            val newsUrl = origin + titleElem.attr("href")

            var imgSrc = imgElem.attr("data-src")
            if (imgSrc.isEmpty()) {
                imgSrc =  imgElem.attr("src")
            }

            newsHeaders.add(
                NewsHeader(
                    title = titleElem.text(),
                    description = descElem.text(),
                    imgSrc = imgSrc,
                    date = dateElem.text(),
                    newsUrl = newsUrl,
                    newsSource = NewsSource.ZingNews,
                    newsSrcLogoResource = R.drawable.ic_logo_zingnews,
                )
            )
        }

        return newsHeaders
    }

    override suspend fun getNewsFromUrl(url: String): News {
        val doc = Jsoup.connect(url).get()
        val news = News()

        val headerElem = doc.selectFirst("header.the-article-header")!!
        news.title = headerElem.selectFirst("h1.the-article-title")!!.text()
        news.description = doc.selectFirst("p.the-article-summary")!!.text()
        news.author = doc.selectFirst("div.the-article-credit > p.author")?.text() ?:
                      doc.selectFirst("div.the-article-credit > p.source")?.text() ?:
                      ""
        news.date = headerElem.selectFirst("ul.the-article-meta")!!.text()

        val articleItems = doc.select("div.the-article-body > *")

        for (item in articleItems) {
            when (item.tagName()) {
                "table" -> {
                    if (!item.hasClass("picture"))
                        continue

                    val imgElem = item.selectFirst("img")!!;
                    val caption = item.selectFirst("td.caption");

                    var imgSrc = imgElem.attr("data-src")
                    if (imgSrc.isNullOrEmpty())
                        imgSrc = imgElem.attr("src")

                    news.content.add(NewsItemImage(imgSrc, caption?.text() ?: ""))
                }
                "p" -> news.content.add(NewsItemText(item.text(), NewsItemText.TextType.P))
                "h2" -> news.content.add(NewsItemText(item.text(), NewsItemText.TextType.H2))
            }
        }
        return news
    }
}
