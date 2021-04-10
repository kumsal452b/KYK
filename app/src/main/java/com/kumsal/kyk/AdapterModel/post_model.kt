package com.kumsal.kyk.AdapterModel

import android.net.Uri
import android.text.TextUtils
import com.google.firebase.Timestamp
import java.util.ArrayList

class post_model{
    var name :String?=null
    var username :String?=null
    var pc :String?=null
    var thmbImageUri :String?=null
    var time :Timestamp?=null
    var comments:Int?=null
    var likes:ArrayList<String>?=null
    var slider_adapter:SliderImagePageAdapter?=null

    var uImageThmb:ArrayList<Uri>?=null
    var uImage:ArrayList<Uri>?=null

    constructor(){}
    constructor(
        name: String?,
        username: String?,
        pc: String?,
        thmbImageUri: String?,
        time: Timestamp?,
        comments: Int,
        likes: ArrayList<String>?,
        slider_adapter:SliderImagePageAdapter?=null,
        uImageThmb:ArrayList<Uri>?=null,
        uImage:ArrayList<Uri>?=null
    ) {
        this.name = name
        this.username = username
        this.pc = pc
        this.thmbImageUri = thmbImageUri
        this.time = time
        this.comments = comments
        this.likes = likes
        this.slider_adapter=slider_adapter
    }
}