package au.sj.owl.templateproject.ui.home.dataholder

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

@Entity(tableName = "rss_feeds")
class DataHolder(@PrimaryKey(autoGenerate = false)
                 @ColumnInfo(name = "link")
                 var link: String,
                 @ColumnInfo(name = "title")
                 var title: String,
                 @ColumnInfo(name = "date")
                 var date: Long,
                 @ColumnInfo(name = "img_url")
                 var imgUrl: String = "",
                 @ColumnInfo(name = "bookmarked")
                 var bookmarked: Boolean = false,
                 @ColumnInfo(name = "was_read")
                 var wasRead: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel,
                               flags: Int) {
        parcel.writeString(link)
        parcel.writeString(title)
        parcel.writeLong(date)
        parcel.writeString(imgUrl)
        parcel.writeByte(if (bookmarked) 1 else 0)
        parcel.writeByte(if (wasRead) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<DataHolder> {
        override fun createFromParcel(parcel: Parcel): DataHolder {
            return DataHolder(parcel)
        }

        override fun newArray(size: Int): Array<DataHolder?> {
            return arrayOfNulls(size)
        }
    }

}