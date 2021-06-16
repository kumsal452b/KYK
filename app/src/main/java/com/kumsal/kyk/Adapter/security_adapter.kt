package com.kumsal.kyk.Adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.kumsal.kyk.Models.security_model
import com.kumsal.kyk.R
import com.kumsal.kyk.Screns.CreatePost
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

    inner class secureHolder(itemView: View) : RecyclerView.ViewHolder(itemView),CompoundButton.OnCheckedChangeListener, View.OnClickListener{
        var imageUrl: CircleImageView = itemView.findViewById(R.id.secure_image)
        var name: TextView = itemView.findViewById(R.id.secure_name)
        var username: TextView = itemView.findViewById(R.id.secure_username)
        var checkBox: LottieAnimationView = itemView.findViewById(R.id.secure_single_check_box)
        var cardView: CardView = itemView.findViewById(R.id.secure_single_cardsingle)
        fun bindElement(theModel: security_model) {
            var checkEmpty=theModel.theimage
                Picasso.get().load(checkEmpty).into(imageUrl)
            name.setText(theModel.thename)
            username.setText(theModel.theusername)
            if (theModel.theisChecked!!){
                checkBox.frame=36
            }else{
                checkBox.frame=0
            }
        }
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        }

        override fun onClick(v: View?) {
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
        p0.bindElement(theModel)
        p0.cardView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                CPElement?.startSelection(p1,p0.checkBox)
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