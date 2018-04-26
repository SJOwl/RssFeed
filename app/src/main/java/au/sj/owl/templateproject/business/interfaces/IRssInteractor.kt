package au.sj.owl.templateproject.business.interfaces

import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import io.reactivex.Single

/**
 * retreive rss feed
 */
interface IRssInteractor {

    /**
     * loads feed, can be used to update list of RssItems
     * Example: load from 3 sources. Start all. When first is ready, emit it. Second ready - emit new merged list:
     * combination first and second. And etc.
     */
    fun getFeed(): Observable<List<DataHolder>>

    /**
     * All bookmarked feeds are at cache. So, load feeds from cache. When item bookmark status changed, emit new list
     */
    fun getBookmarkedRssItems(): Observable<List<DataHolder>>

    /**
     * Bookmark item. Return action success
     * @param link: this part unique for each feed. So it's a key
     */
    fun bookmarkRssItem(link: String): Single<Boolean>

    fun getItem(link: String): Single<DataHolder>

}