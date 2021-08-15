package com.kumsal.kyk.Models

import android.text.TextUtils
import com.google.gson.JsonObject

class security_model{
    var thename:String?=null
    var theusername:String?=null
    var theimage:String?=null
    var theisChecked:Boolean?=null
    var thePersonId:String?=null
    constructor(
        thename: String,
        theusername: String,
        theimage: String,
        theisChecked: Boolean,
        thePersonId: String
    ) {

        if (TextUtils.isEmpty(theimage) || theimage==null){
            this.theimage = "empty"
        }else{
            this.theimage =theimage
        }
        this.thename=thename
        this.theusername = theusername
        this.theisChecked = theisChecked
        this.thePersonId = thePersonId
    }

}