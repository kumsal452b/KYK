package com.kumsal.kyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var passwordTry:EditText
    private lateinit var register:Button
    private lateinit var mUser: FirebaseUser
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth=FirebaseAuth.getInstance()
        name=findViewById(R.id.register_name_and_surname)
        email=findViewById(R.id.register_mail)
        password=findViewById(R.id.regıster_password)
        passwordTry=findViewById(R.id.register_password_try)
        register=findViewById(R.id.register_button)
        mDatabase=FirebaseDatabase.getInstance().reference.child("Users")



        register.setOnClickListener(View.OnClickListener {
            if (register()) {
                mAuth.createUserWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                ).addOnSuccessListener {

                    var mMap:HashMap<String,String>
                    mMap= HashMap()
                    mMap.set("name_surname",name.text.toString())
                    mMap.set("image","")
                    var currId:String=mAuth.toString()
                    mDatabase.child(currId).setValue(mMap).addOnFailureListener {
                        Exception->
                        Toast.makeText(this, Exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }.addOnSuccessListener(
                        OnSuccessListener <Void>{
                            Toast.makeText(this, "Succec", Toast.LENGTH_LONG).show()
                            val intent:Intent=Intent(applicationContext,MainActivity::class.java)
                            startActivity(intent)
                        }
                    )


                }.addOnFailureListener(this) { Exception ->
                    Toast.makeText(this, Exception.localizedMessage, Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    private fun register():Boolean {
        var troubleCount:Int=0

        if (TextUtils.isEmpty(name.text.toString())){

            name.setError(getString(R.string.mustbe_empy_email))
            troubleCount++
        }
        if (TextUtils.isEmpty(email.text.toString())){
            if (!isMailValid(email.text.toString())){
                email.setError(getString(R.string.mail_valid))
                troubleCount++
            }else{
                email.setError(getString(R.string.email_is_empty))
                troubleCount++
            }
        }
        if (TextUtils.isEmpty(password.text.toString())){
                if (password.text.toString().length<password.maxLines){
                    password.setError(getString(R.string.password_wrong))
                    troubleCount++
                }else{
                    password.setError("Password cannot be empty")
                    troubleCount++
                }
        }
        if (TextUtils.isEmpty(passwordTry.text.toString())){
            if (passwordTry.text.toString().length<passwordTry.maxLines){
                passwordTry.setError(getString(R.string.password_wrong))
                troubleCount++
            }else{
                passwordTry.setError("Password cannot be empty")
                troubleCount++
            }
        }

        if (!TextUtils.equals(passwordTry.text.toString(),password.text.toString())){
            password.setError( "passwords must match")
            passwordTry.setError("passwords must match")
        }
        return troubleCount <= 0
    }
    private fun isMailValid(mail:CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
}