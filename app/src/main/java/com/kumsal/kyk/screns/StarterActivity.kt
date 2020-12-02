package com.kumsal.kyk.screns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.kumsal.kyk.R
import com.kumsal.kyk.RegisterActivity

class StarterActivity : AppCompatActivity() {
    private lateinit var create_account_button: Button
    private lateinit var has_account_button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)
        create_account_button=findViewById(R.id.starter_create_acount)
        has_account_button=findViewById(R.id.starter_have_account)
        create_account_button.setOnClickListener(
            View.OnClickListener {
                val intent:Intent=Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
            }
        )
        has_account_button.setOnClickListener(
            View.OnClickListener {
                val intent:Intent=Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideUp(this)
            }
        )
    }
}