package com.kumsal.kyk.screns

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.widget.SocialEditText
import com.kongzue.dialog.interfaces.OnDismissListener
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

class CreatePost : AppCompatActivity(), security_adapter.OnITemClickListener {
    private lateinit var profile_image: CircleImageView
    private lateinit var select_image: ImageButton
    private lateinit var share_button: Button
    private lateinit var post_text_element: SocialEditText
    private lateinit var select_privacy: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    companion object {
        var listElement = ArrayList<security_model>()
        lateinit var textView: TextView
        lateinit var radioGroup: RadioGroup
        private lateinit var mAdapter: security_adapter
        var isActionMode = false
        var selectedlistElement = ArrayList<security_model>()
        var mcounter = 0

        private lateinit var mRadioGroup: RadioGroup
        private lateinit var alfriends: RadioButton
        private lateinit var excpection: RadioButton
        private lateinit var accept_selected_name: Button
        private lateinit var selectedAll: CheckBox
    }


    //add intent element varíable
    var name = ""
    var imageUri = ""
    var thmbImageUri = ""
    var userid = ""
    var username = ""

    //Database section
    private lateinit var mPostRefDb: DatabaseReference
    private lateinit var mUserDbReference: DatabaseReference

    //Action section


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initialComponent()
        textView = TextView(this)

        val mention1 = Mention("dirtyhobo")
        val mention2 = Mention.Builder("hobo")
            .setDisplayname("Regular Hobo")
            .setAvatarDrawable(R.mipmap.ic_launcher)
            .build()
        val mention3 = Mention.Builder("hendraanggrian")
            .setDisplayname("Hendra Anggrian")
            .setAvatarURL("https://avatars0.githubusercontent.com/u/11507430?v=3&s=460")
            .build()
        var uidG = Globals.ınstance?.uid

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
                var ukey = mPostRefDb.child(userid).push().key.toString()
                WaitDialog.show(this@CreatePost, getString(R.string.please_wait));
                WaitDialog.dismiss(10000)
                values.put("pc", postContent as String)
                values.put("name", name as String)
                values.put("username", username)
                values.put("imageUri", imageUri)
                values.put("time", ServerValue.TIMESTAMP)
                values.put("thmbImageUri", thmbImageUri)

                mPostRefDb.child(ukey).setValue(values).addOnSuccessListener {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_action, menu)

