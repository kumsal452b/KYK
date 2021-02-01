package com.kumsal.kyk.AdapterModel

import com.google.firebase.Timestamp

class UsersModel {


    var theEmail: String? = null
    var theNameSurname: String? = null
    var theUserName: String? = null
    var theThmbImage: String? = null
    var theTime: Timestamp? = null
    var theImage: String? = null

    constructor() {

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

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("email", theEmail!!)
        result.put("name", theNameSurname!!)
        result.put("image", theImage!!)
        result.put("thmbImage", theThmbImage!!)
        result.put("time", theTime!!)
        result.put("username", theUserName!!)
        return result
    }

}