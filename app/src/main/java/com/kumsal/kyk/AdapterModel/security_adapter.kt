package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.mikiloz.fancyadapters.SuperSelectableAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

open class security_adapter(items: ArrayList<security_model>?, private val context: Context?): SuperSelectableAdapter<security_model, security_adapter.secureHolder>(
    items
) {
    constructor():this(null,null)
    companion object{
        var secureAdapter:security_adapter=security_adapter()
    }
    class secureHolder(itemView: View,isEnable):RecyclerView.ViewHolder(itemView), {
        var imageUrl:CircleImageView = itemView.findViewById(R.id.secure_image)
        var name:TextView = itemView.findViewById(R.id.secure_name)
        var username:TextView = itemView.findViewById(R.id.secure_username)
        var overlay:View=itemView.findViewById(R.id.overLay)
        init {
            itemView.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    if (secureAdapter.isActionModeEnabled){
                        secureAdapter.startDrag(this@secureHolder,layoutPosition)
                    }else{
                        return false
                    }

                }
            })
        }

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

    override fun onCreateSelectableViewHolder(parent: ViewGroup?, viewType: Int): secureHolder {
        var view=LayoutInflater.from(parent?.context).inflate(R.layout.secure_single, parent, false)
        return secureHolder(view)
    }

    override fun onItemSelected(p0: secureHolder?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemDeselected(p0: secureHolder?, p1: Int) {
        TODO("Not yet implemented")
    }


    override fun startActionMode(): ActionMode {

        var appCompatActivity=AppCompatActivity()

        return  appCompatActivity.startSupportActionMode(object : AdapterActionModeCallback() {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p0?.menuInflater?.inflate(R.menu.secure_menu, p1)


                for (holder in viewHolders) {

                    if (holder != null) {
                        holder.overlay.visibility = View.VISIBLE
                        var position = holder.layoutPosition
                        if (position != -1 && isSelected(position)) {
                            holder.overlay.setBackgroundColor(
                                ContextCompat.getColor(
                                    context, R.color.blackAlpha30
                                )
                            )
                        } else {
                            holder.overlay.setBackgroundColor(
                                ContextCompat.getColor(
                                    context, R.color.colorPrimary
                                )
                            )
                        }
                    }
                }
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onExitActionMode(p0: ActionMode?) {
                TODO("Not yet implemented")
            }
        }) as ActionMode
    }

    override fun onSelectionUpdate(p0: ActionMode?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onMove(p0: secureHolder?, p1: secureHolder?) {
        TODO("Not yet implemented")
    }

    override fun onSwipe(p0: secureHolder?, p1: Int) {
        TODO("Not yet implemented")
    }
}