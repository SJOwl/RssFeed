package au.sj.owl.templateproject.business.interfaces

import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Access to all feeds: cached and not
 */
interface IRssRepository {

    /**
     * List of urls with feeds
     * Example items of list:
     * "http://feeds.feedburner.com/TechCrunch/"
     * "https://www.economist.com/sections/economics/rss.xml"
     */
    fun getFeedsList(): Observable<List<String>>

    /**
     * Load feed from web
     * @param feed: ex: "http://feeds.feedburner.com/TechCrunch/"
     */
    fun loadFeedFromWeb(feed: String): Single<List<DataHolder>>

    /**
     * Save rss items to cache. Merge new list with cached
     * fields "wasread" and "bookmarked" use from cached version
     */
    fun cacheFeed(rssItems: List<DataHolder>): Single<Boolean>

    /**
     * All feeds in cache
     */
    fun getCachedFeeds(): Single<List<DataHolder>>

    /**
     * set field "bookmarked" to @param bookmark value
     */
    fun bookmarkItem(link: String): Single<Boolean>

    fun getItem(link: String): Single<DataHolder>
}