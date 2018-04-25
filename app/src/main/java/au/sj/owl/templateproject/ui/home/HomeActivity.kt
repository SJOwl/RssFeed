package au.sj.owl.templateproject.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.sj.owl.templateproject.R
import au.sj.owl.templateproject.R.layout
import au.sj.owl.templateproject.ui.details.DetailsActivity
import au.sj.owl.templateproject.ui.home.RSSAdapter.ViewHolder
import au.sj.owl.templateproject.utils.saxrssreader.RssReader
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.container
import kotlinx.android.synthetic.main.activity_home.tabs
import kotlinx.android.synthetic.main.fragment_tabbed.rssSwipeRefreshContainer
import kotlinx.android.synthetic.main.fragment_tabbed.rvRssItems
import timber.log.Timber
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat

class HomeActivity : AppCompatActivity(),
                     IHomeActivity {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        tabs.setViewPager(container)

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val titles: Array<String> = arrayOf("All", "Bookmarked")

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? = titles[position]
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(),
                                SwipeRefreshLayout.OnRefreshListener {

        override fun onRefresh() {
            Timber.d("jsp refresh fragment!")
            setupList()
        }

        override fun onCreateView(inflater: LayoutInflater,
                                  container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(layout.fragment_tabbed, container, false)
            return rootView
        }

        override fun onViewCreated(view: View,
                                   savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            rssSwipeRefreshContainer.setOnRefreshListener(this)

            setupList()
        }

        fun setupList() {
            var feedurl1 = "http://feeds.feedburner.com/TechCrunch/"
            var feedurl2 = "https://www.economist.com/sections/economics/rss.xml"
            var url = if (arguments!!.getInt(ARG_SECTION_NUMBER) == 1) feedurl2 else feedurl1
            getItemsFromNet(URL(url)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { items ->
                        rvRssItems.adapter = RSSAdapter(this.activity as IHomeActivity, items)
                        rvRssItems.layoutManager = LinearLayoutManager(this.activity)

                        rssSwipeRefreshContainer.isRefreshing = false
                    }
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }

            fun getItemsAll(): List<DataHolder> {
                var itemsAll: MutableList<DataHolder> = mutableListOf()
                var formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                itemsAll.add(DataHolder("https://www.economist.com/news/finance-and-economics/21740730-second-our-series-shortcomings-economics-profession-economists?fsrc=rss",
                                        "Economists still lack a proper understanding of business cycles",
                                        formatter.parse("Thu, 19 Apr 2018 14:48:19 +0000").time,
                                        "https://www.economist.com/sites/default/files/images/print-edition/20180421_FND000_0.jpg",
                                        false,
                                        false))


                itemsAll.add(DataHolder("https://www.economist.com/blogs/buttonwood/2018/04/long-term-investing?fsrc=rss",
                                        "Six precepts every investor should remember",
                                        formatter.parse("Wed, 18 Apr 2018 14:55:07 +0000").time,
                                        "https://www.economist.com/sites/default/files/20180421_BLP512.jpg",
                                        true,
                                        false))

                itemsAll.add(DataHolder("https://www.economist.com/news/business-and-finance/21740702-tale-cats-christie-and-copious-chaps-victorian-survivor?fsrc=rss",
                                        "A Victorian survivor",
                                        formatter.parse("Wed, 18 Apr 2018 15:15:32 +0000").time,
                                        "https://www.economist.com/sites/default/files/images/2018/04/articles/main/20180421_fnd001.jpg",
                                        false,
                                        false))
                return itemsAll
            }

            fun getItemsBookmarked(): List<DataHolder> {
                var itemsBookmarked: MutableList<DataHolder> = mutableListOf()
                var formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                itemsBookmarked.add(DataHolder("https://www.economist.com/news/finance-and-economics/21740730-second-our-series-shortcomings-economics-profession-economists?fsrc=rss",
                                               "Economists still lack a proper understanding of business cycles",
                                               formatter.parse("Thu, 19 Apr 2018 14:48:19 +0000").time,
                                               "https://www.economist.com/sites/default/files/images/print-edition/20180421_FND000_0.jpg",
                                               false,
                                               false))
                return itemsBookmarked
            }

            /**
             * ==========================                ==========================
             */

            fun getImgUrlDromDescription(inp: String): String {
                var regSrc = Regex("img src=\"\\S*\"")
                var regUrl = Regex("\"\\S*\"")
                val sSrc = regSrc.find(inp)?.value
                var sUrl: String? = if (sSrc != null) regUrl.find(sSrc)?.value else ""
                if (sUrl == null) sUrl = ""
                else sUrl = sUrl.replace("\"", "")
                return sUrl
            }
        }

        fun getItemsFromNet(url: URL = URL("https://www.economist.com/sections/economics/rss.xml")): Single<List<DataHolder>> {
            return Single.fromCallable {
                var itemsAll: MutableList<DataHolder> = mutableListOf()
                try {
                    var feed = RssReader.read(url)
                    var rssItems = feed.rssItems
                    Timber.d("jsp ${rssItems.size}")
                    rssItems.forEach {
                        itemsAll.add(DataHolder(it.link,
                                                it.title,
                                                it.pubDate.time,
                                                getImgUrlDromDescription(it.description),
                                                false,
                                                false))
                    }
                } catch (e: Exception) {
                    Timber.e("jsp no wifi available")
                    // todo show no internet connection
                } finally {
                    return@fromCallable itemsAll
                }
                return@fromCallable itemsAll
            }
        }
    }


    /**
     * ==========================        IHomeActivity        ==========================
     */
    override fun appContext(): Context = applicationContext

    override fun openRssDetails(dataHolder: DataHolder,
                                holder: ViewHolder) {
        val intent = Intent(this, DetailsActivity::class.java)
        val sharedElement = holder.icon
        intent.putExtra("dataHolder", dataHolder)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                                                         sharedElement,
                                                                         sharedElement.transitionName)
        startActivity(intent, options.toBundle())
    }

}
