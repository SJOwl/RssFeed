package au.sj.owl.templateproject.ui.home

import android.graphics.drawable.Drawable
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
import au.sj.owl.templateproject.ui.home.dataholder.DataHolder
import au.sj.owl.templateproject.ui.home.dataholder.DiffDataCallback
import au.sj.owl.templateproject.ui.home.dataholder.IHomeActivity
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
                            //                            Timber.w("jsp loaded ${item.imgUrl} ")
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
        Timber.e("jsp got updating rv list; Size: ${items.size} -> ${list.size}")

        val diffRes = DiffUtil.calculateDiff(DiffDataCallback(items,
                                                              list))
        items = list
        diffRes.dispatchUpdatesTo(this)
    }
}