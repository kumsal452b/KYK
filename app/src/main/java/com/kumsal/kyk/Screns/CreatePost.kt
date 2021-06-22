package com.kumsal.kyk.Screns

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Timestamp
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.storage.*
import com.hendraanggrian.appcompat.widget.Mention
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.FullScreenDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.Adapter.*
import com.kumsal.kyk.Globals
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.Models.UsersModel
import com.kumsal.kyk.Models.newDataPosModel
import com.kumsal.kyk.Models.security_model
import com.kumsal.kyk.R
import com.kumsal.kyk.CustomAnimation.Animation
import com.kumsal.kyk.IInterfaces.GetCenterSimilar
import com.kumsal.kyk.IInterfaces.imageLoadCall
import com.nguyenhoanglam.imagepicker.model.Config.CREATOR.ROOT_DIR_DCIM
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Predicate
import kotlin.Any
import kotlin.Array
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.arrayOf
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log
import kotlin.toString


class CreatePost : AppCompatActivity(), security_adapter.OnITemClickListener,
    imageSelected_adapter.ItemClickListner {
    private lateinit var profile_image: CircleImageView
    private lateinit var select_image: ImageButton
    private lateinit var share_button: Button
    private lateinit var post_text_element: SocialAutoCompleteTextView
    private lateinit var select_privacy: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var securityTag: TextView

    private lateinit var mImageListRecyclerView: RecyclerView
    private lateinit var mlistAdapter: imageSelected_adapter

    private lateinit var mImageListView: java.util.ArrayList<String>
    private lateinit var mthmbImageList: java.util.ArrayList<String>
    private lateinit var mAllFileDataModel: ArrayList<newDataPosModel>
    private lateinit var mStorageReference: StorageReference
    private lateinit var uriList: ArrayList<Image>
    private lateinit var listOfRemoveMember: ArrayList<security_model>
    private lateinit var listOfBlockedMember: ArrayList<security_model>

    companion object {
        private var listElement = ArrayList<security_model>()
        var isActionMode = false
        private var selectedlistElement = ArrayList<security_model>()
        private var mcounter = 0
        private var currentWith = 0
        private lateinit var listener: ListenerRegistration
        private lateinit var deniedListListener: ListenerRegistration
        private lateinit var search: MenuItem
        private lateinit var textView: TextView
        private lateinit var radioGroup: RadioGroup
        private lateinit var mAdapter: security_adapter
        private lateinit var mRadioGroup: RadioGroup
        private lateinit var alfriends: RadioButton
        private lateinit var excpection: RadioButton
        private lateinit var accept_selected_name: Button
        private lateinit var selectedAll: CheckBox
        lateinit var fullScreenDialog: FullScreenDialog
    }

    //add intent element varíable
    var name = ""
    var imageUri = ""
    var thmbImageUri = ""
    var userid = ""
    var username = ""
    var firstControl = true

    //Database section
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mFsSaveSecurity: FirebaseFirestore
    private lateinit var mFsDenied: FirebaseFirestore
    private lateinit var mFsPostDb: FirebaseFirestore
    private lateinit var currentUri: Uri

    var perm = Array<String>(1) { i: Int ->
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var perm2 = Array<String>(1) { i: Int ->
        Manifest.permission.CAMERA
    }

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
//        getUserList(object : GetCenterSimilar<UsersModel> {
//            override fun getUsers(array: java.util.ArrayList<UsersModel>) {
//                for (theUser in array) {
//                    userAdapter.add(
//                        Mention(
//                            theUser.theUserName!!,
//                            theUser.theNameSurname,
//                            theUser.theThmbImage
//                        )
//                    )
//                }
//                userAdapter.notifyDataSetChanged()
//            }
//        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 546) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
//                var intent1 = Intent(this@CreatePost, ImagePickActivity::class.java)
//                intent1.putExtra(IS_NEED_CAMERA, true);
//                intent1.putExtra(Constant.MAX_NUMBER, 6);
//                startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE)
//                openCamara()
                ImagePicker.with(this@CreatePost)
                    .setFolderMode(true)
                    .setFolderTitle("Album")
                    .setDirectoryName("Image Picker")
                    .setMultipleMode(true)
                    .setShowNumberIndicator(true)
                    .setRootDirectoryName(ROOT_DIR_DCIM)
                    .setMaxSize(10)
                    .setLimitMessage("You can select up to 10 images")
                    .setSelectedImages(uriList)
                    .setShowCamera(true)
                    .setRequestCode(100)
                    .start()

            } else {
                Toast.makeText(this, getString(R.string.galery_perm), Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            uriList.clear()
            if (resultCode == RESULT_OK) {
                uriList = ImagePicker.getImages(data)
                if (uriList.size > 0) {
                    share_button.isEnabled = true
                }
//                var getdata=data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE) as ArrayList<ImageFile>
                for (a in uriList) {
                    val file = File(a.path)
                    val uri = Uri.fromFile(file)
                    val theModel =
                        newDataPosModel(file.name, uri, a.path, null, ".jpg", "Image/jpg")
                    mAllFileDataModel.add(theModel)
                }
                mlistAdapter.notifyDataSetChanged()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getImagesList(theList: imageLoadCall) {
        var tempArray = ArrayList<Uri>()
        if (mAllFileDataModel.size > 0) {
            for (an in mAllFileDataModel) {
                var theModel = an
                var isEnd = mAllFileDataModel.indexOf(an) == mAllFileDataModel.size - 1
                uploadData(theModel.mimeType!!, "images/jpg", an.file!!, isEnd, theList)
            }
        } else {
            theList.getLoadImage(ArrayList<String>(), ArrayList<String>())
        }
    }

    private fun uploadData(
        mimeType: String,
        contentType: String,
        fileUri: Uri,
        isEnd: Boolean,
        theList: imageLoadCall
    ) {
        var storageMD = StorageMetadata.Builder()
        storageMD.contentType = contentType
        storageMD.build()
        var file = File(fileUri.path)
        lifecycleScope.launch() {
            var imagePath =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()) + UUID.randomUUID()
            var filePath = mStorageReference.child("PostImage").child(imagePath + ".jpg")

            //referance for thmb
            var filePathForThmn = mStorageReference.child("PostImageThmb").child(imagePath + ".jpg")

            val compressedImageFile = Compressor.compress(this@CreatePost, file) {
                resolution(430, 430)
                quality(80)
                format(Bitmap.CompressFormat.JPEG)
            }
            filePath.putFile(file.toUri(), storageMD.build())
                .addOnCompleteListener(OnCompleteListener { forfile ->
                    if (forfile.isSuccessful) {
                        filePath.downloadUrl.addOnCompleteListener { urlForFile ->
                            filePathForThmn.putFile(compressedImageFile.toUri(), storageMD.build())
                                .addOnCompleteListener(OnCompleteListener { forfileThmb ->
                                    if (forfileThmb.isSuccessful) {
                                        filePathForThmn.downloadUrl.addOnCompleteListener { urlForThumnail ->
                                            mImageListView.add(urlForFile.result.toString())
                                            mthmbImageList.add(urlForThumnail.result.toString())
                                            if ((mthmbImageList.size == mAllFileDataModel.size || mthmbImageList.size == mAllFileDataModel.size)) {
                                                theList.getLoadImage(mImageListView, mthmbImageList)
                                            }
                                        }
                                    }
                                })
                        }
                    }
                })
        }


    }

    private fun getUserList(listInterface: GetCenterSimilar<UsersModel>) {
        mFirestore.collection("Users").addSnapshotListener { document, error ->
            if (error != null) {
                Log.d("Error in cp", error.message!!)
                return@addSnapshotListener
            }
            var userList = ArrayList<UsersModel>()
            for (doc in document!!) {
                var theModel = doc.toObject(UsersModel::class.java)
                userList.add(theModel)
            }
            listInterface.getUsers(userList)
        }
    }

    private fun share() {
        share_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                WaitDialog.show(this@CreatePost, getString(R.string.please_wait));
                WaitDialog.dismiss(10000)
                getImagesList(object : imageLoadCall {
                    override fun getLoadImage(
                        imageList: ArrayList<String>?,
                        imageThmbList: ArrayList<String>?
                    ) {
                        var postContent = post_text_element.text.toString()
                        var values = HashMap<String, Any>()
                        values.put("pc", postContent)
                        values.put("name", name)
                        values.put("username", username)
                        values.put("uImage", imageList!!)
                        values.put("time", Timestamp.now())
                        values.put("uImageThmb", thmbImageUri)
                        values.put("likes", java.util.ArrayList<String>())
                        values.put("uid", Globals.ınstance?.uid!!)
                        values.put("imageUri", imageUri.toString())
                        values.put("uImageThmb", imageThmbList!!)
                        var pushId = mFsPostDb.collection("Post")
                        mFsPostDb.collection("Post").add(values).addOnFailureListener {
                            OnFailureListener {
                                WaitDialog.dismiss()
                                Log.d("Post Db have error", it.message!!)
                            }
                        }.addOnSuccessListener {
                            var dataMap = HashMap<String, Any>()
                            dataMap.put("userOfPost", FieldValue.arrayUnion(it.id))
                            var task =
                                mFsPostDb.collection("Users").document(Globals!!.ınstance!!.uid!!)
                                    .set(dataMap, SetOptions.merge())
                            task.addOnCompleteListener(OnCompleteListener {
                                if (it.isSuccessful) {
                                    WaitDialog.dismiss()
                                    var main_Activity =
                                        Intent(this@CreatePost, MainActivity::class.java)
                                    startActivity(main_Activity)
                                } else {
                                    WaitDialog.show(
                                        this@CreatePost,
                                        getString(R.string.an_error),
                                        TipDialog.TYPE.ERROR
                                    )
                                }
                            })
                        }
                    }
                })
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
                share_button.isEnabled = post_text_element.length() > 0 || uriList.size > 0
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
        firstControl = true
        selectedlistElement = ArrayList<security_model>()
//        var names = ArrayList<Mention>()
        currentUri = Uri.EMPTY
        mStorageReference = FirebaseStorage.getInstance().getReference()
        mImageListRecyclerView = findViewById(R.id.activity_create_post_imageSelected)
        mImageListRecyclerView.setHasFixedSize(true)

        mImageListRecyclerView.layoutManager = GridLayoutManager(this, 3)

        uriList = ObservableArrayList()
        mImageListView = ArrayList()
        mthmbImageList = ArrayList()
        mAllFileDataModel = ArrayList()

        mlistAdapter = imageSelected_adapter(mAllFileDataModel)
        mlistAdapter.itemClickElement = this
        mImageListRecyclerView.adapter = mlistAdapter

        name = intent.getStringExtra("name") as String
        imageUri = intent.getStringExtra("imageUri") as String
        thmbImageUri = intent.getStringExtra("thmburi") as String
        userid = intent.getStringExtra("uid") as String
        username = intent.getStringExtra("username") as String

        //Firebase initialize zoon
        mFirestore = FirebaseFirestore.getInstance()
        mFsSaveSecurity = FirebaseFirestore.getInstance()
        mFsDenied = FirebaseFirestore.getInstance()
        mFsPostDb = FirebaseFirestore.getInstance()
        listener = ListenerRegistration {}
        //secure initialize section
        mAdapter = security_adapter(listElement, this, CreatePost())
        mAdapter.setOnITemClickListener(this)

        listOfBlockedMember = ArrayList()
        listOfRemoveMember = ArrayList()
        //Test section
        secure_initial()
        select_image.setOnClickListener {
            val myArray3 = arrayOf<String>("Camera or Galery")
            BottomMenu.show(
                this@CreatePost,
                myArray3,
                object : OnMenuItemClickListener {
                    override fun onClick(text: String?, index: Int) {
                        when (index) {
                            0 ->
                                if (checkAndRequestPermissions()) {
                                    ActivityCompat.requestPermissions(
                                        this@CreatePost,
                                        perm,
                                        546
                                    )
                                } else {

//                                    var intent1 = Intent(this@CreatePost, ImagePickActivity::class.java)
//                                    intent1.putExtra(IS_NEED_CAMERA, true);
//                                    intent1.putExtra(Constant.MAX_NUMBER, 6);
//                                    startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE)
//                                    ImagePicker.with(this@CreatePost)
//                                        .crop()
//
//                                        .start()
//                                    openCamara()
//                                    var ipicker = InstagramPicker(this@CreatePost)
//                                    ipicker.show(1, 2, 5, MultiListener { item ->
//                                        for (ab in item) {
//                                            val a=Uri.parse(ab)
//                                            val file = File(a.path)
//                                            val uri = Uri.fromFile(file)
//                                            val theModel = newDataPosModel(
//                                                file.name,
//                                                uri,
//                                                a.path,
//                                                null,
//                                                ".jpg",
//                                                "Image/jpg"
//                                            )
//                                            mAllFileDataModel.add(theModel)
//                                        }
//                                        mlistAdapter.notifyDataSetChanged()
//                                    })
                                    ImagePicker.with(this@CreatePost)
                                        .setFolderMode(true)
                                        .setFolderTitle("Album")
                                        .setDirectoryName("Image Picker")
                                        .setMultipleMode(true)
                                        .setRootDirectoryName(ROOT_DIR_DCIM)
                                        .setShowNumberIndicator(true)
                                        .setMaxSize(10)
                                        .setLimitMessage("You can select up to 10 images")
                                        .setSelectedImages(uriList)
                                        .setRequestCode(100)
                                        .start()
                                }

                        }
                    }

                })
                .cancelButtonText = "Close"
        }


    }

    override fun onItemClickListener(position: Int) {
        mAllFileDataModel.removeAt(position)
        mlistAdapter.notifyDataSetChanged()
    }

    private fun checkAndRequestPermissions(): Boolean {
        var permCam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        var permWrtStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var permReadStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        var listPermissionsNeeded = ArrayList<String>()
        if (permCam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (permWrtStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permReadStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(), 546
            )
            return false
        }
        return true
    }

    private fun accesList(theDeniedElement: GetDeniedList) {
        deniedListListener = mFsDenied.collection("Users").document(userid)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.d("error denied list", e.message!!)
                    return@addSnapshotListener
                }
                var theuser = document?.toObject(UsersModel::class.java)
                if (theuser?.blockBy == null) {
                    theuser?.blockBy = ArrayList<String>()
                }
                if(theuser?.blocked == null){
                    theuser?.blocked = ArrayList();
                }

                theDeniedElement.accedDenied(theuser?.blockBy, theuser?.blocked)
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

                                listener = mFirestore.collection("Users")
                                    .addSnapshotListener { it, error ->
                                        listElement.clear()
                                        accesList(object : GetDeniedList {
                                            override fun accedDenied(
                                                blockBy: ArrayList<String>?,
                                                blocked: ArrayList<String>?
                                            ) {
                                                val mUserIDs = ArrayList<String>()
                                                if (selectedlistElement.size > 0) {
                                                    for (i in 0..selectedlistElement.size - 1) {
                                                        mUserIDs.add(selectedlistElement[i].thePersonId.toString())
                                                    }
                                                }
                                                selectedlistElement.clear()
                                                var isCheck = false
                                                for (doc in it!!) {
                                                    if (doc.id == Globals.ınstance?.uid)
                                                        continue
                                                    var theData =
                                                        doc.toObject(UsersModel::class.java)
                                                    theData.theId = doc.id
                                                    if (blocked?.contains(theData.theId)!!) {
                                                        isCheck = true
                                                    }else{
                                                        isCheck=false
                                                    }
                                                    var theSecureData = security_model(
                                                        theData.theNameSurname!!,
                                                        theData.theUserName!!,
                                                        theData.theThmbImage!!,
                                                        isCheck,
                                                        theData.theId!!
                                                    )
                                                    if (isCheck) {
                                                        selectedlistElement.add(theSecureData)
                                                        listOfBlockedMember.add(theSecureData)
                                                    }


//                                                    if (firstControl) {
//                                                        if (blockBy!!.contains(theSecureData.theusername!!)) {
//                                                            theSecureData.theisChecked = true
//                                                            selectedlistElement.add(theSecureData)
//                                                            mcounter++
//                                                        }
//                                                    } else {
//                                                        if (mUserIDs.contains(theSecureData.theusername)) {
//                                                            theSecureData.theisChecked = true
//                                                            selectedlistElement.add(theSecureData)
//                                                        }
//                                                    }

                                                    listElement.add(theSecureData)
                                                }
                                                firstControl = false
                                            }
                                        })

                                        mAdapter.notifyDataSetChanged()
                                    }
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
            //Dedected removable list
            WaitDialog.show(this, getString(R.string.please_wait))
            for (thePerson in listOfBlockedMember) {
                if (!selectedlistElement.contains(thePerson))
                    listOfRemoveMember.add(thePerson)
            }
            // delet'ng member from BlockBy lists
            var fsRemoveMemberBacth = mFsSaveSecurity.batch()
            for (thePerson in listOfRemoveMember) {
                val removeRef =
                    mFsSaveSecurity.collection("Users").document(thePerson.thePersonId!!)
                val theUserMap = HashMap<String, Any>()
                theUserMap.set("blockBy", FieldValue.arrayRemove(Globals.ınstance?.uid))
                fsRemoveMemberBacth.set(removeRef, theUserMap, SetOptions.merge())
            }
            fsRemoveMemberBacth.commit()

            var blockers = HashMap<String, Any>()
            blockers.put("blockBy", FieldValue.arrayUnion(username))

            val fsBlockByBatch = mFsSaveSecurity.batch()
            for (thePerson in selectedlistElement) {
                val blockByRef =
                    mFsSaveSecurity.collection("Users").document(thePerson.thePersonId!!)
                val theUserMap = HashMap<String, Any>()
                theUserMap.set("blockBy", FieldValue.arrayUnion(Globals.ınstance?.uid))
                fsBlockByBatch.set(blockByRef, theUserMap, SetOptions.merge())
            }
            fsBlockByBatch.commit()

            var blocked = HashMap<String, ArrayList<String>>()
            var blockedList=ArrayList<String>()
            for (item in selectedlistElement) {
                blockedList.add(item.thePersonId!!)
            }
            blocked.put("blocked",blockedList)
            mFsSaveSecurity.collection("Users").document(Globals.ınstance?.uid!!).set(
                blocked,
                SetOptions.merge()
            ).addOnSuccessListener {
                WaitDialog.dismiss()
            }.addOnFailureListener { error ->
               WaitDialog.dismiss()
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

    public fun startSelection(index: Int, checkBox: LottieAnimationView) {
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
//        mAdapter.notifyDataSetChanged()
        if (checkBox.frame == 0) {
            checkBox.speed = 1.0f
            checkBox.playAnimation()
            mAdapter.items[index].theisChecked = true
        } else {
            mAdapter.items[index].theisChecked = false
            checkBox.speed = -2.5f
            checkBox.playAnimation()
        }
        if (!selectedlistElement.contains(listElement.get(index))) {
            selectedlistElement.add(listElement.get(index))
            mcounter++
            updateToolbarText(mcounter)
            var theSecureM = listElement.get(index);
            theSecureM.theisChecked = true
            listElement.set(index, theSecureM)
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
            selectedlistElement.remove(listElement.get(index))
            var theSecureM = listElement.get(index);
            theSecureM.theisChecked = false
            listElement.set(index, theSecureM)
        }
        accept_selected_name.isEnabled = !selectedlistElement.isEmpty()
        if (listElement.size == selectedlistElement.size) {
            selectedAll.isChecked = true
        } else if (listElement.size > selectedlistElement.size) {
            selectedAll.isChecked = false
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
        var hint = "What's on your mind, " + name + "?"
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