package com.cmb.hbnews.scrapers

import android.util.Log
import com.cmb.hbnews.models.News
import com.cmb.hbnews.models.NewsHeader
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.yield
import kotlin.coroutines.cancellation.CancellationException

class NewsProvider {
    companion object {
        private var newsScrapers: ArrayList<INewsScraper> = arrayListOf(
            ScraperVnExpress(), ScraperVietnamnet(), ScraperThanhNien()
        )

        private val changeListeners = ArrayList<(ArrayList<NewsSource>) -> Unit>()

        fun addOnChangeListener(callback: (ArrayList<NewsSource>) -> Unit) {
            changeListeners.add(callback)
        }

        fun setNewsSources(newsSources: ArrayList<NewsSource>) {
            newsScrapers = arrayListOf()

            newsSources.forEach {
                newsScrapers.add(NewsScrapperFactory.create(it))
            }

            changeListeners.forEach { it(newsSources) }

        }

        suspend fun getNewsHeaders(category: NewsCategory): ArrayList<NewsHeader> {
            val newsHeadersList = arrayListOf(arrayListOf<NewsHeader>())
            for (scraper in newsScrapers) {
                try {
                    yield()
                    newsHeadersList.add(scraper.getNewsHeaders(category));
                }
                catch (e: Exception) {
                    try {
                        yield()
                        newsHeadersList.add(scraper.getNewsHeaders(category));
                    }
                    catch (e: CancellationException) {
                        // skip
                    }
                    catch (e: Exception) {
                        Log.w("NEWSPROVIDER", e)
                    }
                }
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

        suspend fun getNewsFromUrl(source: NewsSource, url: String): News {
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
