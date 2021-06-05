package com.kumsal.kyk.Models

import android.text.TextUtils
import com.google.firebase.Timestamp

class UsersModel {
    var theEmail: String? = null
    var theNameSurname: String? = null
    var theUserName: String? = null
    var theThmbImage: String? = null
    var theTime: Timestamp? = null
    var theImage: String? = null
    var theId:String?=null
    var blocked:ArrayList<String>?=null
    var blockers:ArrayList<String>?=null
    var postOfLiked:ArrayList<String>?=null
    constructor() {

    }

    constructor(
        theEmail: String?,
        theNameSurname: String?,
        theUserName: String?,
        theThmbImage: String?,
        theTime: Timestamp?,
        theImage: String?,
        theId:String?
    ) {
        this.theEmail = theEmail
        this.theNameSurname = theNameSurname
        this.theUserName = theUserName
        this.theThmbImage = theThmbImage
        this.theTime = theTime
        this.theImage = theImage
        this.theId=theId
    }
    constructor(
        theEmail: String?,
        theNameSurname: String?,
        theUserName: String?,
        theThmbImage: String?,
        theTime: Timestamp?,
        theImage: String?
    ) {
        this.theEmail = theEmail
        this.theNameSurname = theNameSurname
        this.theUserName = theUserName
        this.theThmbImage = theThmbImage
        this.theTime = theTime
        this.theImage = theImage
    }

    constructor(blocked: ArrayList<String>?, blockers: ArrayList<String>?) {
        this.blocked = blocked
        this.blockers = blockers
    }

    constructor(postOfLiked: ArrayList<String>?) {
        this.postOfLiked = postOfLiked
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("theEmail", theEmail!!)
        result.put("theNameSurname", theNameSurname!!)
        result.put("theImage", theImage!!)
        result.put("theThmbImage", theThmbImage!!)
        result.put("theTime", theTime!!)
        result.put("theUserName", theUserName!!)
        if (theId==null || TextUtils.isEmpty(theId))
            theId=""
        result.put("theId",theId!!)
        return result
    }

}