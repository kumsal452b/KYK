package com.kumsal.kyk.bottomTabs

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kumsal.kyk.AdapterModel.SliderImagePageAdapter
import com.kumsal.kyk.AdapterModel.post_adapter
import com.kumsal.kyk.AdapterModel.post_model
import com.kumsal.kyk.Globals
import com.kumsal.kyk.Internet.NetworkChangeReceiver
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.GetCenter
import com.kumsal.kyk.interfaces.PostClick
import com.kumsal.kyk.interfaces.checkInternet
import com.kumsal.voice_newspaper.DbElements
import java.time.Duration
import kotlin.collections.ArrayList


class home_fragment : Fragment(),PostClick {

    private var mUser: FirebaseUser? = null
    private var mPostDb: DatabaseReference? = null
    private var mFsPostDb: FirebaseFirestore? = null
    private var mFsAuthDb: FirebaseFirestore? = null

    //Adapter section
    lateinit var adapter: post_adapter
    lateinit var post_list: ArrayList<post_model>
    private lateinit var query: Query

    //reccler
    private lateinit var recyclerView: RecyclerView

    //Broadcast receiver
    var broadCastReceiver: BroadcastReceiver? = null

    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME="likes"
            const val COLUMN_NAME_UID = "uid"
            const val COLUMN_NAME_PID = "pid"
        }
    }

    //
//    lateinit var adapter11: FirebaseRecyclerAdapter<post_model, Post>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        //Adapter initial
        post_list = ArrayList()
        adapter = post_adapter(post_list, view.context)
        adapter.setOnClickListener(this)

        //Recycler initialze
        recyclerView = view.findViewById(R.id.fragment_home_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        //
        mPostDb = FirebaseDatabase.getInstance().getReference("Post")
        mFsPostDb = FirebaseFirestore.getInstance()
        mFsAuthDb = FirebaseFirestore.getInstance()
        mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            getPostValue()
//            recyclerView.adapter=adapter11
        }
        return view
    }

    var getUserName = ArrayList<String>()
    fun getDeniedPerson(theGetElement: GetCenter<String>) {
        mFsAuthDb?.collection("Users")?.document(mUser?.uid!!)?.get()
            ?.addOnSuccessListener { documents ->
                var blockerList = documents["blockers"]
                var blockedList = documents["blocked"]

                if (blockerList == null)
                    blockerList = ArrayList<String>()
                if (blockedList == null)
                    blockedList = ArrayList<String>()
                theGetElement.getUsers(
                    blockedList as ArrayList<String>,
                    blockerList as ArrayList<String>
                )

            }?.addOnFailureListener {
            Log.d("Home fragment", it.message!!)
        }
    }

    fun getPostValue() {
        var collectionReference = mFsPostDb?.collection("Post")?.orderBy("time",com.google.firebase.firestore.Query.Direction.DESCENDING)
        collectionReference?.get()?.addOnFailureListener(
                OnFailureListener {
                    Log.d("Home fragment", it.message!!)
                }
            )?.addOnSuccessListener { document ->
                getDeniedPerson(object : GetCenter<String> {
                    override fun getUsers(
                        blocked: java.util.ArrayList<String>,
                        blocker: java.util.ArrayList<String>
                    ) {
                        for (doc in document) {
                            var thePost = doc.toObject(post_model::class.java)
                            thePost.id = doc.id
                            println(thePost.id)
                            if (!blocked.contains(thePost.username) && !blocker.contains(thePost.username)) {
                                if (thePost.uImageThmb?.size!! > 0) {
                                    var sliderImagePageAdapter =
                                        SliderImagePageAdapter(context, thePost.uImage)
                                    thePost.slider_adapter = sliderImagePageAdapter
                                }
                                post_list.add(thePost)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                })
            }
    }

    override fun favClick(position: Int) {
        var theClickPost=post_list.get(position)
        if(MainActivity.stateOfInternet!!){
            var dataMap = HashMap<String, Any>()
            var dataMapForUser = HashMap<String, Any>()
            dataMap.put("likes", FieldValue.arrayUnion(Globals.ınstance?.uid))
            dataMapForUser.put("postOfLiked",FieldValue.arrayUnion(theClickPost.id))
            var task=mFsPostDb?.collection("Post")?.document(theClickPost.id!!)?.set(dataMap, SetOptions.merge())
            var taskForUsers=mFsPostDb?.collection("Users")?.document(Globals.ınstance?.uid!!)?.set(dataMapForUser,
                SetOptions.merge())
            task?.addOnCompleteListener { OnCompleteListener<Void>{
                if (it.isSuccessful){
                    if (taskForUsers?.isSuccessful!!){


                    }else{
                        Toast.makeText(context,getString(R.string.checkInternet),Toast.LENGTH_LONG)
                    }
                }
                else{
                    Toast.makeText(context,getString(R.string.checkInternet),Toast.LENGTH_LONG)
                }
            } }
        }else{
            var theDB=DbElements(requireContext(),1,"likes")
            var writeElement=theDB.writableDatabase
            val values = ContentValues().apply {
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_UID, Globals.ınstance?.uid)
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_PID,theClickPost.id)
            }
            writeElement.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values)
            Toast.makeText(context,getString(R.string.warnconnection),Toast.LENGTH_LONG).show()
        }
        
    }

    fun postIsClick(pid:Int):Boolean{

    }

    override fun commClick(position: Int) {

    }

    override fun expandClick(position: Int) {

    }

}