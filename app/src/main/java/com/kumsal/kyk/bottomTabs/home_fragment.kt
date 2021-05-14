package com.kumsal.kyk.bottomTabs

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.number.IntegerWidth
import android.media.Image
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.Query
import com.google.firebase.firestore.*
import com.kumsal.kyk.AdapterModel.*
import com.kumsal.kyk.Globals
import com.kumsal.kyk.Internet.NetworkChangeReceiver
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.GetCenter
import com.kumsal.kyk.interfaces.PostClick
import com.kumsal.kyk.interfaces.checkInternet
import com.kumsal.kyk.screns.PostDetail
import com.kumsal.voice_newspaper.DbElements
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader
import java.io.InputStream
import java.time.Duration
import kotlin.collections.ArrayList


class home_fragment : Fragment(), PostClick {

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
            const val TABLE_NAME = "likes"
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
        var recyclerViewAnimator:RecyclerView.ItemAnimator=DefaultItemAnimator()
        recyclerViewAnimator.addDuration=1000
        recyclerViewAnimator.removeDuration=1000
        recyclerViewAnimator.changeDuration=1000
        recyclerView.itemAnimator=recyclerViewAnimator

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
                mFsPostDb?.collection("Users")?.document(Globals.ınstance?.uid!!)?.get()
                    ?.addOnSuccessListener { documentForLiked ->

                        var useLinkList = documentForLiked["postOfLiked"]
                        var blockerList = documents["blockers"]
                        var blockedList = documents["blocked"]

                        if (blockerList == null)
                            blockerList = ArrayList<String>()
                        if (blockedList == null)
                            blockedList = ArrayList<String>()
                        if (useLinkList == null)
                            useLinkList = ArrayList<String>()
                        theGetElement.getUsers(
                            blockedList as ArrayList<String>,
                            blockerList as ArrayList<String>,
                            useLinkList as ArrayList<String>
                        )
                    }?.addOnFailureListener { exp ->
                        var useLinkList = ArrayList<String>()
                        var blockerList = documents["blockers"]
                        var blockedList = documents["blocked"]

                        if (blockerList == null)
                            blockerList = ArrayList<String>()
                        if (blockedList == null)
                            blockedList = ArrayList<String>()
                        if (useLinkList == null)
                            useLinkList = ArrayList<String>()
                        theGetElement.getUsers(
                            blockedList as ArrayList<String>,
                            blockerList as ArrayList<String>,
                            useLinkList
                        )
                    }
            }?.addOnFailureListener {
                Log.d("Home fragment", it.message!!)
            }
    }

    fun getPostValue() {
        var collectionReference = mFsPostDb?.collection("Post")
            ?.orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
        collectionReference?.get()?.addOnFailureListener(
            OnFailureListener {
                Log.d("Home fragment", it.message!!)
            }
        )?.addOnSuccessListener { document ->
            getDeniedPerson(object : GetCenter<String> {
                override fun getUsers(
                    blocked: java.util.ArrayList<String>,
                    blocker: java.util.ArrayList<String>,
                    currentUserLikeList: java.util.ArrayList<String>
                ) {
                    for (doc in document) {
                        var thePost = doc.toObject(post_model::class.java)

                        thePost.id = doc.id
                        println(thePost.id)
                        if (!blocked.contains(thePost.username) && !blocker.contains(thePost.username)) {
                            if (thePost.uImageThmb?.size!! > 0) {
                                var sliderImagePageAdapter =
                                    SliderImagePageAdapter(context, thePost.uImageThmb)
                                thePost.slider_adapter = sliderImagePageAdapter
                            }
                            thePost.hasLiked = currentUserLikeList.contains(thePost.id)
                            thePost.likesCount = thePost.likes?.size
                            post_list.add(thePost)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

    override fun favClick(position: Int,countTextView: TextView,favButton: ShineButton) {
        var theClickPost = post_list.get(position)
        if (MainActivity.stateOfInternet!!) {
            isPostClick(theClickPost.id!!, this,theClickPost.likes!!)
            var text=countTextView.text.toString()
            var count=Integer.valueOf(text)
            if(favButton.color==Color.RED){
                favButton.setBtnFillColor(Color.GRAY)
                count--
            }  else {
                favButton.setBtnFillColor(Color.RED)
                count++
            }
            countTextView.setText(count.toString())
        } else {
            var theDB = DbElements(requireContext(), 1, "likes")
            var writeElement = theDB.writableDatabase
            val values = ContentValues().apply {
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_UID, Globals.ınstance?.uid)
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_PID, theClickPost.id)
            }
            writeElement.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
            Toast.makeText(context, getString(R.string.warnconnection), Toast.LENGTH_LONG).show()
        }
    }

    override fun isPostClick(isExist: Boolean, pid: String, likeList: ArrayList<String>,likedListForUser:ArrayList<String>) {
        if (!isExist) {
            var dataMap = HashMap<String, Any>()
            var dataMapForUser = HashMap<String, Any>()
            dataMap.put("likes", FieldValue.arrayUnion(Globals.ınstance?.uid))
            dataMapForUser.put("postOfLiked", FieldValue.arrayUnion(pid))
            var task =
                mFsPostDb?.collection("Post")?.document(pid!!)?.set(dataMap, SetOptions.merge())
            var taskForUsers =
                mFsPostDb?.collection("Users")?.document(Globals.ınstance?.uid!!)?.set(
                    dataMapForUser,
                    SetOptions.merge()
                )
            task?.addOnCompleteListener {
                OnCompleteListener<Void> {
                    if (it.isSuccessful) {
                        if (taskForUsers?.isSuccessful!!) {
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.checkInternet),
                                Toast.LENGTH_LONG
                            )
                        }
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.checkInternet),
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        } else {
            var theLikeList = ArrayList<String>()
            var theLikeListForPost=ArrayList<String>()

            theLikeList.addAll(likeList)
            theLikeListForPost.addAll(likedListForUser)

            theLikeList.remove(pid)
            mFsPostDb?.collection("Users")?.document(Globals.ınstance?.uid!!)?.update("postOfLiked", theLikeList)

            theLikeListForPost.remove(Globals.ınstance?.uid)
            mFsAuthDb?.collection("Post")?.document(pid)?.update("likes",theLikeListForPost)
        }

    }

    fun isPostClick(pid: String, theExistPostCheck: PostClick,likedListForUser:ArrayList<String>) {
        mFsPostDb?.collection("Users")?.document(Globals.ınstance?.uid!!)?.get()
            ?.addOnFailureListener(OnFailureListener {
                FirebaseFirestoreException.Code.CANCELLED
                Log.d("Error in fav element", it.localizedMessage, it.fillInStackTrace())
            })?.addOnCompleteListener {
            var thePost = it.result?.toObject(UsersModel::class.java)
            if (thePost?.postOfLiked?.contains(pid)!!) {
                theExistPostCheck.isPostClick(true, pid, thePost.postOfLiked!!,likedListForUser)
            } else {
                theExistPostCheck.isPostClick(false, pid, thePost.postOfLiked!!,likedListForUser)
            }
        }
    }

    override fun commClick(position: Int) {
        var theClickPost = post_list.get(position)
        var forPostDetailIntent=Intent(context,PostDetail::class.java)
        var hasImage=(theClickPost.uImageThmb?.size!=0 && theClickPost.uImageThmb!=null)
        var theSendPostModel=SendPostDataModel()

        theSendPostModel.post_image_Url=theClickPost.uImageThmb
        theSendPostModel.post_HasImage=hasImage
        theSendPostModel.post_Textcontent=theClickPost.pc
        theSendPostModel.post_profile_imagePath=theClickPost.thmbImageUri
        theSendPostModel.post_name_surname=theClickPost.name
        theSendPostModel.post_username=theClickPost.username
        theSendPostModel.post_own_id=theClickPost.uid

        forPostDetailIntent.putExtra("images",theSendPostModel)
        startActivity(forPostDetailIntent)
    }

    override fun expandClick(position: Int) {

    }

    override fun imageSliderClick(position: Int,imagePosition:Int) {
        var theClickPost = post_list.get(position)
        StfalconImageViewer.Builder<String>(context,theClickPost.uImage, ImageLoader<String> { imageView, image ->
            Picasso.get().load(image).into(imageView)
        }).withStartPosition(imagePosition)
            .withHiddenStatusBar(true)
            .allowZooming(true)
            .show(true)
    }
}