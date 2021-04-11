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
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class SliderImagePageAdapter : PagerAdapter {
    var context: Context? = null
    var uriList: ArrayList<String>? = null
    var mlayoutInflater: LayoutInflater? = null

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
        Picasso.get().load(uriList?.get(position)).transform(CropCircleTransformation()).into(imageView)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}