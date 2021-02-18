package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hendraanggrian.appcompat.widget.SocialEditText
import com.hendraanggrian.appcompat.widget.SocialTextView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class post_adapter(private var list: ArrayList<post_model>, private var context: Context) :
    RecyclerView.Adapter<post_adapter.postHolder>() {

    class postHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: CircleImageView = itemView.findViewById(R.id.post_layout_imageView)
        var postc: SocialTextView = itemView.findViewById(R.id.post_layout_imageView_postContent)
        var name: TextView = itemView.findViewById(R.id.post_layout_name)
        var username: TextView = itemView.findViewById(R.id.post_layout_username)
        var since: TextView = itemView.findViewById(R.id.post_layout_sinceTime)
        var expanded: ImageButton = itemView.findViewById(R.id.post_layout_expanded)

        fun BindElement(model: post_model) {
            if (!TextUtils.isEmpty(model.thmbImageUri))
//                Picasso.get().load(model.thmbImageUri).placeholder(R.drawable.persontwo).into(image)
            postc.setText(model.pc)
            name.setText(model.name)
//            since.setText(model.time!!.nanoseconds)
            username.setText(model.username)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false)
        return postHolder(view)
    }

    override fun onBindViewHolder(holder: postHolder, position: Int) {
        var theModel = list.get(position)
        holder.BindElement(theModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}