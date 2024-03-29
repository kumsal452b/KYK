package com.kumsal.kyk.Adapter

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.kumsal.kyk.R
import com.kumsal.kyk.IInterfaces.SliderClick
import com.kumsal.kyk.IInterfaces.imageCallback
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class SliderImagePageAdapter : PagerAdapter {
    var context: Context? = null
    var uriList: ArrayList<String>? = null
    var mlayoutInflater: LayoutInflater? = null
    var postClickItem:SliderClick?=null
    var imageLoadDoneCallback:imageCallback?=null

    fun setOnClickListener(thePostClick: SliderClick) {
        this.postClickItem = thePostClick
    }
    fun setOnCallbackListener(theCallback: imageCallback){
        this.imageLoadDoneCallback=theCallback
    }

    constructor(context: Context?, uriList: ArrayList<String>?) : super() {
        this.context = context
        this.uriList = uriList
        mlayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return uriList?.size!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        var isTrue=view == `object` as LinearLayout
        println(isTrue)
        return isTrue
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var itemView =
            mlayoutInflater?.inflate(R.layout.slider_image_for_post, container, false) as View
        var imageView = itemView.findViewById<ImageView>(R.id.slider_image_for_post_imageView)
        imageView.setOnClickListener{
            if (postClickItem!=null){
                var position=position
                if (position!= POSITION_NONE){
                    postClickItem?.SliderClickItem(position)
                }
            }
        }



        val dip = 430f
        val r: Resources = Resources.getSystem()
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        )
        Picasso.get().load(uriList?.get(position)).into(imageView,object:Callback{
            override fun onSuccess() {
                if (position==count-1){
                    if (imageLoadDoneCallback!=null){
                        imageLoadDoneCallback?.imageLoadDoneCallback()
                    }
                }
            }

            override fun onError(e: Exception?) {
                if (position==count-1){
                    if (imageLoadDoneCallback!=null){
                        imageLoadDoneCallback?.imageLoadDoneCallback()
                    }
                }
            }
        })
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}