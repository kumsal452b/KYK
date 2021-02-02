package com.kumsal.kyk.DBModels

import android.text.TextUtils
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kumsal.kyk.interfaces.Users
import java.lang.reflect.Method
import java.util.ArrayList

class DbUsers<Model> : Users<Model>{
    var mDbFirestore:FirebaseFirestore?=null
    var theModel:Model?=null
    var theModelList:ArrayList<Model>?=null
    private var className=""

    constructor(mDbFirestore: FirebaseFirestore?,className:String) {
        this.mDbFirestore = mDbFirestore
        this.className=className
    }

    fun getElement(element: Users<Model>,collectionName:String,docName:String):Model{
        if (TextUtils.isEmpty(docName)){
            mDbFirestore.collection(collectionName).addSnapshotListener { doc, e ->
                if (e!=null){
                    Log.d("Error", e.message as String)
                    return@addSnapshotListener
                }
                
                var theModel:Class<Model>=
                for (theDoc in doc!!){
                    var theData=theDoc.toObject(Class<Model>::class.java)
                }

            }
        }
    }

    override fun getUsers(array: ArrayList<Model>) {

    }
}