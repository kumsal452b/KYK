package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class SliderImagePageAdapter : PagerAdapter() {
    var context:Context?=null
    var uriList:ArrayList<Uri>?=null
    constructor(context: Context?, uriList: ArrayList<Uri>?) : super() {
        this.context = context
        this.uriList = uriList
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("Not yet implemented")
    }
}