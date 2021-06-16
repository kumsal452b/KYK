package com.kumsal.kyk.DBModels

import android.text.TextUtils
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kumsal.kyk.IInterfaces.GetCenterSimilar
import java.util.ArrayList

class DbUsers<Model> {

    private var theModel: Model? = null
    private var theModelList: ArrayList<Model>? = null
    private var className: Class<Model>? = null
    private var model: Model
    private var mDbFirestore: FirebaseFirestore? = null
    init {
        theModelList= ArrayList()
    }

    constructor(mDbFirestore: FirebaseFirestore?, gClass: Model) {
        this.mDbFirestore = mDbFirestore
        this.model = gClass
    }

    fun getModel(): Model {
        return model
    }

     fun readyElement(
        element: GetCenterSimilar<Model>,
        collectionName: String,
        docName: String
    ) {
        if (TextUtils.isEmpty(docName)) {
            mDbFirestore?.collection(collectionName)?.addSnapshotListener { doc, e ->
                if (e != null) {
                    Log.d("Error in Dbusers", e.message as String)
                    return@addSnapshotListener
                }
                for (theDoc in doc!!) {
                    var theData = theDoc.toObject(model!!::class.java)
                    theModelList?.add(theData)
                    println("test")
                }
                element.getUsers(theModelList!!)
            }
        }else{
            mDbFirestore?.collection(collectionName)?.document(docName)?.addSnapshotListener { doc, e ->
                if (e != null) {
                    Log.d("Error in Dbusers", e.message as String)
                    return@addSnapshotListener
                }
                    var theData = doc?.toObject(model!!::class.java)
                    theModelList?.add(theData!!)
                    element.getUsers(theModelList!!)
            }
        }
    }

    fun getElement(collectionName:String,docName: String?) :ArrayList<Model>{
        readyElement(object:GetCenterSimilar<Model>{
            override fun getUsers(array: ArrayList<Model>) {
                theModelList=array
            }
        }, collectionName, docName!!)
        return theModelList!!
    }
}