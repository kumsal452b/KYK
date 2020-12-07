package com.kumsal.kyk

import android.Manifest
import android.Manifest.permission.*
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu

import com.kongzue.dialog.v3.MessageDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageOptions
import com.theartofdev.edmodo.cropper.CropImageView

import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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
    private lateinit var mAuth: FirebaseAuth
    private var name: String? = null
    private var mUsername: DatabaseReference? = null
    private lateinit var choosingDialog: Dialog
    private lateinit var linearLayout: LinearLayout

    //Chooser
    private lateinit var camera: ImageView
    private lateinit var galery: LinearLayout
    private lateinit var close: ImageButton

    var perm = Array<String>(1) { i: Int ->
        Manifest.permission.READ_EXTERNAL_STORAGE
        WRITE_EXTERNAL_STORAGE
    }
    var perm2 = Array<String>(1) { i: Int ->
        Manifest.permission.CAMERA
    }
    var perm3 = Array<String>(1) { i: Int ->
        Manifest.permission.READ_EXTERNAL_STORAGE
        WRITE_EXTERNAL_STORAGE
    }
    lateinit var bitma: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        name = getIntent().getStringExtra("name")

        username = findViewById(R.id.register_activity_detail_username);
        imageView = findViewById(R.id.register_activity_detail_imageView);
        imageBtn = findViewById(R.id.register_activity_detail_imageButton);
        advice = findViewById(R.id.register_activity_detail_spinner)
        regBtn = findViewById(R.id.register_activity_detail_regBtn)
        mAuth = FirebaseAuth.getInstance()
        mUsername = FirebaseDatabase.getInstance().getReference("Users")
        generateUsername(name)

        regBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isEmpty()
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
                                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm2, 1234
                                        )
                                    } else {
                                        var Cameraintent =
                                            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                                        startActivityForResult(Cameraintent, 600)

                                    }
                                1 ->
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm,
                                            546
                                        )
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

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 546) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                var mediaWindow =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(mediaWindow, 100)
            } else {
                Toast.makeText(this, "galery permission denied", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == 1234) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_LONG)
                var Cameraintent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(Cameraintent, 600)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == 547) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                var uri = getUri(bitma) as Uri
                CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
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
                        .setActivityTitle("Crop Image")
                        .setCropMenuCropButtonTitle("Kirp")
                        .setAutoZoomEnabled(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                }
            }
        }
        if (requestCode == 600) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    bitma = data.extras?.get("data") as Bitmap
                    if (checkAndRequestPermissions()) {
                        var uri = getUri(bitma)
                        CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(2, 2)
                            .setActivityTitle("Crop Image")
                            .setAutoZoomEnabled(true)
                            .start(this)

                    } else {
                        var uri = getUri(bitma)
                        CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(2, 2)
                            .setActivityTitle("Crop Image")
                            .setAutoZoomEnabled(true)
                            .start(this)

                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun readWriteImage(bitmap: Bitmap): Uri {
        // store in DCIM/Camera directory
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val cameraDir = File(dir, "Camera/")

        val file = if (cameraDir.exists()) {
            File(cameraDir, "LK_${System.currentTimeMillis()}.png")
        } else {
            cameraDir.mkdir()
            File(cameraDir, "LK_${System.currentTimeMillis()}.png")
        }

        println("permission" + checkPermissons())
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()

        return Uri.fromFile(file)
    }

    fun checkPermissons(): Boolean {
        var result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
    fun getUri(bitmap: Bitmap):Uri{
       var bytes=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        var path=MediaStore.Images.Media.insertImage(this.contentResolver,bitmap,"Title",null)
        return Uri.parse(path)
    }

    fun checkAndRequestPermissions(): Boolean {
        var permCam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        var permWrtStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var permReadStrg =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        var listPermissionsNeeded=ArrayList<String>()
        if (permCam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(CAMERA)
        }
        if (permWrtStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE)
        }
        if (permReadStrg != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()){

            ActivityCompat.requestPermissions(this,
                listPermissionsNeeded.toTypedArray(),547)
            return false
        }
        return true
    }

    fun isEmpty(): Boolean {
        var troubleCount = 0;
        if (TextUtils.isEmpty(username.text)) {
            if (advice?.selectedItem == null) {
                username.setError(getString(R.string.must_be_leave))
                MessageDialog.show(
                    this@RegisterDetailActivity,
                    getString(R.string.err),
                    getString(R.string.choose_username),
                    "Okey"
                )
                return false
            }
        }
        return true
    }

    fun generateUsername(ad: String?): List<String> {
        var result = ArrayList<String>()
        var username = ArrayList<String>()
        var name = ""
        var surname: String? = null
        var fulname = ""
        mUsername?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (a in snapshot.children) {
                    username.add(a.child("username").value.toString())
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
                var ad1 = name.trim().toLowerCase() + surname?.trim()?.toLowerCase()
                var count = 0
                if (!username.contains(ad1)) {
                    result.add(ad1)
                    count++
                }
                var ad2 = ""
                val surnameM = surname?.substring(0, 1)?.toUpperCase() + surname?.substring(1)
                while (true) {
                    var num = ThreadLocalRandom.current().nextInt(10, 10000)
                    var num2 = ThreadLocalRandom.current().nextInt(0, 2)

                    if (num2 == 1) {
                        ad2 = name.trim().toLowerCase() + surnameM.trim() + num
                    }
                    if (num2 == 0) {
                        ad2 = name.trim().toLowerCase() + surname?.toLowerCase()?.trim() + num
                    }
                    if (!username.contains(ad2)) {
                        result.add(ad2)
                        count++
                    }
                    if (count == 3) {
                        break
                    }
                }
                var adapter = ArrayAdapter<String>(
                    this@RegisterDetailActivity,
                    R.layout.spinner_list,
                    result
                )
                advice?.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }
        })


        return result
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}