package com.kumsal.kyk.screns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.kumsal.kyk.AdapterModel.SliderImagePageAdapter
import com.kumsal.kyk.R

class PostDetail : AppCompatActivity() {
    var pagerView:ViewPager?=null
    var recyclerView:RecyclerView?=null
    var commentContent:EditText?=null
    var shareCommentBtn:Button?=null
    var sharedImages:ArrayList<String>?=null
    var adapter:SliderImagePageAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        sharedImages=intent.getStringArrayListExtra("images")
        adapter=SliderImagePageAdapter(this,sharedImages)
        pagerView?.adapter=adapter
    }
}