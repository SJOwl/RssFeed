package au.sj.owl.templateproject.di.rssall

import android.content.Context
import au.sj.owl.templateproject.business.implementation.RssInteractor
import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.business.interfaces.IRssRepository
import au.sj.owl.templateproject.data.RssRepository
import au.sj.owl.templateproject.ui.home.IRssListPresenter
import au.sj.owl.templateproject.ui.home.RssListPresenter
import dagger.Module
import dagger.Provides

@Module
class RssAllModule {

    @Provides
    fun provideIRssRepository(context: Context): IRssRepository = RssRepository(context)

    @Provides
    //    @RssAllScope
    fun provideIRssInteractor(iRssRepository: IRssRepository): IRssInteractor = RssInteractor(
            iRssRepository)

    @Provides
    //    @RssAllScope
    fun provideIRssAllPresenter(iRssInteractor: IRssInteractor): IRssListPresenter = RssListPresenter(
            iRssInteractor)
}