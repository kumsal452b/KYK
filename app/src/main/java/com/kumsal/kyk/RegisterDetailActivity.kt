package com.kumsal.kyk

import android.Manifest
import android.Manifest.permission.*
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.camera2.CameraManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.*
import android.widget.Toast.makeText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.*
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu

import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.UID.Companion.getInstance
import com.kumsal.kyk.interfaces.LoadImage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageOptions
import com.theartofdev.edmodo.cropper.CropImageView

import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URI
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

class RegisterDetailActivity : AppCompatActivity() {

    private lateinit var username: AutoCompleteTextView
    private lateinit var imageView: CircleImageView
    private lateinit var imageBtn: ImageButton
    private var advice: Spinner? = null
    private lateinit var regBtn: Button
    private var name: String? = null
    private lateinit var choosingDialog: Dialog
    private lateinit var linearLayout: LinearLayout
    private lateinit var usernameCheckBox: CheckBox
    lateinit var adapter: ArrayAdapter<String>
    private lateinit var spinnerColor: TextView
    private lateinit var usernames: ArrayList<String>

    private lateinit var mAuth: FirebaseAuth
    private var mUsername: DatabaseReference? = null
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mRefStorage: StorageReference

    private lateinit var imageuri: Uri
    var perm = Array<String>(1) { i: Int ->
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var perm2 = Array<String>(1) { i: Int ->
        Manifest.permission.CAMERA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        name = getIntent().getStringExtra("name")

        username = findViewById(R.id.register_activity_detail_username);
        imageView = findViewById(R.id.register_activity_detail_imageView);
        imageBtn = findViewById(R.id.register_activity_detail_imageButton);
        advice = findViewById<Spinner>(R.id.register_activity_detail_spinner)
        advice?.isEnabled = false

        regBtn = findViewById(R.id.register_activity_detail_regBtn)
        usernameCheckBox = findViewById(R.id.recomanded_username)
        spinnerColor = findViewById(R.id.spinner_list_)
        usernames = ArrayList<String>()

        imageuri = Uri.EMPTY
        mAuth = FirebaseAuth.getInstance()
        mUsername = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        mRefStorage = FirebaseStorage.getInstance().getReference("images")
        generateUsername(name)

        regBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                WaitDialog.show(this@RegisterDetailActivity, getString(R.string.please_wait))
                WaitDialog.dismiss(6000)

                var theName = getIntent().getStringExtra("name") as String
                var thePass = getIntent().getStringExtra("pass") as String
                var theEmail = getIntent().getStringExtra("email") as String
                var theUserNames:String
                if (usernameCheckBox.isChecked){
                    theUserNames=advice?.getSelectedItem() as String
                }
                else{
                    theUserNames=username.text.toString()
                }
                if (usernames.contains(theUserNames)) {
                    WaitDialog.dismiss()
                    MessageDialog.show(
                        this@RegisterDetailActivity,
                        getString(R.string.err),
                        getString(R.string.exsist_username),
                        "OK"
                    )
                    return
                }

                if (isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(
                        theEmail,
                        thePass
                    ).addOnSuccessListener {

                        var mMap = HashMap<String, String>()

                        val currId: String = mAuth.uid.toString()
                        val globals = Globals.ınstance
                        globals?.uid = currId

                        getImagePath(object : LoadImage {
                            override fun getImagePath(path: String) {
                                WaitDialog.dismiss()
                                println("oath" + path)
                                mMap.set("name_surname", theName)
                                mMap.set("image", path)
                                mMap.set("username", theUserNames)
                                mMap.set("email", theEmail)
                                mDatabase.child(currId).setValue(mMap)
                                    .addOnFailureListener { Exception ->
                                        makeText(
                                            this@RegisterDetailActivity,
                                            Exception.localizedMessage,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }.addOnSuccessListener(
                                        OnSuccessListener<Void> {
                                            makeText(
                                                this@RegisterDetailActivity,
                                                "Succec",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            val intent: Intent =
                                                Intent(applicationContext, MainActivity::class.java)
                                            startActivity(intent)
                                            this@RegisterDetailActivity.finish()
                                        }
                                    )


                            }
                        }, currId)


                    }.addOnFailureListener(this@RegisterDetailActivity) { Exception ->
                        WaitDialog.dismiss()
                        makeText(
                            this@RegisterDetailActivity,
                            "test",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                } else {
                    WaitDialog.dismiss()
                }
            }
        })
        imageBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val myArray3 = arrayOf<String>("Camera", "Galery")
                BottomMenu.show(
                    this@RegisterDetailActivity,
                    myArray3,
                    object : OnMenuItemClickListener {
                        override fun onClick(text: String?, index: Int) {
                            print(index)
                            when (index) {
                                0 ->
                                    if (checkAndRequestPermissions()) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm2, 1234
                                        )
                                    } else {

//                                        CropImage.activity()
//                                            .setGuidelines(CropImageView.Guidelines.ON)
//                                            .setAspectRatio(2,2)
//                                            .start(this@RegisterDetailActivity)

                                    }
                                1 ->
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm,
                                            546
                                        )
                                    } else {
                                        var mediaWindow =
                                            Intent(
                                                Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                            )
                                        startActivityForResult(mediaWindow, 100)

                                    }

                            }
                        }

                    })
                    .cancelButtonText = "Close"
            }
        })
        usernameCheckBox.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (usernameCheckBox.isChecked) {
                    username.setEnabled(false)
                    advice?.isEnabled = true
                    username.setHintTextColor(Color.RED)
                    advice?.setBackgroundResource(R.drawable.bacground_edittext)
                    username.setBackgroundResource(R.drawable.bacground_spinner_error)

                } else {
                    username.setEnabled(true)
                    username.setHintTextColor(Color.parseColor("#D1CDCD"))
                    advice?.isEnabled = false
                    username.setBackgroundResource(R.drawable.bacground_edittext)
                    advice?.setBackgroundResource(R.drawable.bacground_spinner_error)
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 546) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                var mediaWindow =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(mediaWindow, 100)
            } else {
                makeText(this, getString(R.string.galery_perm), Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == 1234) {
            if (grantResults.size >= 2) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0
                ) {
                    makeText(this, getString(R.string.permissin_deniad), Toast.LENGTH_LONG)
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2, 2)
                        .start(this)
                } else {
                    makeText(this, getString(R.string.permision), Toast.LENGTH_LONG).show()
                }
            } else if (grantResults.size == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                    makeText(this, getString(R.string.permissin_deniad), Toast.LENGTH_LONG)
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2, 2)
                        .start(this)
                } else {
                    makeText(this, getString(R.string.permision), Toast.LENGTH_LONG).show()
                }
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    var uri: Uri?
                    uri = data.data
                    CropImage.activity(uri)
                        .setAspectRatio(2, 2)
                        .setActivityTitle(getString(R.string.crop_title))
                        .setCropMenuCropButtonTitle(getString(R.string.crop_))
                        .setAutoZoomEnabled(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                imageuri = result.uri
                imageView.setImageURI(imageuri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun checkAndRequestPermissions(): Boolean {
        var permCam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        var permWrtStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var permReadStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        var listPermissionsNeeded = ArrayList<String>()
        if (permCam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(CAMERA)
        }
        if (permWrtStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE)
        }
        if (permReadStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(), 1234
            )
            return false
        }
        return true
    }

    fun isEmpty(): Boolean {
        var troubleCount = 0;
        if (!usernameCheckBox.isChecked){
            if (!usernameCheckBox.isChecked) {
                if (TextUtils.isEmpty(username.text)) {
                    if (!usernameCheckBox.isChecked) {
                        username.setError(getString(R.string.must_be_leave))
                        MessageDialog.show(
                            this@RegisterDetailActivity,
                            getString(R.string.err),
                            getString(R.string.choose_username),
                            "Okey"
                        )
                        return false
                    }
                } else if (usernames.contains(username.text.toString())) {
                    MessageDialog.show(
                        this@RegisterDetailActivity,
                        getString(R.string.err),
                        getString(R.string.wrong_username),
                        "Okey"
                    )
                    return false
                }
            }
        }
        return true
    }

    fun generateUsername(ad: String?): List<String> {
        var result = ArrayList<String>()

        var name = ""
        var surname: String? = null
        var fulname = ""
        WaitDialog.show(this, getString(R.string.please_wait))
        mUsername?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (a in snapshot.children) {
                    usernames.add(a.child("username").value.toString())
                }
                fulname = ad?.trim().toString()
                for (a in 0..fulname.length - 1) {
                    if (fulname.get(a) == ' ') {
                        if (fulname.get(a + 1) == ' ') {
                            continue
                        } else {
                            surname = fulname.substring(a + 1, fulname.length)
                            break
                        }
                    }
                    name += fulname.get(a)
                }
                var surnameM = surname?.substring(0, 1)?.toUpperCase() + surname?.substring(1)
                if (surname == null) {
                    surname = ""
                    surnameM = ""
                }
                var ad1 = name.trim().toLowerCase() + surname?.trim()?.toLowerCase()
                var count = 0
                if (!usernames.contains(ad1)) {
                    result.add(ad1)
                    count++
                }
                var ad2 = ""
                if (surname == null) {
                    surname = ""
                    surnameM = ""
                }

                while (true) {
                    var num = ThreadLocalRandom.current().nextInt(10, 10000)
                    var num2 = ThreadLocalRandom.current().nextInt(0, 2)

                    if (num2 == 1) {
                        ad2 = name.trim().toLowerCase() + surnameM.trim() + num
                    }
                    if (num2 == 0) {
                        ad2 = name.trim().toLowerCase() + surname?.toLowerCase()?.trim() + num
                    }
                    if (!usernames.contains(ad2)) {
                        result.add(ad2)
                        count++
                    }
                    if (count == 3) {
                        break
                    }
                }
                adapter = ArrayAdapter<String>(
                    this@RegisterDetailActivity,
                    R.layout.spinner_list,
                    result
                )
                advice?.adapter = adapter
                WaitDialog.dismiss()

            }

            override fun onCancelled(error: DatabaseError) {
                WaitDialog.dismiss()
                println(error)
            }
        })


        return result
    }

    fun getImagePath(myLoadImage: LoadImage, uid: String) {
       if (imageuri!= Uri.EMPTY){
           var path = "profile_image" + uid
           var fileRef = mRefStorage.child(path + ".jpg")
           fileRef.putFile(imageuri).addOnFailureListener(object : OnFailureListener {
               override fun onFailure(p0: Exception) {
                   throw p0.fillInStackTrace()
                   return
               }
           }).addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
               override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                   if (p0.isSuccessful) {
                       fileRef.downloadUrl.addOnFailureListener { Exception ->
                           println(Exception.localizedMessage + Exception.stackTrace)
                       }.addOnSuccessListener { Uri ->
                           myLoadImage.getImagePath(Uri.toString())

                       }
                   } else if (p0.isCanceled) {
                       throw p0.exception?.fillInStackTrace()!!
                   } else {
                       makeText(
                           this@RegisterDetailActivity,
                           getString(R.string.unknown),
                           Toast.LENGTH_LONG
                       ).show()
                   }
               }
           })
       }
        else{
           myLoadImage.getImagePath("")
       }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}