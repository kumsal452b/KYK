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

class imageSelected_adapter(imageList: ArrayList<imageSelected_model>) :
    RecyclerView.Adapter<imageSelected_adapter.selectedHolder>() {
    var imageList: ArrayList<imageSelected_model>? = null

    init {
        this.imageList = imageList
    }

    inner class selectedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var closeButton: CircleImageView = itemView.findViewById(R.id.image_selected_single_close)
        var imageView: ImageView = itemView.findViewById(R.id.image_selected_single_imageView);
        fun bindElement(element: imageSelected_model) {
            imageView.setImageURI(element.imageUrl)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.imageselectedsingle, parent, false)
        return selectedHolder(view)
    }

    override fun onBindViewHolder(holder: selectedHolder, position: Int) {
        var theModel=imageList?.get(position)
        holder.bindElement(theModel!!)
    }

    override fun getItemCount(): Int {
        return imageList?.size!!
    }
}