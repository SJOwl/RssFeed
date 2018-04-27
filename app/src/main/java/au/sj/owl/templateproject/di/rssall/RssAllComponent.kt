package au.sj.owl.templateproject.di.rssall

import au.sj.owl.templateproject.di.detailed.DetailedRssComponent
import au.sj.owl.templateproject.di.detailed.DetailedRssModule
import au.sj.owl.templateproject.ui.home.viewpager.RssFeedsAllFragment
import au.sj.owl.templateproject.ui.home.viewpager.RssFeedsBookmarkedFragment
import dagger.Subcomponent

@Subcomponent(modules = [RssAllModule::class])
@RssAllScope
interface RssAllComponent {

    fun inject(rssFeedsAllFragment: RssFeedsAllFragment)
    fun inject(rssFeedsBookmarkedFragment: RssFeedsBookmarkedFragment)

    fun plus(detailedRssModule: DetailedRssModule): DetailedRssComponent
}