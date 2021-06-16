package com.kumsal.kyk

import android.Manifest
import android.Manifest.permission.*
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.Models.UsersModel
import com.kumsal.kyk.IInterfaces.LoadImage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
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
    private lateinit var mFstoreDb:FirebaseFirestore
    private lateinit var mFstoreUsernameDb:FirebaseFirestore
    private lateinit var mRefStorage: StorageReference
    private lateinit var imageuri: Uri
    private lateinit var tmbimageuri: Uri
    var perm = Array<String>(1) { i: Int ->
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var perm2 = Array<String>(1) { i: Int ->
        Manifest.permission.CAMERA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        initializeComponent()
        generateUsername(name)
        clickListeners()
    }

    private fun clickListeners() {
        regBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                WaitDialog.show(this@RegisterDetailActivity, getString(R.string.please_wait))
                WaitDialog.dismiss(24000)

                var theName = getIntent().getStringExtra("name") as String
                var thePass = getIntent().getStringExtra("pass") as String
                var theEmail = getIntent().getStringExtra("email") as String
                var theUserNames: String
                if (usernameCheckBox.isChecked) {
                    theUserNames = advice?.getSelectedItem() as String
                } else {
                    theUserNames = username.text.toString()
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

                        var mMap = HashMap<String, Any>()

                        val currId: String = mAuth.uid.toString()
                        val globals = Globals.ınstance
                        globals?.uid = currId

                        getImagePath(object : LoadImage {
                            override fun getImagePath(path: String, path2: String) {
                                var time=Timestamp.now()
                                var theUserForPush=
                                    UsersModel(theEmail,theName,theUserNames,path2,time,path)
                                theUserForPush.theId=currId
                                theUserForPush.toMap()
                                mFstoreDb.collection("Users").document(currId).set(theUserForPush).addOnSuccessListener(
                                    OnSuccessListener<Void> {
                                        WaitDialog.dismiss()
                                        makeText(
                                            this@RegisterDetailActivity,
                                            getString(R.string.success),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                                        intent.putExtra("deneme",Globals.ınstance)
                                        startActivity(intent)
                                        this@RegisterDetailActivity.finish()
                                    }).addOnFailureListener(OnFailureListener {
                                    WaitDialog.dismiss()
                                    makeText(
                                        this@RegisterDetailActivity,
                                        it.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                            }
                        }, currId)


                    }.addOnFailureListener(this@RegisterDetailActivity) { Exception ->
                        WaitDialog.dismiss()
                        makeText(
                            this@RegisterDetailActivity,
                            Exception.localizedMessage,
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
                            when (index) {
                                0 ->
                                    if (checkAndRequestPermissions()) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm2, 1234
                                        )
                                    }
                                1 ->
                                    if (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm,
                                            546
                                        )
                                    }else{
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
    private fun initializeComponent() {
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
        mFstoreUsernameDb= FirebaseFirestore.getInstance()
        mFstoreDb= FirebaseFirestore.getInstance()
        mRefStorage = FirebaseStorage.getInstance().getReference("images")
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
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0
                ) {
                    var camera_intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(camera_intent, 12345);
                } else {
                    makeText(this, getString(R.string.permision), Toast.LENGTH_LONG).show()
                }
            } else if (grantResults.size == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                    makeText(this, getString(R.string.permissin_deniad), Toast.LENGTH_LONG)
                    var camera_intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(camera_intent, 12345);
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
                var path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KYK"
                var result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                imageuri = result.uri
                imageView.setImageURI(imageuri)

                lifecycleScope.launch() {
                    var copresorImage = Compressor.compress(
                        this@RegisterDetailActivity,
                        File(imageuri.path)
                    )
                    tmbimageuri = copresorImage.toUri();

                }
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
        if (!usernameCheckBox.isChecked) {
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
    fun clearTurkishWorld(name:String):String{
        var convert=name
        var turkisChar= arrayOf<Char>(0x131.toChar(),
            0x130.toChar(), 0xFC.toChar(),
            0xDC.toChar(), 0xF6.toChar(), 0xD6.toChar(),
            0x15F.toChar(), 0x15E.toChar(), 0xE7.toChar(), 0xC7.toChar(), 0x11F.toChar(), 0x11E.toChar()
        )
        var englishChar= arrayOf<Char>('i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G')
        for (i in 0..turkisChar.size-1){
            var t=turkisChar[i]
            var eng=englishChar[i]
            convert=convert.replace(t,eng)
        }
        return convert
    }
    fun generateUsername(ad: String?): List<String> {
        var result = ArrayList<String>()

        var name = ""
        var surname: String? = null
        var fulname = ""
        WaitDialog.show(this, getString(R.string.please_wait))

        mFstoreUsernameDb.collection("Users").addSnapshotListener { value, error ->
            if (error!=null){
                Log.d("Error",error.message as String)
                return@addSnapshotListener
            }
            for (valueof in value!!){
                val theUser = valueof.toObject<UsersModel>(UsersModel::class.java)
                usernames.add(theUser.theUserName as String)
            }
            fulname = clearTurkishWorld(ad?.trim().toString())
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
            var surnameM = ""
            if (surname == null || TextUtils.isEmpty(surname)) {
                surname = ""
                surnameM = ""
            } else {
                surnameM = surname?.substring(0, 1)?.toUpperCase() + surname?.substring(1)
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

        return result
    }
    fun getImagePath(myLoadImage: LoadImage, uid: String) {
        if (imageuri != Uri.EMPTY) {
            var path = "profile_image" + uid
            var pathThmb = "profile_image_thumnail" + uid
            var fileRef = mRefStorage.child(path + ".jpg")
            var fileRefThmb = mRefStorage.child(pathThmb + ".jpg")
            fileRef.putFile(imageuri).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.d("problem", p0.localizedMessage)
                    return
                }
            }).addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                    if (p0.isSuccessful) {
                        fileRef.downloadUrl.addOnFailureListener { Exception ->
                            println(Exception.localizedMessage + Exception.stackTrace)
                        }.addOnSuccessListener { Uri ->
                            fileRefThmb.putFile(tmbimageuri).addOnFailureListener { Exception ->
                                Log.d("problem", Exception.localizedMessage)
                            }.addOnSuccessListener {
                                fileRefThmb.downloadUrl.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        myLoadImage.getImagePath(Uri.toString(), Uri.toString())
                                    } else {
                                        Log.d("problem", "Problem has been found")
                                    }
                                }

                            }

                        }
                    } else if (p0.isCanceled) {
                        Log.d("problem", p0.exception!!.localizedMessage)
                    } else {
                        makeText(
                            this@RegisterDetailActivity,
                            getString(R.string.unknown),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        } else {
            myLoadImage.getImagePath("", "")
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}