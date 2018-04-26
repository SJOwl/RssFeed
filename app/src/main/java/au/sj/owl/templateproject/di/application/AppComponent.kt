package au.sj.owl.templateproject.di.application

import au.sj.owl.templateproject.di.rssall.RssAllComponent
import au.sj.owl.templateproject.di.rssall.RssAllModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(AppModule::class)])
@Singleton
interface AppComponent {

    //    fun plus(homeComponent: HomeComponent): HomeComponent
    fun plus(rssAllModule: RssAllModule): RssAllComponent
}

