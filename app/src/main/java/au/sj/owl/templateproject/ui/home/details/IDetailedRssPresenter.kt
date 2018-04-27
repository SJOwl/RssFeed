package au.sj.owl.templateproject.ui.home.details

import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import io.reactivex.Observable
import io.reactivex.Single

interface IDetailedRssPresenter {
    fun bindLink(link: String)
    fun getRssItem(): Single<DataHolder>
    fun bookmark()
    fun onBookmarked(): Observable<Boolean>
}