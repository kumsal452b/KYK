package com.kumsal.kyk.DBModels

import com.google.firebase.firestore.FirebaseFirestore
import com.kumsal.kyk.interfaces.Users
import java.util.ArrayList

class DbUsers<Model> : Users<Model>{
    var mDbFirestore:FirebaseFirestore?=null
    var theModel:Model?=null
    var theModelList:ArrayList<Model>?=null

    constructor(mDbFirestore: FirebaseFirestore?) {
        this.mDbFirestore = mDbFirestore
    }

    fun getElement(element: Users<Model>,collectionName:String){
        mDbFirestore.collection()
    }

    override fun getUsers(array: ArrayList<Model>) {

    }
}