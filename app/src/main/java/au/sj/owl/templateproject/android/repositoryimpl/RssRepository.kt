package au.sj.owl.templateproject.android.repositoryimpl

import android.annotation.SuppressLint
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RssRepository? = null

        fun getInstance(context: Context): RssRepository {
            if (instance == null)
                instance = RssRepository(
                        (context))
            return instance!!
        }
    }

    override fun getItem(link: String): Single<DataHolder> {
        return Single.fromCallable { RssDataBase.getInstance(context).rssDataDao().get(link) }
    }

    override fun getFeedsList(): Observable<List<String>> {
        Timber.d("jsp repo getFeedsList()")
        return Observable.just(mutableListOf("http://feeds.feedburner.com/TechCrunch",
                                             "https://lifehacker.com/rss",
                                             "http://feeds.feedburner.com/Techcrunch",
                                             "http://feeds.wired.com/wired/index",
                                             "http://feeds.nytimes.com/nyt/rss/Technology",
                                             "http://feeds.feedburner.com/time/gadgetoftheweek",
                                             "http://rss.macworld.com/macworld/feeds/main",
                                             "http://feeds.pcworld.com/pcworld/latestnews",
                                             "http://www.techworld.com/news/rss",
                                             "http://feeds.gawker.com/lifehacker/vip",
                                             "http://feeds.feedburner.com/readwriteweb",
                                             "http://www.engadget.com/rss-full.xml",
                                             "http://readwrite.com/feed/",
                                             "http://feeds.mashable.com/Mashable",
                                             "http://feeds.feedburner.com/oreilly/radar/atom",
                                             "http://brb.yahoo.com/",
                                             "http://feeds.gawker.com/gizmodo/full",
                                             "https://www.technologyreview.com/rss/"
                                            ))
    }

    private var laodFromWebCounter = 0

    override fun loadFeedFromWeb(feed: String): Single<List<DataHolder>> {
        Timber.e("jsp repo foooooooooooooooooo!!!  load from web ${laodFromWebCounter++} time")
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
            if (item != null) {
                item.bookmarked = !item.bookmarked
                db.updateRssItem(item)
                return@fromCallable item.bookmarked
            } else throw IllegalStateException("attempt to bookmark non-existed rssItem")
        }
    }

}