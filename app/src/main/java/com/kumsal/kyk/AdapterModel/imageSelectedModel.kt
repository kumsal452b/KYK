package com.kumsal.kyk.AdapterModel

import android.net.Uri

class imageSelectedModel {
    var imageUrl:Uri?=null

    constructor()
    constructor(imageUrl: Uri?) {
        this.imageUrl = imageUrl
    }
}