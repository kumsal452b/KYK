package com.kumsal.kyk.interfaces

interface PostClick {
    fun favClick(position:Int)
    fun commClick(position: Int)
    fun expandClick(position: Int)
    fun isPostClick(isExist:Boolean)
}