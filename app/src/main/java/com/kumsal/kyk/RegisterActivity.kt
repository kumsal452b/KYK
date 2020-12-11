package com.kumsal.kyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.interfaces.UserListCallback
import java.util.ArrayList

class RegisterActivity : AppCompatActivity() {
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var passwordTry:EditText
    private lateinit var register:Button
    private lateinit var mUser: FirebaseUser
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase:DatabaseReference
    var troubleCount:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth=FirebaseAuth.getInstance()
        name=findViewById(R.id.register_name_and_surname)
        email=findViewById(R.id.login_mail)
        password=findViewById(R.id.login_password)
        passwordTry=findViewById(R.id.register_password_try)
        register=findViewById(R.id.register_activity_detail_regBtn)
        mDatabase=FirebaseDatabase.getInstance().reference.child("Users")
        register.setOnClickListener(View.OnClickListener {
//            WaitDialog.show(this,getString(R.string.please_wait))
            if (register()) {
                WaitDialog.show(this, "Loading")
                WaitDialog.dismiss(5000)
                val intent: Intent =
                    Intent(this@RegisterActivity, RegisterDetailActivity::class.java)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("email", email.text.toString())
                intent.putExtra("pass", password.text.toString())
                var test = email.text.toString().trimEnd().trimStart()

                callEmailCheck(object : UserListCallback {
                    override fun onCallBack(value: ArrayList<String>) {
                        WaitDialog.dismiss()
                        println(value.contains(test))
                        if (!value.contains(test)) {
                            startActivity(intent)
                            Animatoo.animateSwipeLeft(this@RegisterActivity)
                        } else {
                            MessageDialog.show(
                                this@RegisterActivity,
                                getString(R.string.err),
                                getString(R.string.email_available),
                                "OK"
                            )
                        }
                    }

                })
            }
        })

        password.addTextChangedListener {
            text ->
            if (!TextUtils.equals(password.text.toString(),passwordTry.text.toString())){
                password.setError( getString(R.string.register_activity_match_pass))
                passwordTry.setError(getString(R.string.register_activity_match_pass))
                troubleCount++
            }else{
                passwordTry.setError(null)
                password.setError(null)
            }
        }
        passwordTry.addTextChangedListener {
                text ->
            if (!TextUtils.equals(password.text.toString(),passwordTry.text.toString())){
                password.setError( getString(R.string.register_activity_match_pass))
                passwordTry.setError(getString(R.string.register_activity_match_pass))
                troubleCount++
            }else{
                passwordTry.setError(null)
                password.setError(null)
            }
        }
    }
    private fun callEmailCheck(myList:UserListCallback){
        var emailArray=ArrayList<String>()
        mDatabase.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    var email=data.child("email").value as String
                    emailArray.add(email)
                }
                myList.onCallBack(emailArray)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun register():Boolean {


        if (TextUtils.isEmpty(name.text.toString())){

            name.setError(getString(R.string.mustbe_empy_email))
            troubleCount++
        }
        if (!isMailValid(email.text.toString())){
            email.setError(getString(R.string.mail_valid))
            troubleCount++
        }
        if (TextUtils.isEmpty(email.text.toString())){
                email.setError(getString(R.string.email_is_empty))
                troubleCount++

        }

        if (TextUtils.isEmpty(password.text.toString())){
                if (password.text.toString().length<password.maxLines){
                    password.setError(getString(R.string.password_wrong))
                    troubleCount++
                }else{
                    password.setError(getString(R.string.password_cannot_be_emty))
                    troubleCount++
                }
        }
        if (TextUtils.isEmpty(passwordTry.text.toString())){
            if (passwordTry.text.toString().length<passwordTry.maxLines){
                passwordTry.setError(getString(R.string.password_wrong))
                troubleCount++
            }else{
                passwordTry.setError(getString(R.string.password_cannot_be_emty))
                troubleCount++
            }
        }

        if (!TextUtils.equals(passwordTry.text.toString(),password.text.toString())){
            password.setError( getString(R.string.register_activity_match_pass))
            passwordTry.setError(getString(R.string.register_activity_match_pass))
        }

        if (troubleCount>0){
            troubleCount=0
            return false
        }else{
            return true
        }
    }
    private fun isMailValid(mail:CharSequence):Boolean{

        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()

    }
}