package com.kumsal.kyk.screns

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.auth.User
import com.hendraanggrian.appcompat.widget.Mention
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.FullScreenDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.AdapterModel.UsersModel
import com.kumsal.kyk.AdapterModel.security_adapter
import com.kumsal.kyk.AdapterModel.security_model
import com.kumsal.kyk.DBModels.DbUsers
import com.kumsal.kyk.Globals
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.kumsal.kyk.animation.Animation
import com.kumsal.kyk.interfaces.GetCenter
import com.kumsal.kyk.interfaces.UserListCallback
import com.percolate.mentions.Mentionable
import com.percolate.mentions.Mentions
import com.percolate.mentions.QueryListener
import com.percolate.mentions.SuggestionsListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log

class CreatePost : AppCompatActivity(), security_adapter.OnITemClickListener {
    private lateinit var profile_image: CircleImageView
    private lateinit var select_image: ImageButton
    private lateinit var share_button: Button
    private lateinit var post_text_element: SocialAutoCompleteTextView
    private lateinit var select_privacy: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var securityTag: TextView

    companion object {
        private var listElement = ArrayList<security_model>()
        var isActionMode = false
        private var selectedlistElement = ArrayList<security_model>()
        private var mcounter = 0
        private var currentWith = 0
        private lateinit var textView: TextView
        private lateinit var radioGroup: RadioGroup
        private lateinit var mAdapter: security_adapter
        private lateinit var listener: ListenerRegistration
        private lateinit var deniedListListener: ListenerRegistration
        private lateinit var mRadioGroup: RadioGroup
        private lateinit var alfriends: RadioButton
        private lateinit var excpection: RadioButton
        private lateinit var accept_selected_name: Button
        private lateinit var selectedAll: CheckBox
        private lateinit var search: MenuItem
        private lateinit var fullScreenDialog: FullScreenDialog
    }

    //add intent element varíable
    var name = ""
    var imageUri = ""
    var thmbImageUri = ""
    var userid = ""
    var username = ""
    var firstControl=true
    //Database section
    private lateinit var mPostRefDb: DatabaseReference
    private lateinit var mFirestore: FirebaseFirestore

