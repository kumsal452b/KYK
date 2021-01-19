package com.kumsal.kyk.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

open class Animation(width:Int, view: View): Animation() {
    private var width=0
    private var startWidth=0;
    private var view:View
    init {
        this.view=view
        this.width=width
        this.startWidth=view.width
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        var newWidth=startWidth+((width-startWidth)*interpolatedTime) as Int
        view.layoutParams.width=newWidth
        view.requestLayout()
        
    }

}