package com.kumsal.kyk

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu

import com.kongzue.dialog.v3.MessageDialog

import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.ThreadLocalRandom

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        name = getIntent().getStringExtra("name")
//        linearLayout=findViewById(R.id.chooser_layout_linearLayout)
//        var view = layoutInflater.inflate(R.layout.chooser_layout_item, linearLayout,false) as View
        choosingDialog = Dialog(this@RegisterDetailActivity, R.style.AppTheme)
//        close = findViewById(R.id.chooser_layout_close)
//        galery =findViewById(R.id.chooser_layout_galery)
//        camera =findViewById(R.id.chooser_layout_camera)

//        galery.setOnClickListener(this)
//        camera.setOnClickListener(this)


//        chooserSetting()

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
        var perm = Array<String>(3) { i: Int ->
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        var perm2 = Array<String>(3) { i: Int ->
            Manifest.permission.CAMERA
        }


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
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm2, 545
                                        )
                                    }
                                1 ->
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                            this@RegisterDetailActivity,
                                            perm, 546
                                        )
                                    }

                            }
                        }

                    })
                    .cancelButtonText = "Close"
            }
        })

    }


    fun chooserSetting() {
        choosingDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        choosingDialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
        choosingDialog.window?.setGravity(Gravity.BOTTOM)
        choosingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        choosingDialog.setCancelable(true)
        choosingDialog.setContentView(R.layout.chooser_layout_item)


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
            }
        }
        if (requestCode == 1234) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0) {
                Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_LONG)
                var Cameraintent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(Cameraintent, 600)
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                }
            }
        }
        if (requestCode == 600) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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