package com.kumsal.kyk

import android.graphics.Bitmap


class Globals internal constructor() {
    var uid: String? = null

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
