package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.kumsal.kyk.R

class SliderImagePageAdapter : PagerAdapter {
    var context:Context?=null
    var uriList:ArrayList<Uri>?=null
    var mlayoutInflater:LayoutInflater?=null
    constructor(context: Context?, uriList: ArrayList<Uri>?) : super() {
        this.context = context
        this.uriList = uriList
        mlayoutInflater=context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return uriList?.size!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view==`object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var itemView=mlayoutInflater?.inflate(R.layout.slider_image_for_post,container,false) as View

        return super.instantiateItem(container, position)
    }
}