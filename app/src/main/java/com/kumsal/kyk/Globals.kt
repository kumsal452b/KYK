package com.kumsal.kyk
import java.io.Serializable


class Globals internal constructor() :Serializable {
    var uid: String? = null
    var name: String? = null
//    var : String? = null
//    var uid: String? = null
    companion object {
        private var instance: Globals? = null
        val ınstance: Globals?
            get() {
                if (instance == null) {
                    instance = Globals()
                }
                return instance
            }
    }
}
