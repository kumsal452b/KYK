package com.kumsal.kyk.AdapterModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso

class postImage_adapter :RecyclerView.Adapter<post_adapter.postHolder>{
    private var imageList:ArrayList<postImage_model>?=null

    inner class postImageHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var imageView:ImageView=itemView.findViewById(R.id.post_single_image)
        fun bindElement(theEl:postImage_model){
            Picasso.get().load(theEl.imageUri).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): post_adapter.postHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.post_single_image,parent,false)
    }

    override fun onBindViewHolder(holder: post_adapter.postHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}