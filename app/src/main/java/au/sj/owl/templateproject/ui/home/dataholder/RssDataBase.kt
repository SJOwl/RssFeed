package au.sj.owl.templateproject.ui.home.dataholder

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(DataHolder::class)], version = 1)
abstract class RssDataBase : RoomDatabase() {

    abstract fun rssDataDao(): RssItemsDao

    companion object {
        private var INSTANCE: RssDataBase? = null

        fun getInstance(context: Context): RssDataBase {
            if (INSTANCE == null) {
                synchronized(RssDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                                    RssDataBase::class.java,
                                                    "rss.db").build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}