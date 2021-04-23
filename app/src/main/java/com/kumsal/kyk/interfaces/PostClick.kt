package com.kumsal.kyk.interfaces

import android.widget.TextView

interface PostClick {
    fun favClick(position:Int,countTextView:TextView)
    fun commClick(position: Int)
    fun expandClick(position: Int)
    fun isPostClick(isExist:Boolean,pid:String,likeList:ArrayList<String>,likedListForUser:ArrayList<String>)
}