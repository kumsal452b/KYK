package com.kumsal.kyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog

class LoginActivity : AppCompatActivity() {
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var login:Button
    private lateinit var register:Button
    private lateinit var forgotPsw:TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email=findViewById(R.id.login_mail)
        password=findViewById(R.id.login_password)
        login=findViewById(R.id.login_button)
        register=findViewById(R.id.login_register_button)
        forgotPsw=findViewById(R.id.login_forgot_button)

        mAuth=FirebaseAuth.getInstance()

        login.setOnClickListener(
            View.OnClickListener {
                WaitDialog.show(this,"Loading")
                mAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnFailureListener(
                    OnFailureListener {
                        Exception->
                        TipDialog.show(this, "Problem has found", TipDialog.TYPE.ERROR);
//                        Toast.makeText(applicationContext,Exception.localizedMessage,Toast.LENGTH_LONG).show();
                    }
                ).addOnCompleteListener(
                    OnCompleteListener {
                        task ->
                        if (task.isSuccessful){
                            TipDialog.show(this, getString(R.string.login_activity_sucess), TipDialog.TYPE.SUCCESS);
                            val intent: Intent = Intent(applicationContext,MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"error",Toast.LENGTH_LONG).show();
                        }
                    }
                )
            }
        )
        register.setOnClickListener(View.OnClickListener {
            val intent:Intent=Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        })
    }
}