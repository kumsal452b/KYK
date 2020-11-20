package com.kumsal.kyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

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

                if (TextUtils.isEmpty(mail.text.toString())) {
                    mail.setError(getString(R.string.mustbe_empy_email))
                    return@OnClickListener
                }
                if (isMailValid(mail.text.toString())) {

                }else{
                    mail.setError(getString(R.string.test))
                }
            }
        )
    }
    private fun isMailValid(mail:CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

}