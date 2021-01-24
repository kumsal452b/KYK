package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.opengl.Visibility
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kumsal.kyk.R
import com.kumsal.kyk.animation.Animation
import com.kumsal.kyk.screns.CreatePost
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class security_adapter(
    var items: ArrayList<security_model>,
    private val context: Context?,
    private val CPElement: CreatePost?
) :
    RecyclerView.Adapter<security_adapter.secureHolder>(), Filterable {
    lateinit var mitemClickListener: OnITemClickListener
    var filerList = ArrayList<security_model>()

    init {
        filerList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): secureHolder {
        var view =
            LayoutInflater.from(parent?.context).inflate(R.layout.secure_single, parent, false)
        return secureHolder(view)
    }

    override fun getItemCount(): Int {
        return filerList.size
    }

    interface OnITemClickListener {
        fun clickCheckBox(position: Int)
    }

    fun setOnITemClickListener(clickListener: OnITemClickListener) {
        this.mitemClickListener = clickListener
    }

    inner class secureHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener {
        var imageUrl: CircleImageView = itemView.findViewById(R.id.secure_image)
        var name: TextView = itemView.findViewById(R.id.secure_name)
        var username: TextView = itemView.findViewById(R.id.secure_username)
        var overlay: View = itemView.findViewById(R.id.overLay)
        var checkBox: CheckBox = itemView.findViewById(R.id.secure_single_check_box)
        var cardView: CardView = itemView.findViewById(R.id.secure_single_cardsingle)
        fun bindElement(theModel: security_model) {
            Picasso.get().load(theModel.theimage).into(imageUrl)
            name.setText(theModel.thename)
            username.setText(theModel.theusername)
            if (!checkBox.isChecked && theModel.theisChecked){
                checkBox.isChecked=true
            }
        }

        init {
            checkBox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (mitemClickListener != null) {
                var position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mitemClickListener.clickCheckBox(items.indexOf(filerList.get(position)))
                }
            }
        }
    }

    override fun onBindViewHolder(p0: secureHolder, p1: Int) {
        var theModel = filerList.get(p1)
        if (CreatePost.isActionMode) {
//            var anim=Animation(100,p0.checkBox)
//            anim.duration=100
//            p0.checkBox.animation=anim
            p0.checkBox.visibility = View.VISIBLE

        } else {
            p0.checkBox.visibility = View.GONE
        }
        p0.bindElement(theModel)
        p0.cardView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                CPElement?.startSelection(p1)
                return true
            }
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearh = constraint.toString()
                if (charSearh.isEmpty()) {
                    filerList = items
                } else {
                    var resultList = ArrayList<security_model>()
                    for (row in items) {
                        if (row.thename?.toLowerCase(Locale.ROOT)
                                ?.contains(charSearh.toLowerCase(Locale.ROOT))!!
                        ) {
                            resultList.add(row)
                        }
                    }
                    filerList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filerList
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filerList = results?.values as ArrayList<security_model>
                notifyDataSetChanged()
            }

        }
    }
}