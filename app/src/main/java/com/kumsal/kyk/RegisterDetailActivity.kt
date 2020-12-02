package com.kumsal .kyk

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

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
    private lateinit var choosingDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        name = getIntent().getStringExtra("name")
        println(name)
        username = findViewById(R.id.register_activity_detail_username);
        imageView = findViewById(R.id.register_activity_detail_imageView);
        imageBtn = findViewById(R.id.register_activity_detail_imageButton);
        advice = findViewById(R.id.register_activity_detail_spinner)
        regBtn = findViewById(R.id.register_activity_detail_regBtn)
        mAuth = FirebaseAuth.getInstance()
        mUsername = FirebaseDatabase.getInstance().getReference("Users")
        generateUsername(name)
        choosingDialog= Dialog(this,R.style.AppTheme)
        chooserSetting()
        regBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isEmpty()
            }
        })
        var perm=Array<String>(3){
            i: Int ->
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        var perm2=Array<String>(3){
                i: Int ->
            Manifest.permission.CAMERA
        }

        imageBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
//                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//                    if (perm != null) {
//                        requestPermissions(perm,2)
//                    }
//                }else{
//                    var mediaWindow=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    startActivityForResult(mediaWindow,1)
//                }

                if (checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    if (perm != null) {
                        requestPermissions(perm2,1234)
                    }
                }else{
                    var mediaWindow=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(mediaWindow,1111)
                }
            }
        })

    }

    fun chooserSetting(){
        choosingDialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==2){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.size>0){
                var mediaWindow=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(mediaWindow,1)
            }
        }
        if (requestCode==1234){
            if (grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults.size>0){
                var mediaWindow=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(mediaWindow,1111)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==1){
            if (resultCode== RESULT_OK){
                if (data!=null){

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
        var surname:String?=null
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
                    var num2 =  ThreadLocalRandom.current().nextInt(0, 2)

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