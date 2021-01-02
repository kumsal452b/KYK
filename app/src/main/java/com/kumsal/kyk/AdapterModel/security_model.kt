package com.kumsal.kyk.AdapterModel

import android.text.TextUtils

class security_model(name:String, username:String,image:String) {
    var thename=""
    var theusername=""
    var theimage=""
    init {
        if (TextUtils.isEmpty(image) || image==null){
            this.theimage="empty"
        }else{
            this.theimage=image
        }
        this.thename=name
        this.theusername=username
    }
}