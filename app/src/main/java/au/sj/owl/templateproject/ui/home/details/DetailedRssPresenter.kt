package au.sj.owl.templateproject.ui.home.details

import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DetailedRssPresenter(private val iRssInteractor: IRssInteractor) : IDetailedRssPresenter {
    private var link: String = ""
    private val evBookmarked: PublishSubject<Boolean> = PublishSubject.create()

    override fun bindLink(link: String) {
        this.link = link
    }

    override fun bookmark() {
        iRssInteractor.bookmarkRssItem(link).subscribeOn(Schedulers.io())
                .subscribe { bookmarked -> evBookmarked.onNext(bookmarked) }
    }

    override fun onBookmarked(): Observable<Boolean> = evBookmarked

    override fun getRssItem(): Single<DataHolder> {
        return iRssInteractor.getItem(link)
    }

}