package au.sj.owl.templateproject.data

import android.content.Context
import au.sj.owl.templateproject.business.implementation.RssModel
import au.sj.owl.templateproject.business.interfaces.IRssRepository
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import au.sj.owl.templateproject.ui.home.dataholder.RssDataBase
import au.sj.owl.templateproject.utils.saxrssreader.RssReader
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.lang.Exception
import java.net.URL

class RssRepository(val context: Context) : IRssRepository {
    override fun getItem(link: String): Single<DataHolder> {
        return Single.fromCallable { RssDataBase.getInstance(context).rssDataDao().get(link) }
    }

    override fun getFeedsList(): Observable<List<String>> {
        Timber.d("jsp repo getFeedsList()")
        return Observable.just(mutableListOf( //"http://feeds.feedburner.com/TechCrunch/"
                //                "https://www.economist.com/sections/economics/rss.xml" // many images
                "https://www.popsci.com/rss-science.xml?loc=contentwell&lnk=science&dom=section-1"
                //                                             "https://www.popsci.com/rss-eastern-arsenal.xml?loc=contentwell&lnk=eastern-arsenal&dom=section-1"
                                            ))
    }

    override fun loadFeedFromWeb(feed: String): Single<List<DataHolder>> {
        Timber.d("jsp repo loadFeedFromWeb()")
        var url = URL(feed)
        return Single.fromCallable {
            //            Thread.sleep(1000)

            var itemsAll: MutableList<DataHolder> = mutableListOf()
            try {
                var feed = RssReader.read(url)
                var rssItems = feed.rssItems
                Timber.d("jsp from web loaded one feed with ${rssItems.size} items")
                rssItems.forEach {
                    itemsAll.add(DataHolder(it.link,
                                            it.title,
                                            it.pubDate.time,
                                            RssModel.getImgUrlFromDescription(
                                                    it.description),
                                            false,
                                            false))
                }
            } catch (e: Exception) {
                Timber.e("jsp no network is available")
            } finally {
                return@fromCallable itemsAll
            }
            itemsAll
        }
    }

    override fun cacheFeed(rssItems: List<DataHolder>): Single<Boolean> {
        Timber.d("jsp repo cacheFeed()")
        return Single.fromCallable {
            RssDataBase.getInstance(context).rssDataDao().saveAll(rssItems)
            return@fromCallable true
        }
    }

    override fun getCachedFeeds(): Single<List<DataHolder>> {
        Timber.d("jsp repo getCachedFeeds()")
        return Single.fromCallable { RssDataBase.getInstance(context).rssDataDao().getAll() }
    }

    override fun bookmarkItem(link: String): Single<Boolean> {
        Timber.d("jsp repo bookmarkItem()")
        return Single.fromCallable {
            val db = RssDataBase.getInstance(context).rssDataDao()
            val item = db.get(link)
            item.bookmarked = !item.bookmarked
            db.updateRssItem(item)
            return@fromCallable item.bookmarked
        }
    }

}