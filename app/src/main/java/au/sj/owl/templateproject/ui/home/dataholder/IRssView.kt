package au.sj.owl.templateproject.ui.home.dataholder

import android.content.Context
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.RSSAdapter.ViewHolder

interface IRssView {
    fun appContext(): Context
    fun openRssDetails(dataHolder: DataHolder,
                       holder: ViewHolder)
}