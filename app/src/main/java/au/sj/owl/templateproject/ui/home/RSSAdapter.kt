package au.sj.owl.templateproject.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import au.sj.owl.templateproject.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.w_rssitem.view.rssiBookmarkNo
import kotlinx.android.synthetic.main.w_rssitem.view.rssiBookmarkYes
import kotlinx.android.synthetic.main.w_rssitem.view.rssiDate
import kotlinx.android.synthetic.main.w_rssitem.view.rssiIcon
import kotlinx.android.synthetic.main.w_rssitem.view.rssiProgress
import kotlinx.android.synthetic.main.w_rssitem.view.rssiRoot
import kotlinx.android.synthetic.main.w_rssitem.view.rssiTitle
import timber.log.Timber

class DataHolder(var link: String,
                 var title: String,
                 var date: Long,
                 var imgUrl: String = "",
                 var bookmarked: Boolean = false,
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

class DiffDataCallback(var itemsOld: List<DataHolder>,
                       var itemsNew: List<DataHolder>
                      ) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int,
                                 newItemPosition: Int): Boolean {
        val itemOld = itemsOld[oldItemPosition]
        val itemNew = itemsNew[newItemPosition]
        return itemNew.link == itemOld.link
    }

    override fun areContentsTheSame(oldItemPosition: Int,
                                    newItemPosition: Int): Boolean {
        return true
    }


    override fun getOldListSize(): Int = itemsOld.size

    override fun getNewListSize(): Int = itemsNew.size
}

interface IHomeActivity {
    fun appContext(): Context
    fun openRssDetails(dataHolder: DataHolder,
                       holder: RSSAdapter.ViewHolder)
}

class RSSAdapter(var activity: IHomeActivity,
                 var items: List<DataHolder>) : RecyclerView.Adapter<RSSAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val item = items[holder.adapterPosition]

        holder.icon.visibility = View.GONE

        if (item.bookmarked) {
            holder.bookmarkNo.visibility = View.INVISIBLE
            holder.bookmarkYes.visibility = View.VISIBLE
        } else {
            holder.bookmarkNo.visibility = View.VISIBLE
            holder.bookmarkYes.visibility = View.INVISIBLE
        }

        holder.title.text = item.title

        holder.date.text = DateUtils.formatDateTime(activity.appContext(),
                                                    item.date,
                                                    DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE)

        holder.icon.transitionName = item.imgUrl

        holder.progress.visibility = View.VISIBLE

        var options = RequestOptions()
                .error(R.mipmap.ic_launcher)

        holder.root.setOnClickListener { activity.openRssDetails(item, holder) }

        if (item.imgUrl != "") {
            holder.icon.visibility = View.VISIBLE
            Glide.with(activity.appContext())
                    .load(item.imgUrl)
                    .apply(options)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?,
                                                  model: Any?,
                                                  target: Target<Drawable>?,
                                                  isFirstResource: Boolean): Boolean {
                            Timber.e("jsp load of ${items[position].imgUrl} failed")
                            holder.progress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?,
                                                     model: Any?,
                                                     target: Target<Drawable>?,
                                                     dataSource: DataSource?,
                                                     isFirstResource: Boolean): Boolean {
                            holder.progress.visibility = View.GONE
                            Timber.w("jsp loaded ${item.imgUrl} ")
                            return false
                        }

                    }).into(holder.icon)
        } else {
            holder.progress.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.w_rssitem, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var icon: ImageView = v.rssiIcon
        var bookmarkYes: ImageView = v.rssiBookmarkYes
        var bookmarkNo: ImageView = v.rssiBookmarkNo
        var title: TextView = v.rssiTitle
        var date: TextView = v.rssiDate
        var progress: ProgressBar = v.rssiProgress
        var root: View = v.rssiRoot
    }

    fun updateList(list: List<DataHolder>) {
        Timber.v("jsp updating apps list. New size: ${list.size}")

        val diffRes = DiffUtil.calculateDiff(DiffDataCallback(items, list))
        items = list
        diffRes.dispatchUpdatesTo(this)
    }
}