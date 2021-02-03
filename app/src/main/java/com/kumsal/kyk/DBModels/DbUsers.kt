package com.kumsal.kyk.DBModels

import android.text.TextUtils
import android.util.Log
import android.view.Display
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.reflect.TypeToken
import com.kumsal.kyk.interfaces.UserListCallback
import com.kumsal.kyk.interfaces.Users
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.ArrayList

class DbUsers<Model> : Users<Model>{
    var mDbFirestore:FirebaseFirestore?=null
    var theModel:Model?=null
    var theModelList:ArrayList<Model>?=null
    var className:Class<Model>?=null
     private var model:Model


    constructor(mDbFirestore: FirebaseFirestore?,gClass:Model) {
        this.mDbFirestore = mDbFirestore
        this.model=gClass
    }
    fun getModel():Model{
        return model
    }

    fun readyElement(element: Users<Model>,collectionName:String,docName:String):ArrayList<Model>?{
        if (TextUtils.isEmpty(docName)){
            mDbFirestore?.collection(collectionName)?.addSnapshotListener { doc, e ->
                if (e!=null){
                    Log.d("Error", e.message as String)
                    return@addSnapshotListener
                }
                for (theDoc in doc!!){
                    var theData=theDoc.toObject(model!!::class.java)
                    theModelList?.add(theData)
                }

            }
        }
        return theModelList
    }
    fun getElement(){
        readyElement(this,"Users","")
    }

    override fun getUsers(array: ArrayList<Model>) {

    }
}