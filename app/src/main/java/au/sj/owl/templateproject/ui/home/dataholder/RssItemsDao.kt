package au.sj.owl.templateproject.ui.home.dataholder

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface RssItemsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(list: List<DataHolder>)

    @Query("SELECT * from rss_feeds WHERE link = :link LIMIT 1")
    fun get(link: String): DataHolder

    @Query("SELECT * from rss_feeds")
    fun getAll(): List<DataHolder>

    @Query("SELECT * from rss_feeds WHERE bookmarked='true'")
    fun getBookmarked(): List<DataHolder>

    @Update
    fun updateRssItems(rssItems: List<DataHolder>): Int

    @Update
    fun updateRssItem(rssItem: DataHolder): Int

    @Query("DELETE from rss_feeds")
    fun deleteAll()
}