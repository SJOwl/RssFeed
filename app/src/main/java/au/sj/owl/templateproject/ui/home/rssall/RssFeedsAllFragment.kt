package au.sj.owl.templateproject.ui.home.rssall

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.sj.owl.templateproject.App
import au.sj.owl.templateproject.R.layout
import au.sj.owl.templateproject.di.rssall.RssAllModule
import au.sj.owl.templateproject.ui.home.IRssListPresenter
import au.sj.owl.templateproject.ui.home.RSSAdapter
import au.sj.owl.templateproject.ui.home.dataholder.IHomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tabbed.rssSwipeRefreshContainer
import kotlinx.android.synthetic.main.fragment_tabbed.rvRssItems
import timber.log.Timber
import javax.inject.Inject

class RssFeedsAllFragment : Fragment(),
                            SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var iRssAllPresenter: IRssListPresenter

    private var feedsDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.e("jsp rssAll onCreate")
        super.onCreate(savedInstanceState)
        retainInstance = true
        // injection for subcomponent
        App.get(this.activity!!.applicationContext).applicationComponent().plus(RssAllModule()).inject(this)

        // injection for component
        // DaggerRssAllComponent.builder().rssAllModule(RssAllModule()).build().inject(this)
    }

    override fun onRefresh() {
        Timber.d("jsp  rssAll refresh fragment!")
        resubscribeFeeds()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("jsp  rssAll onResume ")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("jsp  rssAll onPause ")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.e("jsp rssAll onCreateView")
        val rootView = inflater.inflate(layout.fragment_tabbed, container, false)
        return rootView
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("jsp rssAll onViewCreated")
        rssSwipeRefreshContainer.setOnRefreshListener(this)

        rvRssItems.layoutManager = LinearLayoutManager(this.activity)
        rvRssItems.adapter = RSSAdapter(this.activity as IHomeActivity, mutableListOf())
        if (feedsDisposable == null) resubscribeFeeds()
    }

    fun resubscribeFeeds() {
        Timber.d("jsp rssAll resubscribe list")
        feedsDisposable?.dispose()
        feedsDisposable = iRssAllPresenter.getFeeds()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    (rvRssItems.adapter as RSSAdapter).updateList(list)
                    Timber.e("jsp got new feed with ${list.size} items")
                    rssSwipeRefreshContainer.isRefreshing = false
                }
    }

}
