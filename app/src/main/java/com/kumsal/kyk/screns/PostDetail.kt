package com.kumsal.kyk.screns

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.kumsal.kyk.AdapterModel.SliderImagePageAdapter
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.imageCallback

open class PostDetail : AppCompatActivity(), imageCallback {
    var pagerView: ViewPager? = null
    var recyclerView: RecyclerView? = null
    var commentContent: EditText? = null
    var shareCommentBtn: Button? = null
    var sharedImages: ArrayList<String>? = null
    var adapter: SliderImagePageAdapter? = null
    var animatedLayout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        recyclerView = findViewById(R.id.activity_post_detail_recyclerView)
        pagerView = findViewById(R.id.activity_post_detail_pagerView)
        commentContent = findViewById(R.id.activity_post_detail_edttext)
        shareCommentBtn = findViewById(R.id.activity_post_detail_commentBtn)
        animatedLayout = findViewById(R.id.activity_post_detail_animationLay)
//        this.animatedLayout?.isVisible = false
        animatedLayout?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.placeholder))
        sharedImages = intent.getStringArrayListExtra("images")
        adapter = SliderImagePageAdapter(this, sharedImages)
        adapter?.setOnCallbackListener(this)
        pagerView?.adapter = adapter
    }

    override fun imageLoadDoneCallback() {
        animatedLayout?.animation?.cancel()

    }
}