package au.sj.owl.templateproject.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val appContext: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = appContext

}