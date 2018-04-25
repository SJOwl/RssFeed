package au.sj.owl.templateproject.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import au.sj.owl.templateproject.R
import au.sj.owl.templateproject.ui.home.DataHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_details.progressBar
import kotlinx.android.synthetic.main.activity_details.rssdetFab
import kotlinx.android.synthetic.main.activity_details.rssdetIcon
import kotlinx.android.synthetic.main.activity_details.rssdetPubDate
import kotlinx.android.synthetic.main.activity_details.rssdetTitle
import kotlinx.android.synthetic.main.activity_details.rssdetWebView
import kotlinx.android.synthetic.main.activity_details.toolbar
import timber.log.Timber


class DetailsActivity : AppCompatActivity() {
    /**
     * ==========================        user actions with this activity        ==========================
     */
    fun openInBrowser() {
        Timber.e("jsp open in browser!")
        val url = rssItem.link
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    fun bookmark() {
        Timber.d("jsp bookmark!")
        rssItem.bookmarked = !rssItem.bookmarked
        var d = if (rssItem.bookmarked) R.drawable.ic_bookmarkyes else R.drawable.ic_bookmarkno
        rssdetFab.setImageDrawable(resources.getDrawable(d))
    }

    /**
     * ====================================================================================================
     */

    private lateinit var rssItem: DataHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rssItem = intent.getParcelableExtra("dataHolder")

        // fab
        var d = if (rssItem.bookmarked) R.drawable.ic_bookmarkyes else R.drawable.ic_bookmarkno
        rssdetFab.setImageDrawable(resources.getDrawable(d))
        rssdetFab.setOnClickListener { bookmark() }
        // title
        title = ""
        rssdetTitle.text = rssItem.title
        // pub date
        rssdetPubDate.text = DateUtils.formatDateTime(this,
                                                      rssItem.date,
                                                      DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE)

        // content
        rssdetWebView.webViewClient = WebViewClient()
        rssdetWebView.loadUrl(rssItem.link)
        rssdetWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?,
                                           newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) progressBar.visibility = View.GONE
                else progressBar.visibility = View.VISIBLE
            }
        }

        // icon
        if (rssItem.imgUrl != "") {
            rssdetIcon.transitionName = rssItem.imgUrl
            rssdetIcon.setOnClickListener { finishAfterTransition() }
            postponeEnterTransition()
            Glide.with(this)
                    .load(rssItem.imgUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?,
                                                  model: Any?,
                                                  target: Target<Drawable>?,
                                                  isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?,
                                                     model: Any?,
                                                     target: Target<Drawable>?,
                                                     dataSource: DataSource?,
                                                     isFirstResource: Boolean): Boolean {
                            startPostponedEnterTransition()
                            return false
                        }

                    })
                    .into(rssdetIcon)
        } else {
            rssdetIcon.minimumHeight = 0
        }

    }


    fun getTextFromLink(link: String): String {
        return "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities." +
               "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities." +
               "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities." +
               "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities." +
               "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities." +
               "Note that we need use methods on the exiting fragment such as setSharedElementReturnTransition and setExitTransition. On the entering fragment, we call setSharedElementEnterTransition and setEnterTransition. Finally we need to find the instance of the shared element and then call addSharedElement(view, transitionName) as part of building the FragmentTransaction. Additional external resources for fragment-to-fragment shared element transitions include:\n" +
               "\n" +
               "Fragment Transitions Detailed Tutorial\n" +
               "Android Authority article covering the basics\n" +
               "Medium article on fragment shared element transitions\n" +
               "Useful stackoverflow post for more details\n" +
               "Sample repo with working code\n" +
               "More useful sample code\n" +
               "With that you can apply these handy transitions to fragments as well as to activities."
    }

    /**
     * ==========================        menu        ==========================
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_browser -> openInBrowser()
            else              -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
