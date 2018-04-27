package au.sj.owl.templateproject.di.rssall

import android.content.Context
import au.sj.owl.templateproject.android.repositoryimpl.RssRepository
import au.sj.owl.templateproject.business.implementation.RssInteractor
import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.business.interfaces.IRssRepository
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.IRssListPresenter
import au.sj.owl.templateproject.ui.home.viewpager.rssitemsadapter.RssListPresenter
import dagger.Module
import dagger.Provides

@Module
class RssAllModule {

    @Provides
    fun provideIRssRepository(context: Context): IRssRepository = RssRepository.getInstance(context)

    @Provides
    //    @RssAllScope
    fun provideIRssInteractor(iRssRepository: IRssRepository): IRssInteractor {
        return RssInteractor.getInstance(iRssRepository)
    }

    @Provides
    //    @RssAllScope
    fun provideIRssAllPresenter(iRssInteractor: IRssInteractor): IRssListPresenter = RssListPresenter(
            iRssInteractor)
}