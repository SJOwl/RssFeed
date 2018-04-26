package au.sj.owl.templateproject.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import au.sj.owl.templateproject.R
import au.sj.owl.templateproject.ui.details.DetailsActivity
import au.sj.owl.templateproject.ui.home.RSSAdapter.ViewHolder
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import au.sj.owl.templateproject.ui.home.dataholder.IHomeActivity
import au.sj.owl.templateproject.ui.home.rssall.RssFeedsAllFragment
import au.sj.owl.templateproject.ui.home.rssbookmarked.RssFeedsBookmarkedFragment
import kotlinx.android.synthetic.main.activity_home.container
import kotlinx.android.synthetic.main.activity_home.tabs
import timber.log.Timber

class HomeActivity : AppCompatActivity(),
                     IHomeActivity {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.offscreenPageLimit = mSectionsPagerAdapter!!.count // save fragments on swipe tabs
        tabs.setViewPager(container)
        Timber.d("jsp HomeActivity onCreate")
    }

    override fun onResume() {
        super.onResume()
        restoreStateFromPrefs()
    }

    override fun onPause() {
        super.onPause()
        saveStateToPrefs()
    }

    override fun onBackPressed() {
        when (container.currentItem) {
            0 -> super.onBackPressed()
            1 -> container.currentItem = 0
        }
    }

    private fun saveStateToPrefs() {
        val prefs = getSharedPreferences("HomeActivity", Context.MODE_PRIVATE).edit()
        prefs.putInt("tab", container.currentItem)
        prefs.apply()
    }

    private fun restoreStateFromPrefs() {
        val prefs = getSharedPreferences("HomeActivity", Context.MODE_PRIVATE)
        container.currentItem = prefs.getInt("tab", 0)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val titles: Array<String> = arrayOf("All", "Bookmarked")

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0    -> RssFeedsAllFragment()
                1    -> RssFeedsBookmarkedFragment()
                else -> RssFeedsAllFragment()
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? = titles[position]
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
        //        intent.putExtra("link", dataHolder.link)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                                                         sharedElement,
                                                                         sharedElement.transitionName)
        startActivity(intent, options.toBundle())

    }

}
