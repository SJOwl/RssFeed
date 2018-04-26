package au.sj.owl.templateproject

import android.app.Application
import android.content.Context
import au.sj.owl.templateproject.di.application.AppComponent
import au.sj.owl.templateproject.di.application.AppModule
import au.sj.owl.templateproject.di.application.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        //        initCanary()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    fun applicationComponent() = appComponent

    companion object {
        fun get(context: Context) = context.applicationContext as App
    }
}