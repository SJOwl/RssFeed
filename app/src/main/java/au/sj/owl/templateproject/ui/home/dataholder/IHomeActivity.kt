package au.sj.owl.templateproject.ui.home.dataholder

import android.content.Context
import au.sj.owl.templateproject.ui.home.RSSAdapter.ViewHolder

interface IHomeActivity {
    fun appContext(): Context
    fun openRssDetails(dataHolder: DataHolder,
                       holder: ViewHolder)
}