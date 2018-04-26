package au.sj.owl.templateproject.ui.home

import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import timber.log.Timber

class RssListPresenter(val iRssInteractor: IRssInteractor) : IRssListPresenter {
    override fun getFeedsBookmarked(): Observable<List<DataHolder>> {
        return iRssInteractor.getFeed()
                .map { list -> list.filter { it.bookmarked } }
    }

    override fun getFeeds(): Observable<List<DataHolder>> {
        Timber.d("jsp presenter getfeeds ")
        return iRssInteractor.getFeed()
                .map { list -> list.sortedByDescending { it.date } }
    }
}
