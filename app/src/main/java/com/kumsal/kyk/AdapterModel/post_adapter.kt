package com.kumsal.kyk.AdapterModel

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class post_adapter: RecyclerView.Adapter<post_adapter.postHolder>() {

    class postHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image:CircleImageView=itemView.findViewById(R.id.post_layout_imageView)
        var postc:TextView=itemView.findViewById(R.id.post_layout_imageView_postContent)
        var name:TextView=itemView.findViewById(R.id.post_layout_name)
        var since:TextView=itemView.findViewById(R.id.post_layout_sinceTime)
        var expanded:TextView=itemView.findViewById(R.id.post_layout_expanded)

        fun BindElement(model:post_model){
            Picasso.get().load(model.theImage).placeholder(R.drawable.persontwo).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postHolder {

    }

    override fun onBindViewHolder(holder: postHolder, position: Int) {

    }

    override fun getItemCount(): Int {

    }
}