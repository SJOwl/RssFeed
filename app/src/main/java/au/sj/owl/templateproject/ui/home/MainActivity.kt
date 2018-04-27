package au.sj.owl.templateproject.ui.home

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import au.sj.owl.templateproject.R
import au.sj.owl.templateproject.ui.home.viewpager.RssFeedsAllFragment
import au.sj.owl.templateproject.ui.home.viewpager.RssFeedsBookmarkedFragment
import kotlinx.android.synthetic.main.activity_home.container
import kotlinx.android.synthetic.main.activity_home.tabs
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var currentTab = 0

    override fun onStart() {
        super.onStart()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onStart ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.offscreenPageLimit = mSectionsPagerAdapter!!.count // save fragments on swipe tabs
        tabs.setViewPager(container)
        Timber.d("jsp ${this.javaClass.simpleName} onCreate")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onResume ")
        restoreStateFromPrefs()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onPause ")
        saveStateToPrefs()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("jsp lcycle ${this.javaClass.simpleName} onDestroy ")
    }

    override fun onBackPressed() {
        when (container.currentItem) {
            0 -> super.onBackPressed()
            1 -> container.currentItem = 0
        }
    }

    private fun saveStateToPrefs() {
        val prefs = getSharedPreferences("MainActivity", Context.MODE_PRIVATE).edit()
        prefs.putInt("tab", container.currentItem)
        prefs.apply()
    }

    private fun restoreStateFromPrefs() {
        val prefs = getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
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

        var currentFragment: Fragment? = null

        override fun setPrimaryItem(container: ViewGroup,
                                    position: Int,
                                    obj: Any) {
            if (currentFragment != obj)
                currentFragment = obj as Fragment
            super.setPrimaryItem(container, position, obj)
        }
    }
}