        val search: MenuItem? = menu?.findItem(R.id.search_bar_for_count)

        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search something"


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText.toString())
                selectedAll.isChecked=false
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun canBeSent() {
        post_text_element.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                share_button.isEnabled = post_text_element.length() > 0
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
        select_privacy = findViewById(R.id.activity_create_post_select_security)
        selectedlistElement = ArrayList<security_model>()
        var names = ArrayList<Mention>()

        name = intent.getStringExtra("name") as String
        imageUri = intent.getStringExtra("imageUri") as String
        thmbImageUri = intent.getStringExtra("thmburi") as String
        userid = intent.getStringExtra("uid") as String
        username = intent.getStringExtra("username") as String

        //Firebase initialize zoon
        mPostRefDb = FirebaseDatabase.getInstance().getReference("Post")
        mUserDbReference = FirebaseDatabase.getInstance().getReference("Users")

        //secure initialize section
        mAdapter = security_adapter(listElement, this, CreatePost())
        mAdapter.setOnITemClickListener(this)
        //Test section
        secure_initial()


    }

    private fun secure_initial() {

        select_privacy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                FullScreenDialog.show(
                    this@CreatePost,
                    R.layout.security_bind_element,
                    object : FullScreenDialog.OnBindView {
                        override fun onBind(dialog: FullScreenDialog?, rootView: View?) {
                            recyclerView = rootView?.findViewById(R.id.secure_recycler)!!
                            recyclerView.setHasFixedSize(true)
                            recyclerView.layoutManager = LinearLayoutManager(rootView.context)
                            mRadioGroup = rootView?.findViewById(R.id.secure_rg)
                            alfriends = rootView?.findViewById(R.id.secure_allfriends)
                            excpection = rootView?.findViewById(R.id.secure_except)
                            textView = rootView?.findViewById(R.id.secure_bind_element_size)
                            toolbar = rootView.findViewById(R.id.secure_bind_toolbar)
                            accept_selected_name = rootView.findViewById(R.id.secure_bind_accept)
                            selectedAll = rootView.findViewById(R.id.secure_bind_selectAll)
                            setSupportActionBar(toolbar);
                            if (alfriends.isChecked) {
                                recyclerView.visibility = View.GONE
                            }
                            alfriends.setOnClickListener {
                                recyclerView.visibility = View.GONE

                            }
                            excpection.setOnClickListener {
                                recyclerView.visibility = View.VISIBLE

                            }
                            accept_selected_name.setOnClickListener {
                                println("accept is  run")
                            }
                            selectedAll.setOnClickListener {
                                if (selectedAll.isChecked) {
                                    for (i in 0..mAdapter.filerList.size-1) {
                                        var mainIndex=listElement.indexOf(mAdapter.filerList.get(i))
                                        var theSecureM = listElement.get(mainIndex);
                                        theSecureM.theisChecked = true
                                        listElement.set(mainIndex, theSecureM)
                                    }
                                    mAdapter.notifyDataSetChanged()
                                    mcounter= mAdapter.filerList.size
                                    textView.text="${mcounter} person selected"
                                    selectedlistElement.addAll(mAdapter.filerList)
                                }else{
                                    for (i in 0..listElement.size-1) {
                                        var mainIndex=listElement.indexOf(mAdapter.filerList.get(i))
                                        var theSecureM = listElement.get(mainIndex);
                                        theSecureM.theisChecked = false
                                        listElement.set(mainIndex, theSecureM)
                                    }
                                    mAdapter.notifyDataSetChanged()
                                    mcounter= 0
                                    textView.text="0 person selected"
                                    selectedlistElement.clear()

                                }
                            }
                            recyclerView.adapter = mAdapter
                            mUserDbReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var theModel: security_model
                                    listElement.clear()
                                    for (a in snapshot.children) {
                                        var thename = a.child("name_surname").value as String
                                        var theimg = a.child("thmbImage").value as String
                                        var theUsername = a.child("username").value as String
                                        theModel =
                                            security_model(thename, theUsername, theimg, false)
                                        listElement.add(theModel)
                                    }
                                    mAdapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                    }).onDismissListener = OnDismissListener {
                    isActionMode = false
                    selectedlistElement.clear()
                    listElement.clear()
                    mAdapter.notifyDataSetChanged()
                    mcounter = 0
                }
            }
        })
    }

    fun startSelection(index: Int) {

        if (!isActionMode) {
            isActionMode = true
            if (selectedlistElement == null) {
                selectedlistElement = ArrayList<security_model>()
            }
            textView.visibility = View.VISIBLE
            selectedAll.visibility= View.VISIBLE
            updateToolbarText(mcounter)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun updateToolbarText(counter: Int) {
        if (counter == 0) {
            textView.setText("0 person selected ")
        } else {
            textView.setText("$counter person selected ")

        }

    }

    override fun clickCheckBox(position: Int) {
        if (!selectedlistElement.contains(listElement.get(position))) {

            selectedlistElement.add(listElement.get(position))
            mcounter++
            updateToolbarText(mcounter)
            var theSecureM = listElement.get(position);
            theSecureM.theisChecked = true
            listElement.set(position, theSecureM)
        } else {
            selectedAll.isChecked=false
            mcounter--
            updateToolbarText(mcounter)
            if (mcounter == 0) {
                isActionMode = false
                mAdapter.notifyDataSetChanged()
                textView.visibility = View.GONE
                selectedAll.visibility=View.GONE
            }
            selectedlistElement.remove(listElement.get(position))
            var theSecureM = listElement.get(position);
            theSecureM.theisChecked = false
            listElement.set(position, theSecureM)
        }
        accept_selected_name.isEnabled = !selectedlistElement.isEmpty()
    }

    private fun initialDynamic() {
        var hint = "What's on your mind, $name?"
        post_text_element.hint = hint
        if (!TextUtils.isEmpty(imageUri)) {
            Picasso.get().load(imageUri).into(profile_image)
        }
    }
}