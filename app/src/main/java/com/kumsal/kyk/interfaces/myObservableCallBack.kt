package com.kumsal.kyk.interfaces

import android.net.Uri
import androidx.databinding.ObservableList

class myObservableCallBack: ObservableList.OnListChangedCallback<ObservableList<Uri>>() {


    override fun onItemRangeChanged(sender: ObservableList<Uri>?, positionStart: Int, itemCount: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemRangeInserted(sender: ObservableList<Uri>?, positionStart: Int, itemCount: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemRangeMoved(
        sender: ObservableList<Uri>?,
        fromPosition: Int,
        toPosition: Int,
        itemCount: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onItemRangeRemoved(sender: ObservableList<Uri>?, positionStart: Int, itemCount: Int) {
        TODO("Not yet implemented")
    }

    override fun onChanged(sender: ObservableList<Uri>?) {
        TODO("Not yet implemented")
    }
}