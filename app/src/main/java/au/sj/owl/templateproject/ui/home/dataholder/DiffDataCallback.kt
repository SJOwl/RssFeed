package au.sj.owl.templateproject.ui.home.dataholder

import android.support.v7.util.DiffUtil.Callback

class DiffDataCallback(var itemsOld: List<DataHolder>,
                       var itemsNew: List<DataHolder>
                      ) : Callback() {

    override fun areItemsTheSame(oldItemPosition: Int,
                                 newItemPosition: Int): Boolean {
        val itemOld = itemsOld[oldItemPosition]
        val itemNew = itemsNew[newItemPosition]
        return itemNew.link == itemOld.link
    }

    override fun areContentsTheSame(oldItemPosition: Int,
                                    newItemPosition: Int): Boolean {
        val iO = itemsOld[oldItemPosition]
        val iN = itemsNew[newItemPosition]
        return iO.bookmarked == iN.bookmarked && iO.wasRead == iN.wasRead
    }


    override fun getOldListSize(): Int = itemsOld.size

    override fun getNewListSize(): Int = itemsNew.size
}