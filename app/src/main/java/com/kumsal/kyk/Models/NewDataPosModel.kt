package com.kumsal.kyk.Models

import android.net.Uri

class newDataPosModel {
     var dataName:String?=null
     var file:Uri?=null
     var fileUrl:String?=null
     var thmbUrl:String?=null
     var mimeType:String?=null
     var contentType:String?=null

    constructor(
        dataName: String?,
        file: Uri?,
        fileUrl: String?,
        thmbUrl: String?,
        mimeType: String?,
        contentType: String?
    ) {
        this.dataName = dataName
        this.file = file
        this.fileUrl = fileUrl
        this.thmbUrl = thmbUrl
        this.mimeType = mimeType
        this.contentType = contentType
    }
}