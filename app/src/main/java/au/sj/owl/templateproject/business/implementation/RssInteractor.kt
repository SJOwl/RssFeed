package au.sj.owl.templateproject.business.implementation

import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.business.interfaces.IRssRepository
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class RssInteractor(val iRssRepository: IRssRepository) : IRssInteractor {
    companion object {
        private var instance: RssInteractor? = null

        fun getInstance(iRssRepository: IRssRepository): RssInteractor {
            if (instance == null)
                instance = RssInteractor(iRssRepository)
            return instance!!
        }
    }

    /**
     * ==========================        IRssInteractor implementation        ==========================
     */
    val feedReceivedSubject: PublishSubject<List<DataHolder>> = PublishSubject.create()
    var checkCounter = 0
    private var lastTimeLoadedFromWeb = 0L
    private val refreshThreshHold = 1000 * 60 * 10  // 10 min

    override fun getFeed(): Observable<List<DataHolder>> {
        Timber.d("jsp interactor getfeed")
        iRssRepository.getCachedFeeds()
                .subscribeOn(Schedulers.io()).subscribe { list ->
                    sendFeed(list)
                }

        // load from web only after certain time
        if (System.currentTimeMillis() - refreshThreshHold > lastTimeLoadedFromWeb) {
            lastTimeLoadedFromWeb = System.currentTimeMillis()
            iRssRepository.getFeedsList()
                    .flatMapIterable { feed -> feed } // Observable of links to feed
                    .flatMap { feed -> iRssRepository.loadFeedFromWeb(feed).toObservable() } // List<DataHolder>
                    .flatMap { list -> iRssRepository.cacheFeed(list).toObservable() }
                    .flatMap { _ -> iRssRepository.getCachedFeeds().toObservable() }
                    .subscribeOn(Schedulers.io()).subscribe { list ->
                        sendFeed(list)
                    }
        }

        return feedReceivedSubject
    }

    private fun sendFeed(list: List<DataHolder>) {
        Timber.d("jsp interactor sendfeed")
        feedReceivedSubject.onNext(list)
        //        println("jsp repository get feeds for ${checkCounter++} time. List has ${list.size} items")
    }

    private val evRssItemBookmarked: PublishSubject<List<DataHolder>> = PublishSubject.create()

    override fun getBookmarkedRssItems(): Observable<List<DataHolder>> {
        Timber.d("jsp interactor getbookmarkedrssitem")
        updateBookmarked()
        return evRssItemBookmarked
    }

    override fun bookmarkRssItem(link: String): Single<Boolean> {
        Timber.d("jsp interactor bookmarkrssitem")
        return iRssRepository.bookmarkItem(link)
                .map { bookmarked ->
                    updateBookmarked()
                    bookmarked
                }
    }

    override fun getItem(link: String): Single<DataHolder> {
        return iRssRepository.getItem(link)
    }

    /**
     * ==========================        Helpers        ==========================
     */
    fun updateBookmarked() {
        Timber.d("jsp interactor updatebookmarked")
        iRssRepository.getCachedFeeds() //.map { list -> list.filter { it.bookmarked } }
                .subscribeOn(Schedulers.io()).subscribe { list ->
                    feedReceivedSubject.onNext(list)
                }
    }
}