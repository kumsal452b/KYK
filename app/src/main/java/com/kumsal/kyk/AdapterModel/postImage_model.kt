package com.kumsal.kyk.AdapterModel

import android.net.Uri

class postImage_model{
    private var imageUri:Uri?=null

    constructor()
    constructor(imageUri: Uri?) {
        this.imageUri = imageUri
    }
}