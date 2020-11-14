package com.kumsal.kyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var passwordTry:EditText
    private lateinit var register:Button
    private lateinit var mUser: FirebaseUser
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth=FirebaseAuth.getInstance()
        name=findViewById(R.id.register_name_and_surname)
        email=findViewById(R.id.register_mail)
        password=findViewById(R.id.regıster_password)
        passwordTry=findViewById(R.id.register_password_try)
        register=findViewById(R.id.register_button)

        register.setOnClickListener(View.OnClickListener {
            if (register()){
                mAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnSuccessListener {
                       
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