package com.kumsal.kyk.AdapterModel

import android.net.Uri

class imageSelected_model {
    var imageUrl:Uri?=null
    constructor()
    constructor(imageUrl: Uri?) {
        this.imageUrl = imageUrl
    }
}