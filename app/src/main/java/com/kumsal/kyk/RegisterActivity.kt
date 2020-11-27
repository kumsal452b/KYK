package com.kumsal.kyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var username:AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth=FirebaseAuth.getInstance()
        name=findViewById(R.id.register_name_and_surname)
        email=findViewById(R.id.login_mail)
        password=findViewById(R.id.login_password)
        passwordTry=findViewById(R.id.register_password_try)
        register=findViewById(R.id.login_button)
        username=findViewById(R.id.resgister_username_in_registr);

        mDatabase=FirebaseDatabase.getInstance().reference.child("Users")

        val list=ArrayList<String>()
        list.add("deneme")
        list.add("merhaba")
        list.add("selam")

        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list)
        username.setAdapter(adapter)
        register.setOnClickListener(View.OnClickListener {
            println(register())
            if (register()) {
                password.clearAnimation()
                passwordTry.clearAnimation()
                mAuth.createUserWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                ).addOnSuccessListener {

                    var mMap: HashMap<String, String>
                    mMap = HashMap()
                    mMap.set("name_surname", name.text.toString())
                    mMap.set("image", "")
                    val currId: String = mAuth.uid.toString()
                    val globals = Globals.ınstance
                    globals?.uid = currId
                    mDatabase.child(currId).setValue(mMap).addOnFailureListener { Exception ->
                        Toast.makeText(this, Exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }.addOnSuccessListener(
                        OnSuccessListener<Void> {
                            Toast.makeText(this, "Succec", Toast.LENGTH_LONG).show()
                            val intent: Intent =
                                Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            this.finish()
                        }
                    )


                }.addOnFailureListener(this) { Exception ->
                    Toast.makeText(this, Exception.localizedMessage, Toast.LENGTH_LONG).show()

                }
            }
        })

        password.addTextChangedListener {
            text ->
            if (!TextUtils.equals(password.text.toString(),passwordTry.text.toString())){
                password.setError( getString(R.string.register_activity_match_pass))
                passwordTry.setError(getString(R.string.register_activity_match_pass))
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
            }else{
                passwordTry.setError(null)
                password.setError(null)
            }
        }
    }

    private fun register():Boolean {
        var troubleCount:Int=0

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
            return false
        }else{
            return true
        }
    }
    private fun isMailValid(mail:CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
}