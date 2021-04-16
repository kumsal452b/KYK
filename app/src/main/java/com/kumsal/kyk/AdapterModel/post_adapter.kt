package com.kumsal.kyk.AdapterModel

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.hendraanggrian.appcompat.widget.SocialEditText
import com.hendraanggrian.appcompat.widget.SocialTextView
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.PostClick
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class post_adapter(private var list: ArrayList<post_model>, private var context: Context) :
    RecyclerView.Adapter<post_adapter.postHolder>() {
    var thePostClick: PostClick? = null
    fun setOnClickListener(thePostClick: PostClick) {
        this.thePostClick = thePostClick
    }

    inner class postHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var image: CircleImageView = itemView.findViewById(R.id.post_layout_imageView)
        var postc: SocialTextView = itemView.findViewById(R.id.post_layout_imageView_postContent)
        var name: TextView = itemView.findViewById(R.id.post_layout_name)
        var username: TextView = itemView.findViewById(R.id.post_layout_username)
        var since: TextView = itemView.findViewById(R.id.post_layout_sinceTime)
        var expanded: ImageButton = itemView.findViewById(R.id.post_layout_expanded)
        var favorite: ShineButton = itemView.findViewById(R.id.post_layout_favorite)
        var favoriteCount: TextView = itemView.findViewById(R.id.post_layout_favorite_count)
        var commit: ImageButton = itemView.findViewById(R.id.post_layout_comment)
        var commitCount: TextView = itemView.findViewById(R.id.post_layout_comment_count)
        var pagerView:ViewPager=itemView.findViewById(R.id.post_layout_pagerView)
        var pagerItemCountForImage:TextView=itemView.findViewById(R.id.post_layout_piecesOfPicture)
        init {
            favorite.setOnClickListener(this)
            commit.setOnClickListener(this)
            expanded.setOnClickListener(this)
            pagerView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    pagerItemCountForImage.setText("${position+1}/${pagerView.adapter?.count}")
                }
                override fun onPageSelected(position: Int) {

                }

            })
        }
        fun BindElement(model: post_model) {
            if (!TextUtils.isEmpty(model.thmbImageUri))
                Picasso.get().load(model.thmbImageUri).placeholder(R.drawable.persontwo).into(image)
            postc.setText(model.pc)
            name.setText(model.name)
//            since.setText(model.time!!.nanoseconds)
            username.setText(model.username)
            if (model.slider_adapter!=null){
                pagerView.visibility=View.VISIBLE
                pagerItemCountForImage.visibility=View.VISIBLE
                pagerView.adapter=model.slider_adapter
            }else{
                pagerView.visibility=View.GONE
                pagerItemCountForImage.visibility=View.GONE
            }

        }

        override fun onClick(v: View?) {
            if (thePostClick != null) {
                var position=adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    when (v?.id){
                        favorite.id->{
                            thePostClick!!.favClick(position)
                        }
                        commit.id->{
                            thePostClick!!.commClick(position)
                        }
                        expanded.id->{
                            thePostClick!!.expandClick(position)
                        }
                    }
                }
            }
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