package com.kumsal.kyk.AdapterModel

import android.text.TextUtils
import com.google.firebase.Timestamp
import java.util.ArrayList

class post_model{
    var name :String?=null
    var username :String?=null
    var pc :String?=null
    var thmbImageUri :String?=null
    var time :Timestamp?=null
    var comments:ArrayList<HashMap<String,Any>>?=null
    var likes:ArrayList<String>?=null
    constructor(){}
    constructor(
        name: String?,
        username: String?,
        pc: String?,
        thmbImageUri: String?,
        time: Timestamp?,
        comments: ArrayList<HashMap<String, Any>>?,
        likes: ArrayList<String>?
    ) {
        this.name = name
        this.username = username
        this.pc = pc
        this.thmbImageUri = thmbImageUri
        this.time = time
        this.comments = comments
        this.likes = likes
    }
}