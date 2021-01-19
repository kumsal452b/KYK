package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class security_adapter(private var items: ArrayList<security_model>, private val context: Context?):
    RecyclerView.Adapter<security_adapter.secureHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): secureHolder {
        var view=LayoutInflater.from(parent?.context).inflate(R.layout.secure_single, parent, false)
        return secureHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class secureHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var imageUrl:CircleImageView = itemView.findViewById(R.id.secure_image)
        var name:TextView = itemView.findViewById(R.id.secure_name)
        var username:TextView = itemView.findViewById(R.id.secure_username)
        var overlay:View=itemView.findViewById(R.id.overLay)
        var checkBox:CheckBox=itemView.findViewById(R.id.secure_single_check_box)
        fun bindElement(theModel: security_model){
            Picasso.get().load(theModel.theimage).into(imageUrl)
            name.setText(theModel.thename)
            username.setText(theModel.theusername)
        }
    }

    override fun onBindViewHolder(p0: secureHolder, p1: Int) {
        var theModel=items.get(p1)
        p0.bindElement(theModel)
    }

}