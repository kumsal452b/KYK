package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.opengl.Visibility
import android.view.*
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.kumsal.kyk.animation.Animation
import com.kumsal.kyk.screns.CreatePost
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class security_adapter(private var items: ArrayList<security_model>, private val context: Context?, private val CPElement: CreatePost?):
    RecyclerView.Adapter<security_adapter.secureHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): secureHolder {
        var view=LayoutInflater.from(parent?.context).inflate(R.layout.secure_single, parent, false)
        return secureHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnITemClickListener{
        fun clickCheckBox(position:Int)
    }
    lateinit var mitemClickListener:OnITemClickListener

    fun setOnITemClickListener(clickListener: OnITemClickListener){
        this.mitemClickListener=clickListener
    }
    inner class secureHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageUrl:CircleImageView = itemView.findViewById(R.id.secure_image)
        var name:TextView = itemView.findViewById(R.id.secure_name)
        var username:TextView = itemView.findViewById(R.id.secure_username)
        var overlay:View=itemView.findViewById(R.id.overLay)
        var checkBox:CheckBox=itemView.findViewById(R.id.secure_single_check_box)
        var cardView:CardView=itemView.findViewById(R.id.secure_single_cardsingle)
        fun bindElement(theModel: security_model){
            Picasso.get().load(theModel.theimage).into(imageUrl)
            name.setText(theModel.thename)
            username.setText(theModel.theusername)

        }
        init {
            checkBox.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (mitemClickListener!=null){
                var position=adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    mitemClickListener.clickCheckBox(position)
                }
            }
        }
    }

    override fun onBindViewHolder(p0: secureHolder, p1: Int) {
        var theModel=items.get(p1)
        if (CreatePost.isActionMode as Boolean){
//            var anim=Animation(100,p0.checkBox)
//            anim.duration=100
//            p0.checkBox.animation=anim
            p0.checkBox.visibility=View.VISIBLE

        }else{
//            var anim=Animation(0,p0.checkBox)
//            anim.duration=100
//            p0.checkBox.animation=anim

            p0.checkBox.visibility=View.INVISIBLE

        }
        p0.bindElement(theModel)
        p0.cardView.setOnLongClickListener(object:View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                CPElement?.startSelection(p1)
                return true
            }
        })
    }

}