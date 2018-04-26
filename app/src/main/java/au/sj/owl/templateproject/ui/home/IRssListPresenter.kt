package au.sj.owl.templateproject.ui.home

import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable

interface IRssListPresenter {
    fun getFeeds(): Observable<List<DataHolder>>
    fun getFeedsBookmarked(): Observable<List<DataHolder>>
}