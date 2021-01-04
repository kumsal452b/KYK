package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ActionMode
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.mikiloz.fancyadapters.SelectableAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.util.ArrayList

class security_adapter(items:ArrayList<security_model>,private val context: Context): SelectableAdapter<security_model, security_adapter.secureHolder>(items) {

    class secureHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var imageUrl:CircleImageView
        var name:TextView
        var username:TextView

        init {
            imageUrl=itemView.findViewById(R.id.secure_image)
            name=itemView.findViewById(R.id.secure_name)
            username=itemView.findViewById(R.id.secure_username)
            itemView.setOnLongClickListener(object:View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    if ()
                    return true
                }
            })
        }

        fun bindElement(theModel:security_model){
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
        var view=LayoutInflater.from(parent?.context).inflate(R.layout.secure_single,parent,false)
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

        return  appCompatActivity.startSupportActionMode(object: AdapterActionModeCallback() {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p0?.menuInflater?.inflate(R.menu.secure_menu,p1)

                for (i in 0..p0.){

                }
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
}