    private lateinit var mFsSaveSecurity: FirebaseFirestore
    private lateinit var mFsDenied: FirebaseFirestore
    private lateinit var mFsPostDb:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initialComponent()
        //initialize intent element
        initialDynamic()
        canBeSent()
        share()
        mentionSetup()
    }

    private fun mentionSetup() {
        post_text_element.context.assets
        var userAdapter: ArrayAdapter<Mention>
        userAdapter = MentionArrayAdapter<Mention>(this)
        post_text_element.mentionAdapter = userAdapter
        getUserList(object : GetCenter<UsersModel> {
            override fun getUsers(array: java.util.ArrayList<UsersModel>) {
                for (theUser in array) {
                    userAdapter.add(
                        Mention(
                            theUser.theUserName!!,
                            theUser.theNameSurname,
                            theUser.theThmbImage
                        )
                    )
                }
                userAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getUserList(listInterface:GetCenter<UsersModel>){
        mFirestore.collection("Users").addSnapshotListener { document, error ->
            if (error!=null){
                Log.d("Error in cp",error.message!!)
                return@addSnapshotListener
            }
            var userList=ArrayList<UsersModel>()
            for (doc in document!!){
                var theModel=doc.toObject(UsersModel::class.java)
                userList.add(theModel)
            }
            listInterface.getUsers(userList)
        }
    }

    private fun share() {
        share_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var postContent = post_text_element.text.toString()
                var values = HashMap<String, Any>()
                var ukey = mPostRefDb.child(userid).push().key.toString()
                WaitDialog.show(this@CreatePost, getString(R.string.please_wait));
                WaitDialog.dismiss(10000)

                values.put("pc", postContent)
                values.put("name", name)
                values.put("username", username)
                values.put("imageUri", imageUri)
                values.put("time", ServerValue.TIMESTAMP)
                values.put("thmbImageUri", thmbImageUri)

                var pushId = mFsPostDb.collection("Post").id
                mFsPostDb.collection("Post").add(values).addOnFailureListener {
                    OnFailureListener {
                        Log.d("Post Db have error", it.message!!)
                    }
                }.addOnSuccessListener {
                    println("success")
                }
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

        search = menu?.findItem(R.id.search_bar_for_count)!!
        search.isVisible = false
        var searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText.toString())

                var counter = 0;
                for (i in 0..mAdapter.filerList.size - 1) {
                    var theSecureM = mAdapter.filerList.get(i);
                    if (theSecureM.theisChecked == false) {
                        selectedAll.isChecked = false;
                        counter++
                        break
                    }
                }
                if (counter == 0) {
                    selectedAll.isChecked = true
                }

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
        textView = TextView(this)
        securityTag = findViewById(R.id.create_post_security_tag)
        var uidG = Globals.ınstance?.uid
        firstControl= true
        selectedlistElement = ArrayList<security_model>()
//        var names = ArrayList<Mention>()

        name = intent.getStringExtra("name") as String
        imageUri = intent.getStringExtra("imageUri") as String
        thmbImageUri = intent.getStringExtra("thmburi") as String
        userid = intent.getStringExtra("uid") as String
        username = intent.getStringExtra("username") as String

        //Firebase initialize zoon
        mFirestore = FirebaseFirestore.getInstance()
        mFsSaveSecurity = FirebaseFirestore.getInstance()
        mFsDenied = FirebaseFirestore.getInstance()
        mFsPostDb= FirebaseFirestore.getInstance()

        //secure initialize section
        mAdapter = security_adapter(listElement, this, CreatePost())
        mAdapter.setOnITemClickListener(this)
        //Test section
        secure_initial()


    }

    private fun accesList(theDeniedElement: GetDeniedList) {
        deniedListListener = mFsDenied.collection("Authentication").document(userid)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.d("error denied list", e.message!!)
                    return@addSnapshotListener
                }
                var deniedList = document?.data as HashMap<String, String>?
                if (deniedList==null)
                    deniedList=HashMap<String, String>()
                theDeniedElement.accedDenied(deniedList)
            }
    }

    private fun secure_initial() {

        select_privacy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                fullScreenDialog = FullScreenDialog.build(this@CreatePost)
                fullScreenDialog.setStyle(DialogSettings.STYLE.STYLE_IOS)
                fullScreenDialog.setStyle(DialogSettings.STYLE.STYLE_KONGZUE)
                    .setCustomView(R.layout.security_bind_element,
                        object : FullScreenDialog.OnBindView {
                            override fun onBind(dialog: FullScreenDialog?, rootView: View?) {
                                securityPanelInitialzed(rootView)
                                securityPanelEventClick()


//                        test.readyElement(object : GetCenter<UsersModel> {
//                            override fun getUsers(array: java.util.ArrayList<UsersModel>) {
//                                    for (get in array){
//                                        listElement.add(security_model(get!!.theNameSurname!!,get!!.theUserName!!,get!!.theThmbImage!!,false))
//                                    }
//                                mAdapter.notifyDataSetChanged()
//                            }
//                        }, "Users", "")
                                var getUsernames = ArrayList<String>()

                                listener = mFirestore.collection("Users")
                                    .addSnapshotListener { it, error ->
                                        listElement.clear()
                                        accesList(object : GetDeniedList {
                                            override fun accedDenied(map: HashMap<String, String>?) {

                                                for (get in map!!) {
                                                    getUsernames.add(get.value)
                                                }
                                                var mUserName = ArrayList<String>()
                                                if (selectedlistElement.size > 0) {
                                                    for (i in 0..selectedlistElement.size - 1) {
                                                        mUserName.add(selectedlistElement[i].theusername.toString())
                                                    }
                                                }
                                                selectedlistElement.clear()
                                                for (doc in it!!) {
                                                    if (doc.id == Globals.ınstance?.uid)
                                                        continue
                                                    var theData =
                                                        doc.toObject(UsersModel::class.java)
                                                    var theSecureData = security_model(
                                                        theData!!.theNameSurname!!,
                                                        theData!!.theUserName!!,
                                                        theData!!.theThmbImage!!,
                                                        false,
                                                        theData.theId!!
                                                    )
                                                    theData.theId = doc.id
                                                    if (firstControl){
                                                        if (getUsernames.contains(theSecureData.theusername)) {
                                                            theSecureData.theisChecked = true
                                                            selectedlistElement.add(theSecureData)
                                                            mcounter++
                                                        }
                                                    }else {
                                                        if (mUserName.contains(theSecureData.theusername)) {
                                                            theSecureData.theisChecked = true
                                                            selectedlistElement.add(theSecureData)
                                                        }
                                                    }

                                                    listElement.add(theSecureData)
                                                }
                                                firstControl=false
                                            }
                                        })

                                        mAdapter.notifyDataSetChanged()
                                    }
//                        mFirestore.collection("Users").addSnapshotListener { document, e ->
//                            if (e!=null){
//                                Log.d("Error", e.message as String)
//                                return@addSnapshotListener
//                            }
//                            for (theDoc in document!!){
//                                var theData=theDoc.toObject(UsersModel::class.java)
//                                listElement.add(security_model(theData!!.theNameSurname!!,theData!!.theUserName!!,theData!!.theThmbImage!!,false))
//                            }
//                            mAdapter.notifyDataSetChanged()
//                        }
                            }
                        })
                fullScreenDialog.show()
            }

        })
    }

    private fun securityPanelEventClick() {
        if (alfriends.isChecked) {
            recyclerView.visibility = View.GONE
        }

        alfriends.setOnClickListener {
            recyclerView.visibility = View.GONE
            selectedAll.visibility = View.GONE
            var anim = Animation(0, textView)
            textView.animation = anim
            accept_selected_name.isEnabled = false
            search.isVisible = false

        }
        excpection.setOnClickListener {
            search.isVisible = true
            recyclerView.visibility = View.VISIBLE
            if (isActionMode) {
                recyclerView.visibility = View.VISIBLE
                selectedAll.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                textView.setText("${selectedlistElement.size} person selected ")
                var anim = Animation(currentWith, textView)
                textView.animation = anim
                if (selectedlistElement.size == listElement.size) {
                    selectedAll.isChecked = true
                }
                if (selectedlistElement.size > 0) {
                    accept_selected_name.isEnabled = true
                }
            }

        }
        accept_selected_name.setOnClickListener {
            WaitDialog.show(this, getString(R.string.please_wait))
            var deniedMap = HashMap<String, Any>()
            for (get in selectedlistElement)
                deniedMap.put(get.thePersonId!!, get.theusername!!)

            mFsSaveSecurity.collection("Authentication").document(Globals.ınstance?.uid!!)
                .set(deniedMap as Map<String, Any>).addOnSuccessListener(OnSuccessListener {
                    WaitDialog.dismiss()
                    fullScreenDialog.doDismiss()
                    securityTag.text = "Someone"
                }).addOnFailureListener {
                    OnFailureListener { exception: Exception ->
                        Log.d("Load denied error", exception.message!!)
                        WaitDialog.dismiss()
                    }
                }
        }
        selectedAll.setOnClickListener {
            if (selectedAll.isChecked) {
                accept_selected_name.isEnabled = true
                for (i in 0..mAdapter.filerList.size - 1) {
                    var mainIndex =
                        listElement.indexOf(mAdapter.filerList.get(i))
                    var theSecureM = listElement.get(mainIndex);
                    theSecureM.theisChecked = true
                    listElement.set(mainIndex, theSecureM)
                }
                mAdapter.notifyDataSetChanged()
                mcounter = mAdapter.filerList.size
                textView.text = "${mcounter} person selected"
                selectedlistElement.addAll(mAdapter.filerList)
            } else {
                accept_selected_name.isEnabled = false
                for (i in 0..listElement.size - 1) {
                    var mainIndex =
                        listElement.indexOf(mAdapter.filerList.get(i))
                    var theSecureM = listElement.get(mainIndex);
                    theSecureM.theisChecked = false
                    listElement.set(mainIndex, theSecureM)
                }
                mAdapter.notifyDataSetChanged()
                mcounter = 0
                textView.text = "0 person selected"
                selectedlistElement.clear()

            }
        }
    }

    private fun securityPanelInitialzed(rootView: View?) {
        recyclerView = rootView?.findViewById(R.id.secure_recycler)!!
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        mRadioGroup = rootView?.findViewById(R.id.secure_rg)
        alfriends = rootView?.findViewById(R.id.secure_allfriends)
        excpection = rootView?.findViewById(R.id.secure_except)
        textView = rootView?.findViewById(R.id.secure_bind_element_size)
        textView.measure(0, 0);       //must call measure!
        toolbar = rootView.findViewById(R.id.secure_bind_toolbar)
        accept_selected_name =
            rootView.findViewById(R.id.secure_bind_accept)
        selectedAll = rootView.findViewById(R.id.secure_bind_selectAll)
        checkSecurePanel()
        setSupportActionBar(toolbar)
        recyclerView.adapter = mAdapter
    }

    private fun checkSecurePanel() {
        if (selectedlistElement.size > 0) {
            isActionMode = true

//            for (i in 0..listElement.size-1){
//                var theSecureM = listElement.get(i);
//                theSecureM.theisChecked = true
//                listElement.set(i, theSecureM)
//            }
        }
    }

    fun startSelection(index: Int) {

        if (!isActionMode) {

            isActionMode = true
            if (selectedlistElement == null) {
                selectedlistElement = ArrayList<security_model>()
            }
            textView.visibility = View.VISIBLE
            currentWith = textView.measuredWidth;
            var anim = Animation(currentWith, textView)
            textView.animation = anim
            selectedAll.visibility = View.VISIBLE
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
            selectedAll.isChecked = false
            mcounter--
            updateToolbarText(mcounter)
            if (mcounter == 0) {
                isActionMode = false
                mAdapter.notifyDataSetChanged()
                textView.visibility = View.GONE
                selectedAll.visibility = View.GONE
            }
            selectedlistElement.remove(listElement.get(position))
            var theSecureM = listElement.get(position);
            theSecureM.theisChecked = false
            listElement.set(position, theSecureM)
        }
        accept_selected_name.isEnabled = !selectedlistElement.isEmpty()
        if (listElement.size == selectedlistElement.size) {
            selectedAll.isChecked = true
        } else if (listElement.size > selectedlistElement.size) {
            selectedAll.isChecked = false
        }
    }

    private fun initialDynamic() {
        var hint = "What's on your mind, $name?"
        post_text_element.hint = hint
        if (!TextUtils.isEmpty(imageUri)) {
            Picasso.get().load(imageUri).into(profile_image)
        }
    }

    override fun onStop() {
        listener.remove()
        super.onStop()
    }

    override fun onDestroy() {
        listener.remove()
        super.onDestroy()
    }
}