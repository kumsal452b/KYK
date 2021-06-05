package com.kumsal.kyk.Models

import com.google.firebase.Timestamp
import com.kumsal.kyk.AdapterModel.SliderImagePageAdapter
import kotlin.collections.ArrayList

class post_model{
    var id:String?=null
    var name :String?=null
    var username :String?=null
    var pc :String?=null
    var thmbImageUri :String?=null
    var time :Timestamp?=null
    var comments:Int?=null
    var likes:ArrayList<String>?=null
    var slider_adapter: SliderImagePageAdapter?=null
    var hasLiked:Boolean?=null
    var uImageThmb:ArrayList<String>?=null
    var uImage:ArrayList<String>?=null
    var likesCount:Int?=null
    var uid:String?=null
    constructor(){}
    constructor(
        name: String?,
        username: String?,
        pc: String?,
        thmbImageUri: String?,
        time: Timestamp?,
        comments: Int,
        likes: ArrayList<String>?,
        slider_adapter: SliderImagePageAdapter?=null,
        uImageThmb:ArrayList<String>?=null,
        uImage:ArrayList<String>?=null
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

    constructor(id: String?,uid:String?) {
        this.id = id
        this.uid=uid
    }

    constructor(hasLiked: Boolean?) {
        this.hasLiked = hasLiked
    }

    constructor(likesCount: Int?) {
        this.likesCount = likesCount
    }


}