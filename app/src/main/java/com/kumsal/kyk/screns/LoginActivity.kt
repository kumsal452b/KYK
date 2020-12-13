package com.kumsal.kyk.screns

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Pair
import android.view.View
import android.widget.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.kumsal.kyk.MainActivity
import com.kumsal.kyk.R
import com.kumsal.kyk.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var forgotPsw: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imageView: ImageView
    private var troubleCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.login_mail)
        password = findViewById(R.id.login_password)
        login = findViewById(R.id.register_activity_detail_regBtn)
        register = findViewById(R.id.login_register_button)
        forgotPsw = findViewById(R.id.login_forgot_textView)
        mAuth = FirebaseAuth.getInstance()
        imageView = findViewById(R.id.login_imageView)

        login.setOnClickListener(
            View.OnClickListener {
                if (isLogin()){
                    if (!isMailValid(email.text.toString())){
                        email.setError(getString(R.string.mail_valid))
                        return@OnClickListener
                    }
                    WaitDialog.show(this, "Loading")
                    mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnFailureListener(
                            OnFailureListener { Exception ->
                                TipDialog.show(this, "Problem has found", TipDialog.TYPE.ERROR)
                                Toast.makeText(
                                    applicationContext,
                                    Exception.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        ).addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent: Intent =
                                        Intent(applicationContext, MainActivity::class.java)
                                    var pair: android.util.Pair<View, String>

                                    pair = android.util.Pair(imageView, "loginTransImageLogo")

                                    var option =
                                        ActivityOptions.makeSceneTransitionAnimation(this, pair)
                                    startActivity(intent, option.toBundle())
                                    this.finish()

                                } else {
                                    Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG)
                                        .show();
                                }
                            }
                        )
                }
            }
        )
        register.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        })
        forgotPsw.setOnClickListener(View.OnClickListener {
            val intent_forgot_password: Intent =
                Intent(applicationContext, ForgotPasswordActivity::class.java)

            var option = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(email, "mailEditText"),
                Pair.create(register, "button")
            )
            startActivity(intent_forgot_password, option.toBundle())
        })
    }

    private fun isLogin(): Boolean {
        troubleCount=0
        if (TextUtils.isEmpty(email.text.toString())) {
            email.setError(getString(R.string.mustbe_empy_email))
            troubleCount++
        }
        if (TextUtils.isEmpty(password.text.toString())) {
            password.setError(getString(R.string.password_cannot_be_emty))
            troubleCount++
        }
        return troubleCount <= 0
    }

    private fun isMailValid(mail: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideDown(this)
    }
}