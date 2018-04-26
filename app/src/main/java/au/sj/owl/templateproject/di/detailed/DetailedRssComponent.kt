package au.sj.owl.templateproject.di.detailed

import au.sj.owl.templateproject.ui.details.DetailsActivity
import au.sj.owl.templateproject.ui.details.DetailsFragment
import dagger.Subcomponent


@Subcomponent(modules = [DetailedRssModule::class])
@DetailedRssScope
interface DetailedRssComponent {

    fun inject(detailsActivity: DetailsActivity)
    fun inject(detailsFragment: DetailsFragment)
}