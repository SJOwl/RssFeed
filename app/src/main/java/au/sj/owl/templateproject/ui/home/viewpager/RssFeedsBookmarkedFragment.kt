package au.sj.owl.templateproject.ui.home.viewpager

import au.sj.owl.templateproject.App
import au.sj.owl.templateproject.di.rssall.RssAllModule
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.IRssListPresenter
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.RSSAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tabbed.rssSwipeRefreshContainer
import kotlinx.android.synthetic.main.fragment_tabbed.rvRssItems
import timber.log.Timber
import javax.inject.Inject

class RssFeedsBookmarkedFragment : BaseRssFragment() {

    @Inject
    lateinit var iRssAllPresenter: IRssListPresenter

    override fun injectDependencies() {
        App.get(this.activity!!.applicationContext).applicationComponent().plus(RssAllModule()).inject(this)
    }

    override fun resubscribeFeeds() {
        Timber.d("jsp RssFeedsBookmarkedFragment resubscribe list")
        feedsDisposable?.dispose()
        feedsDisposable = iRssAllPresenter.getFeedsBookmarked()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    itemsList = list
                    if (rvRssItems != null) {
                        (rvRssItems.adapter as RSSAdapter).updateList(list)
                        Timber.e("jsp got new bookmarked feed with ${list.size} items")
                        rssSwipeRefreshContainer.isRefreshing = false
                    }
                }
    }
}
