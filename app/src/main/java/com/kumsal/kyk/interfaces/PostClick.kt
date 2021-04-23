package com.kumsal.kyk.interfaces

import android.widget.TextView
import com.sackcentury.shinebuttonlib.ShineButton

interface PostClick {
    fun favClick(position:Int,countTextView:TextView,favButton:ShineButton)
    fun commClick(position: Int)
    fun expandClick(position: Int)
    fun isPostClick(isExist:Boolean,pid:String,likeList:ArrayList<String>,likedListForUser:ArrayList<String>)
}