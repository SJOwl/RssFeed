package au.sj.owl.templateproject.ui.home.viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.sj.owl.templateproject.R.layout
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import au.sj.owl.templateproject.ui.home.dataholder.IRssView
import au.sj.owl.templateproject.ui.home.details.DetailsActivity
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.RSSAdapter
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.RSSAdapter.ViewHolder
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_tabbed.rssSwipeRefreshContainer
import kotlinx.android.synthetic.main.fragment_tabbed.rvRssItems
import timber.log.Timber

abstract class BaseRssFragment : Fragment(),
                                 SwipeRefreshLayout.OnRefreshListener,
                                 IRssView {

    protected var feedsDisposable: Disposable? = null

    protected var itemsList = listOf<DataHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.e("jsp lcycle ${this.javaClass.simpleName} onCreate")
        super.onCreate(savedInstanceState)
        retainInstance = true
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.e("jsp lcycle ${this.javaClass.simpleName} onCreateView")
        val rootView = inflater.inflate(layout.fragment_tabbed, container, false)
        return rootView
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("jsp lcycle ${this.javaClass.simpleName} onViewCreated")
        rssSwipeRefreshContainer.setOnRefreshListener(this)

        rvRssItems.layoutManager = LinearLayoutManager(context!!)
        rvRssItems.adapter = RSSAdapter(this, mutableListOf())
    }

    abstract fun injectDependencies()

    override fun onRefresh() {
        Timber.d("jsp lcycle ${this.javaClass.simpleName} refresh fragment!")
        resubscribeFeeds()
    }

    override fun onResume() {
        super.onResume()
        resubscribeFeeds()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onResume ")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onPause ")
    }

    abstract fun resubscribeFeeds()

    /**
     * ==========================        IRssView        ==========================
     */
    override fun appContext(): Context = activity!!.applicationContext

    override fun openRssDetails(dataHolder: DataHolder,
                                holder: ViewHolder) {
        Timber.d("jsp openRssDetails for ${holder.icon.transitionName}")
        val intent = Intent(context, DetailsActivity::class.java)
        val sharedElement = holder.icon
        intent.putExtra("dataHolder", dataHolder)
        //        intent.putExtra("link", dataHolder.link)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                                                                         sharedElement,
                                                                         sharedElement.transitionName)
        startActivity(intent, options.toBundle())
    }

}
