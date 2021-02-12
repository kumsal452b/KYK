package com.kumsal.kyk.AdapterModel

import android.text.TextUtils
import com.google.firebase.Timestamp
import java.util.ArrayList

class post_model{
    var thead :String?=null
    var theusername :String?=null
    var thePost :String?=null
    var theImage :String?=null
    var theSince :Timestamp?=null
    var theThmbImg :String?=null
    var comments:ArrayList<HashMap<String,Any>>?=null
    var likes:ArrayList<String>?=null
    constructor(){}
    constructor(
        thead: String?,
        theusername: String?,
        thePost: String?,
        theImage: String?,
        theSince: Timestamp?,
        theThmbImg: String?,
        comments: ArrayList<HashMap<String, Any>>?,
        likes: ArrayList<String>?
    ) {
        this.thead = thead
        this.theusername = theusername
        this.thePost = thePost
        this.theImage = theImage
        this.theSince = theSince
        this.theThmbImg = theThmbImg
        this.comments = comments
        this.likes = likes
    }
}