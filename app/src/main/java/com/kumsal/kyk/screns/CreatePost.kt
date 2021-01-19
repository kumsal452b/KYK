package com.kumsal.kyk.screns

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.widget.SocialEditText
import com.kongzue.dialog.v3.FullScreenDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.AdapterModel.security_adapter
import com.kumsal.kyk.AdapterModel.security_model
import com.kumsal.kyk.Globals
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreatePost : AppCompatActivity() {
    private lateinit var profile_image:CircleImageView
    private lateinit var select_image:ImageButton
    private lateinit var share_button:Button
    private lateinit var post_text_element: SocialEditText
    private lateinit var select_privacy:ImageButton
    private lateinit var recyclerView: RecyclerView
    companion object {
        var listElement=ArrayList<security_model>()
    }
    var selectedlistElement=ArrayList<security_model>()
    private lateinit var mAdapter:security_adapter
    private lateinit var mRadioGroup: RadioGroup
    private lateinit var alfriends:RadioButton
    private lateinit var excpection:RadioButton
    //add intent element varíable
    var name=""
    var imageUri=""
    var thmbImageUri=""
    var userid=""
    var username=""
    //Database section
    private lateinit var mPostRefDb:DatabaseReference
    private lateinit var mUserDbReference: DatabaseReference

    //Action section
    var isActionMode=false
    var counter=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initialComponent()
        val mention1 = Mention("dirtyhobo")
        val mention2 = Mention.Builder("hobo")
            .setDisplayname("Regular Hobo")
            .setAvatarDrawable(R.mipmap.ic_launcher)
            .build()
        val mention3 = Mention.Builder("hendraanggrian")
            .setDisplayname("Hendra Anggrian")
            .setAvatarURL("https://avatars0.githubusercontent.com/u/11507430?v=3&s=460")
            .build()
        var uidG=Globals.ınstance?.uid

//        names.add(mention2)
//        names.add(mention1)
//        names.add(mention3)
//        var adapter=ArrayAdapter<Mention>(this, android.R.layout.simple_list_item_1, names)
//        val mentions: MutableList<String> = arrayListOf()
//        mentions.add("@hasankucuk")
//        mentions.add("@yahya")
//        mentions.add("@kumsal")
//        post_text_element.setLinkedMention(mentions)

        //initialize intent element
        initialDynamic()
        canBeSent()
        share_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var postContent = post_text_element.text.toString()
                var values = HashMap<String, Any>()
                var ukey=mPostRefDb.child(userid).push().key.toString()
                WaitDialog.show(this@CreatePost, getString(R.string.please_wait));
                WaitDialog.dismiss(10000)
                values.put("pc", postContent as String)
                values.put("name", name as String)
                values.put("username", username)
                values.put("imageUri",imageUri )
                values.put("time",ServerValue.TIMESTAMP)
                values.put("thmbImageUri",thmbImageUri)

                mPostRefDb.child(ukey).setValue(values).addOnSuccessListener{
                            WaitDialog.dismiss()
                            var main_Activity = Intent(this@CreatePost, MainActivity::class.java)
                            startActivity(main_Activity)
                }.addOnFailureListener { Exception ->
                    WaitDialog.dismiss()
                    Toast.makeText(this@CreatePost, Exception.localizedMessage, Toast.LENGTH_LONG)
                }.addOnCompleteListener {
                        WaitDialog.dismiss()
                        var main_Activity = Intent(this@CreatePost, MainActivity::class.java)
                        startActivity(main_Activity)
                        WaitDialog.dismiss()
                }
            }
        })
    }
    fun startSelection(index: Int) {
        if (!isActionMode){
            isActionMode=true
            if (selectedlistElement==null){
                selectedlistElement=ArrayList<security_model>()
            }
            selectedlistElement.add(listElement.get(index))
            counter++
            updateToolbarText(counter)
        }
    }

    private fun updateToolbarText(counter: Int) {

    }

    private fun canBeSent() {
        post_text_element.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                share_button.isEnabled = post_text_element.length()>0
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun initialComponent() {
        profile_image = findViewById(R.id.activity_create_post_image)
        select_image = findViewById(R.id.activity_create_post_select_image)
        share_button = findViewById(R.id.activity_create_post_share)
        post_text_element = findViewById(R.id.activity_create_post_post_text_element)
        select_privacy=findViewById(R.id.activity_create_post_select_security)
        selectedlistElement= ArrayList<security_model>()
        var names=ArrayList<Mention>()

        name = intent.getStringExtra("name") as String
        imageUri = intent.getStringExtra("imageUri") as String
        thmbImageUri=intent.getStringExtra("thmburi") as String
        userid=intent.getStringExtra("uid") as String
        username=intent.getStringExtra("username") as String

        //Firebase initialize zoon
        mPostRefDb=FirebaseDatabase.getInstance().getReference("Post")
        mUserDbReference=FirebaseDatabase.getInstance().getReference("Users")

        //secure initialize section
        mAdapter= security_adapter(listElement,this, CreatePost())
        //Test section
        select_privacy.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                FullScreenDialog.show(this@CreatePost,R.layout.security_bind_element,object: FullScreenDialog.OnBindView{
                    override fun onBind(dialog: FullScreenDialog?, rootView: View?) {
                        recyclerView= rootView?.findViewById(R.id.secure_recycler)!!
                        recyclerView.setHasFixedSize(true)
                        recyclerView.layoutManager=LinearLayoutManager(rootView.context)
                        mRadioGroup=rootView?.findViewById(R.id.secure_rg)
                        alfriends=rootView?.findViewById(R.id.secure_allfriends)
                        excpection=rootView?.findViewById(R.id.secure_except)
                        recyclerView.adapter=mAdapter
                        mUserDbReference.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var theModel:security_model
                                for (a in snapshot.children){
                                    var thename=a.child("name_surname").value as String
                                    var theimg=a.child("thmbImage").value as String
                                    var theUsername=a.child("username").value as String
                                    theModel= security_model(thename,theUsername,theimg)
                                    listElement.add(theModel)
                                }
                                mAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                    }
                })
            }
        })
    }

    private fun initialDynamic() {

        var hint = "What's on your mind, $name?"
        post_text_element.hint = hint
        if (!TextUtils.isEmpty(imageUri)){
            Picasso.get().load(imageUri).into(profile_image)
        }
    }
}