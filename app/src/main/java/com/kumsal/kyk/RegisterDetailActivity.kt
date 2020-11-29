package com.kumsal.kyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import de.hdodenhof.circleimageview.CircleImageView

class RegisterDetailActivity : AppCompatActivity() {

    private lateinit var username: AutoCompleteTextView
    private lateinit var imageView:CircleImageView
    private lateinit var imageBtn:ImageButton
    private  var advice:Spinner?=null
    private lateinit var regBtn:Button
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        username=findViewById(R.id.register_activity_detail_username);
        imageView=findViewById(R.id.register_activity_detail_imageView);
        imageBtn=findViewById(R.id.register_activity_detail_imageButton);
        advice=findViewById(R.id.register_activity_detail_spinner)
        regBtn=findViewById(R.id.register_activity_detail_regBtn)
        mAuth=FirebaseAuth.getInstance()

        regBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                isEmpty()
            }

        })
    }

    fun isEmpty():Boolean{
     var troubleCount=0;
        if (TextUtils.isEmpty(username.text)){
            if (TextUtils.isEmpty(advice?.selectedItem.toString())) {
                username.setError("You must not leave this field blank")
                MessageDialog.show(this,"Warning","test")
                return false
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}