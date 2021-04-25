package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.graphics.PathUtils
import androidx.viewpager.widget.PagerAdapter
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.PostClick
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.CropSquareTransformation

class SliderImagePageAdapter : PagerAdapter {
    var context: Context? = null
    var uriList: ArrayList<String>? = null
    var mlayoutInflater: LayoutInflater? = null
    var postClickItem:PostClick?=null
    fun setOnClickListener(thePostClick: PostClick) {
        this.postClickItem = thePostClick
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
                    postClickItem?.imageSliderClick(position)
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
        Picasso.get().load(uriList?.get(position)).resize(px.toInt(), px.toInt()).transform(CropSquareTransformation()).into(imageView)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}