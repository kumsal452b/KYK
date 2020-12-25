package com.kumsal.kyk.screns

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.StreamDownloadTask
import com.google.firebase.storage.UploadTask
import com.hendraanggrian.socialview.commons.Hashtag
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.widget.SocialAutoCompleteTextView
import com.hendraanggrian.widget.SocialEditText
import com.hendraanggrian.widget.SocialTextView
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.security.Timestamp
import java.util.*
import kotlin.collections.HashMap

class CreatePost : AppCompatActivity() {
    private lateinit var profile_image:CircleImageView
    private lateinit var select_image:ImageButton
    private lateinit var share_button:Button
    private lateinit var post_text_element: SocialEditText

    //add intent element varíable
    var name=""
    var imageUri=""
    var userid=""
    var username=""
    //Database section
    private lateinit var mPostRefDb:DatabaseReference
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
                var time = ServerValue.TIMESTAMP
                var values = HashMap<String, String>()
                var ukey=mPostRefDb.child(userid).push().key.toString()
                WaitDialog.show(this@CreatePost, getString(R.string.please_wait));
                WaitDialog.dismiss(10000)
                values.put("pc", postContent.toString())
                values.put("time", time.toString())
                values.put("name", name)
                values.put("username", username)
                mPostRefDb.child(userid).child(ukey).setValue(values).addOnSuccessListener {
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
        mPostRefDb=FirebaseDatabase.getInstance().getReference("Post")
        var names=ArrayList<Mention>()

        name = intent.getStringExtra("name") as String
        imageUri = intent.getStringExtra("thmburi") as String
        userid=intent.getStringExtra("uid") as String
        username=intent.getStringExtra("username") as String
    }

    private fun initialDynamic() {

        var hint = "What's on your mind, $name?"
        post_text_element.hint = hint
        if (!TextUtils.isEmpty(imageUri)){
            Picasso.get().load(imageUri).into(profile_image)
        }
    }
}