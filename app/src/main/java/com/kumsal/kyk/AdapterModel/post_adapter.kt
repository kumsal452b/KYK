package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class post_adapter(private var list:ArrayList<post_model>,private var context:Context): RecyclerView.Adapter<post_adapter.postHolder>() {

    class postHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image:CircleImageView=itemView.findViewById(R.id.post_layout_imageView)
        var postc:TextView=itemView.findViewById(R.id.post_layout_imageView_postContent)
        var name:TextView=itemView.findViewById(R.id.post_layout_name)
        var username:TextView=itemView.findViewById(R.id.post_layout_username)
        var since:TextView=itemView.findViewById(R.id.post_layout_sinceTime)
        var expanded:ImageButton=itemView.findViewById(R.id.post_layout_expanded)

        fun BindElement(model:post_model){
            Picasso.get().load(model.theThmbImg).placeholder(R.drawable.persontwo).into(image)
            postc.setText(model.thePost)
            name.setText(model.thead)
            since.setText(model.theSince)
            username.setText(model.theusername)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false)
        return postHolder(view)
    }

    override fun onBindViewHolder(holder: postHolder, position: Int) {
        var theModel=list.get(position)
        holder.BindElement(theModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}