package com.kumsal.kyk.AdapterModel

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class imageSelected_adapter:RecyclerView.Adapter<imageSelected_adapter.selectedHolder>() {
    var imageList:ArrayList<Uri>?=null
    constructor(imageList: ArrayList<Uri>?) : super() {
        this.imageList = imageList
    }

    inner class selectedHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var closeButton:CircleImageView=itemView.findViewById(R.id.image_selected_single_close)
        var imageView:ImageView=itemView.findViewById(R.id.image_selected_single_imageView);
        fun bindElement()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.imageselectedsingle,parent,false)
        return selectedHolder(view)
    }

    override fun onBindViewHolder(holder: selectedHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}