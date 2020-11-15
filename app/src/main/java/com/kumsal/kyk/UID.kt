package com.kumsal.kyk

class UID {
    var uid:String
        get() = field
        set(value) { field = value }

    constructor(uid: String) {
        this.uid = uid
    }
    constructor():this("number"){

    }
    companion object{
        private lateinit var instance:UID
        fun getInstance():UID{
            if (instance==null){
                instance= UID()
            }
            return instance
        }
    }
}