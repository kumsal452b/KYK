package com.kumsal.kyk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.Models.newDataPosModel
import com.kumsal.kyk.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class imageSelected_adapter(imageList: ArrayList<newDataPosModel>) :
    RecyclerView.Adapter<imageSelected_adapter.selectedHolder>() {
    interface ItemClickListner {
        fun onItemClickListener(position: Int)
    }

    var itemClickElement: ItemClickListner? = null
    fun setItemClikElement(clickItem: ItemClickListner) {
        this.itemClickElement = clickItem
    }

    var imageList: ArrayList<newDataPosModel>? = null

    init {
        this.imageList = imageList
    }

    inner class selectedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var closeButton: CircleImageView = itemView.findViewById(R.id.image_selected_single_close)
        var imageView: ImageView = itemView.findViewById(R.id.image_selected_single_imageView);
        fun bindElement(element: newDataPosModel) {
            imageView.setImageURI(element.file)
        }

        init {
            closeButton.setOnClickListener(View.OnClickListener {
                if (itemClickElement != null) {
                    var position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        itemClickElement!!.onItemClickListener(position)
                    }
                }

            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.imageselectedsingle, parent, false)
        return selectedHolder(view)
    }

    override fun onBindViewHolder(holder: selectedHolder, position: Int) {
        var theModel = imageList?.get(position)
        holder.bindElement(theModel!!)
    }

    override fun getItemCount(): Int {
        return imageList?.size!!
    }
}