package com.kumsal.kyk.Models

import android.net.Uri

class postImage_model{
     var imageUri:Uri?=null

    constructor()
    constructor(imageUri: Uri?) {
        this.imageUri = imageUri
    }
}