package com.kumsal.kyk.AdapterModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso

class postImage_adapter :RecyclerView.Adapter<postImage_adapter.postImageHolder>{
    private var imageList:ArrayList<postImage_model>?=null
    constructor()
    constructor(imageList: ArrayList<postImage_model>?) : super() {
        this.imageList = imageList
    }

    inner class postImageHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var imageView:ImageView=itemView.findViewById(R.id.post_single_image)
        fun bindElement(theEl:postImage_model){
            Picasso.get().load(theEl.imageUri).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postImage_adapter.postImageHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.post_single_image,parent,false)
        return postImageHolder(view)
    }

    override fun onBindViewHolder(holder: postImage_adapter.postImageHolder, position: Int) {
        holder.bindElement(imageList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return imageList?.size!!
    }
}