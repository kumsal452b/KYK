package com.kumsal.kyk.AdapterModel

import android.text.TextUtils

class security_model{
    var thename=""
    var theusername=""
    var theimage=""
    var theisChecked=false
    var thePersonId=""
    constructor():this("","","",false,"")
    init {
        if (TextUtils.isEmpty(image) || image==null){
            this.theimage="empty"
        }else{
            this.theimage=image
        }
        this.thename=name
        this.theusername=username
        this.theisChecked=isChecked
    }
}