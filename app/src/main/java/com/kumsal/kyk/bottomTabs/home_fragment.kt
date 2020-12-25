package com.kumsal.kyk.bottomTabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kumsal.kyk.AdapterModel.post_adapter
import com.kumsal.kyk.AdapterModel.post_model
import com.kumsal.kyk.R
import java.util.ArrayList


class home_fragment : Fragment() {

    private var mUser: FirebaseUser? = null
    private var mPostDb: DatabaseReference?=null
    //Adapter section
    lateinit var adapter: post_adapter
    lateinit var post_list: ArrayList<post_model>

    //reccler
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_home, container, false)
        //Adapter initial
        post_list= ArrayList()
        adapter= post_adapter(post_list,view.context)

        //Recycler initialze
        recyclerView=view.findViewById(R.id.fragment_home_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(view.context)
        recyclerView.adapter=adapter

        mPostDb = FirebaseDatabase.getInstance().getReference("Post")
        mUser=FirebaseAuth.getInstance().currentUser
        if (mUser!=null){
            getPostValue()
        }
        return view
    }
    fun getPostValue(){
        mPostDb?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (a in snapshot.children){
                    for (b in a.children){
                        var thead = b.child("name").value.toString()
                        var theusername = b.child("username").value.toString()
                        var thePost = b.child("pc").value.toString()
                        var theImage =b.child("thmbImageUri").value.toString()
//                        var theSince = ""
                        var theModel=post_model(thead,theusername,thePost,"","",theImage)
                        post_list.add(theModel)
                    }
                }
                adapter=post_adapter(post_list, view?.context!!)
                adapter.notifyDataSetChanged()
                recyclerView.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}