package au.sj.owl.templateproject.di.detailed

import au.sj.owl.templateproject.ui.home.details.DetailsActivity
import dagger.Subcomponent


@Subcomponent(modules = [DetailedRssModule::class])
@DetailedRssScope
interface DetailedRssComponent {

    fun inject(detailsActivity: DetailsActivity)
}