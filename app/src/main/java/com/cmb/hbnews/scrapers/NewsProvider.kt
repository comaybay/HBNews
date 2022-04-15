package com.cmb.hbnews.scrapers

import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader

class NewsProvider {
    companion object {
        private val newsScrapers: ArrayList<INewsScraper> = arrayListOf(
            ScraperVnExpress(), ScraperVietnamnet(), ScraperThanhNien()
        )

        fun setNewsSources(newsSources: Array<NewsSource>) {
            for (newsSource in newsSources) {
                newsScrapers.add(NewsScrapperFactory.create(newsSource))
            }
        }

        fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {
            val newsHeadersList = arrayListOf(arrayListOf<NewsHeader>())
            for (scraper in newsScrapers) {
                newsHeadersList.add(scraper.getNewsHeaders(category));
            }

            val length = (newsHeadersList.map { newsHeaders -> newsHeaders.size }).maxOrNull() ?: 0;

            // trộn tin từ các nguồn khác nhau
            val newsHeadersAll = arrayListOf<NewsHeader>()
            for (i in 0..length) {
                for (newsHeaders in newsHeadersList) {
                    val newsHeader = newsHeaders.getOrNull(i)
                    if (newsHeader != null)
                        newsHeadersAll.add(newsHeader)
                }
            }

            return newsHeadersAll
        }

        fun getNewsFromUrl(source: NewsSource, url: String): News {
            return NewsScrapperFactory.create(source).getNewsFromUrl(url)
        }
    }
}

private class NewsScrapperFactory {
    companion object {
        fun create(newsSource: NewsSource) : INewsScraper {
            return when (newsSource) {
                NewsSource.VnExpress -> ScraperVnExpress()
                NewsSource.ThanhNien -> ScraperThanhNien()
                NewsSource.Vietnamnet -> ScraperVietnamnet()
            }
        }
    }
}
