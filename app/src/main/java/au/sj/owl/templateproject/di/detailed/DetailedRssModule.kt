package au.sj.owl.templateproject.di.detailed

import au.sj.owl.templateproject.business.interfaces.IRssInteractor
import au.sj.owl.templateproject.ui.details.DetailedRssPresenter
import au.sj.owl.templateproject.ui.details.IDetailedRssPresenter
import dagger.Module
import dagger.Provides

@Module
class DetailedRssModule {

    @Provides
    fun provideDetailedRssPresenter(iRssInteractor: IRssInteractor): IDetailedRssPresenter =
            DetailedRssPresenter(iRssInteractor)
}