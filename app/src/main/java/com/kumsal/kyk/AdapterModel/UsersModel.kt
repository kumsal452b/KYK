package com.kumsal.kyk.AdapterModel

import com.google.firebase.Timestamp

class UsersModel(email:String,image:String,nameSurname:String,thmbImage:String,time:Timestamp?,username:String) {
    var theEmail=""
    var theNameSurname=""
    var theUserName=""
    var theThmbImage=""
    var theTime:Timestamp?
    var theImage=""
    constructor():this("","","","",null,"")
    init {
        this.theEmail=email
        this.theImage=image
        this.theNameSurname=nameSurname
        this.theTime=time
        this.theThmbImage=thmbImage
        this.theUserName=username
    }
}