package com.kumsal.kyk.Models

import android.os.Parcel
import android.os.Parcelable

class SendPostDataModel() :Parcelable {
    var post_Textcontent:String?=null
    var post_image_Url:ArrayList<String>?=null
    var post_username:String?=null
    var post_profile_imagePath:String?=null
    var post_HasImage:Boolean?=null
    var post_name_surname:String?=null
    var post_own_id:String?=null
    constructor(parcel: Parcel) : this() {
        post_Textcontent = parcel.readString()
        post_username = parcel.readString()
        post_profile_imagePath = parcel.readString()
        post_HasImage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        post_name_surname = parcel.readString()
        post_image_Url=parcel.readArrayList(ArrayList::class.java.classLoader) as? ArrayList<String>
        post_own_id=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(post_Textcontent)
        parcel.writeString(post_username)
        parcel.writeString(post_profile_imagePath)
        parcel.writeValue(post_HasImage)
        parcel.writeString(post_name_surname)
        parcel.writeArray(post_image_Url?.toArray())
        parcel.writeString(post_own_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SendPostDataModel> {
        override fun createFromParcel(parcel: Parcel): SendPostDataModel {
            return SendPostDataModel(parcel)
        }

        override fun newArray(size: Int): Array<SendPostDataModel?> {
            return arrayOfNulls(size)
        }
    }

}