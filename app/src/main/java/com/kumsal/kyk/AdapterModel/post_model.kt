package com.kumsal.kyk.AdapterModel

import android.text.TextUtils

class post_model(ad: String, username: String, postContent: String, imageUri: String,since:String) {
    var thead = ""
    var theusername = ""
    var thePost = ""
    var theImage = ""
    var theSince = ""

    init {
        if (TextUtils.isEmpty(imageUri)){
            this.theImage="empty"
        }else{
            this.theImage=imageUri
        }
        this.theusername=username
        this.thePost=postContent
        this.theSince=since
    }
}