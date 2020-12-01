package com.kumsal .kyk

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.view.menu.ActionMenuItemView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.Notification
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class RegisterDetailActivity : AppCompatActivity() {

    private lateinit var username: AutoCompleteTextView
    private lateinit var imageView: CircleImageView
    private lateinit var imageBtn: ImageButton
    private var advice: Spinner? = null
    private lateinit var regBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private var name: String? = null
    private var mUsername: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        name = getIntent().getStringExtra("name")
        println(name)
        username = findViewById(R.id.register_activity_detail_username);
        imageView = findViewById(R.id.register_activity_detail_imageView);
        imageBtn = findViewById(R.id.register_activity_detail_imageButton);
        advice = findViewById(R.id.register_activity_detail_spinner)
        regBtn = findViewById(R.id.register_activity_detail_regBtn)
        mAuth = FirebaseAuth.getInstance()
        mUsername = FirebaseDatabase.getInstance().getReference("Users")
        generateUsername(name)

        regBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isEmpty()
            }
        })
        imageBtn.setOnClickListener(object: View.OnClickListener{

        })

    }

    fun isEmpty(): Boolean {
        var troubleCount = 0;
        if (TextUtils.isEmpty(username.text)) {
            if (advice?.selectedItem == null) {
                username.setError(getString(R.string.must_be_leave))
                MessageDialog.show(
                    this@RegisterDetailActivity,
                    getString(R.string.err),
                    getString(R.string.choose_username),
                    "Okey"
                )
                return false
            }
        }
        return true
    }

    fun generateUsername(ad: String?): List<String> {
        var result = ArrayList<String>()
        var username = ArrayList<String>()
        var name = ""
        var surname:String?=null
        var fulname = ""
        mUsername?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (a in snapshot.children) {
                    username.add(a.child("username").value.toString())
                }
                fulname = ad?.trim().toString()
                for (a in 0..fulname.length - 1) {
                    if (fulname.get(a) == ' ') {
                        if (fulname.get(a + 1) == ' ') {
                            continue
                        } else {
                            surname = fulname.substring(a + 1, fulname.length)
                            break
                        }
                    }
                    name += fulname.get(a)
                }
                var ad1 = name.trim().toLowerCase() + surname?.trim()?.toLowerCase()
                var count = 0
                if (!username.contains(ad1)) {
                    result.add(ad1)
                    count++
                }
                var ad2 = ""
                val surnameM = surname?.substring(0, 1)?.toUpperCase() + surname?.substring(1)
                while (true) {
                    var num = ThreadLocalRandom.current().nextInt(10, 10000)
                    var num2 =  ThreadLocalRandom.current().nextInt(0, 2)

                    if (num2 == 1) {
                        ad2 = name.trim().toLowerCase() + surnameM.trim() + num
                    }
                    if (num2 == 0) {
                        ad2 = name.trim().toLowerCase() + surname?.toLowerCase()?.trim() + num
                    }
                    if (!username.contains(ad2)) {
                        result.add(ad2)
                        count++
                    }
                    if (count == 3) {
                        break
                    }
                }
                var adapter = ArrayAdapter<String>(
                    this@RegisterDetailActivity,
                    R.layout.spinner_list,
                    result
                )
                advice?.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }
        })


        return result
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeRight(this)
    }
}