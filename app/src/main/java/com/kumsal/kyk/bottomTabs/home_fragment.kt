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
import kotlin.collections.HashMap


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
        mFsAuthDb?.collection("Authentication")?.get()?.addOnSuccessListener{
            documents->
            for (doc in documents){
                var usernames=doc.data as HashMap<String,String>
                getUserName.addAll()
            }
        }?.addOnFailureListener{
            Log.d("Home fragment",it.message!!)
        }

//        addSnapshotListener { document, e ->
//            if (e!=null){
//
//                return@addSnapshotListener
//            }
//            for (the in document!!){
//                var usernames=document.documents as HashMap<String,String>
//
//            }
//        }
    }
    fun getPostValue() {
        mFsPostDb?.collection("Post")?.get()?.addOnFailureListener(
            OnFailureListener {
                Log.d("Home fragment",it.message!!)
            }
        )?.addOnSuccessListener(object:OnSuccessListener<QuerySnapshot>{
            override fun onSuccess(p0: QuerySnapshot?) {
                getDeniedPerson(object:GetCenter<String>{
                    override fun getUsers(array: ArrayList<String>) {

                    }
                })
            }
        })
        query = mPostDb as Query
//        mPostDb?.orderByChild("time")?.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (a in snapshot.children) {
//                    var abx = a.getValue(post_list::class.java)
//                    for (b in a.children) {
//                        var thead = b.child("name").value.toString()
//                        var theusername = b.child("username").value.toString()
//                        var thePost = b.child("pc").value.toString()
//                        var theImage = b.child("thmbImageUri").value.toString()
//                        var theSince = b.child("time").value.toString()
//                        var theModel =
//                            post_model(thead, theusername, thePost, "", theSince, theImage)
//                        post_list.add(theModel)
//                    }
//                }
//                post_list.sortByDescending { postModel ->
//                    postModel.theSince
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//        val option = FirebaseRecyclerOptions.Builder<post_model>()
//            .setQuery(query, post_model::class.java)
//            .setLifecycleOwner(this)
//            .build()
//
//         adapter11 = object : FirebaseRecyclerAdapter<post_model, Post>(option){
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Post {
//                var view=LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
//                return Post(view)
//            }
//
//            override fun onBindViewHolder(holder: Post, position: Int, model: post_model) {
//                holder.BindElement(option.snapshots.get(position))
//            }
//        }
//    }
//    class Post(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var image: CircleImageView = itemView.findViewById(R.id.post_layout_imageView)
//        var postc: TextView = itemView.findViewById(R.id.post_layout_imageView_postContent)
//        var name: TextView = itemView.findViewById(R.id.post_layout_name)
//        var username: TextView = itemView.findViewById(R.id.post_layout_username)
//        var since: TextView = itemView.findViewById(R.id.post_layout_sinceTime)
//        var expanded: ImageButton = itemView.findViewById(R.id.post_layout_expanded)
//
//        fun BindElement(model: post_model) {
//            Picasso.get().load(model.theThmbImg).placeholder(R.drawable.persontwo).into(image)
//            postc.setText(model.thePost)
//            name.setText(model.thead)
//            since.setText(model.theSince)
//            username.setText(model.theusername)
//        }
   }
}