package com.kumsal.kyk.bottomTabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kumsal.kyk.AdapterModel.post_adapter
import com.kumsal.kyk.AdapterModel.post_model
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.GetCenter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors
import kotlin.collections.HashMap
import kotlin.reflect.typeOf


class home_fragment : Fragment(){

    private var mUser: FirebaseUser? = null
    private var mPostDb: DatabaseReference?=null
    private var mFsPostDb:FirebaseFirestore?=null
    private var mFsAuthDb:FirebaseFirestore?=null
    //Adapter section
    lateinit var adapter: post_adapter
    lateinit var post_list: ArrayList<post_model>
    private lateinit var query:Query
    //reccler
    private lateinit var recyclerView: RecyclerView
//
//    lateinit var adapter11: FirebaseRecyclerAdapter<post_model, Post>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_home, container, false)
        //Adapter initial
        post_list= ArrayList()
        adapter= post_adapter(post_list, view.context)

        //Recycler initialze
        recyclerView=view.findViewById(R.id.fragment_home_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(view.context)
        recyclerView.adapter=adapter

        //
        mPostDb = FirebaseDatabase.getInstance().getReference("Post")
        mFsPostDb= FirebaseFirestore.getInstance()
        mFsAuthDb= FirebaseFirestore.getInstance()
        mUser=FirebaseAuth.getInstance().currentUser
        if (mUser!=null){
            getPostValue()
//            recyclerView.adapter=adapter11
        }
        return view
    }
    var getUserName=ArrayList<String>()
    fun getDeniedPerson(theGetElement:GetCenter<String>){
        mFsAuthDb?.collection("Users")?.document(mUser?.uid!!)?.get()?.addOnSuccessListener{
            documents->
            var blockerList=documents["blocked"]
            if (blockerList==null)
                blockerList=ArrayList<String>()
            theGetElement.getUsers(blockerList as ArrayList<String>)

        }?.addOnFailureListener{
            Log.d("Home fragment",it.message!!)
        }
    }
    fun getPostValue() {
        mFsPostDb?.collection("Post")?.get()?.addOnFailureListener(
            OnFailureListener {
                Log.d("Home fragment",it.message!!)
            }
        )?.addOnSuccessListener{document->
            getDeniedPerson(object : GetCenter<String> {
                override fun getUsers(array: ArrayList<String>) {
                    for (doc in document) {

                        var thePost = doc.toObject(post_model::class.java)
                        if (!array.contains(thePost.username)) {
                            post_list.add(thePost)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }
        query = mPostDb as Query
//
   }
}