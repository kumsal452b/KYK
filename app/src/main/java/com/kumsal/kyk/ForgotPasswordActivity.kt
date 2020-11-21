package com.kumsal.kyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var mail:EditText
    private lateinit var button: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mAuth=FirebaseAuth.getInstance()

        mail=findViewById(R.id.forgot_password_mail)
        button=findViewById(R.id.forgot_password_button)

        button.setOnClickListener(
            View.OnClickListener {
                WaitDialog.show(this,getString(R.string.sending_email_forgot_pasword))
                if (TextUtils.isEmpty(mail.text.toString())) {
                    WaitDialog.dismiss()
                    mail.setError(getString(R.string.mustbe_empy_email))
                    return@OnClickListener
                }
                if (isMailValid(mail.text.toString())) {
                    mAuth.sendPasswordResetEmail(mail.text.toString()).addOnFailureListener {
                        Exception->
                        WaitDialog.dismiss()
                        Toast.makeText(this,Exception.localizedMessage,Toast.LENGTH_LONG)
                    }.addOnSuccessListener(
                        OnSuccessListener {
                            TipDialog.show(this,getString(R.string.login_activity_sucess),TipDialog.TYPE.SUCCESS)
                            Thread.sleep(500)
                            val intent: Intent = Intent(applicationContext,LoginActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }else{
                    WaitDialog.dismiss()
                    mail.setError(getString(R.string.mail_valid))
                }
            }
        )
    }
    private fun isMailValid(mail:CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

}