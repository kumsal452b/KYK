package com.kumsal.kyk


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter
import com.kumsal.kyk.screns.StarterActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    private var toolbar: Toolbar? = null
    private var mViewPager: ViewPager? = null
    private var sectionPagerAdapter: SectionPagerAdapter? = null
    private var mBottomBar: SmoothBottomBar? = null
    private var add: FloatingActionButton? = null
    private var addPost: FloatingActionButton? = null
    private var addMessage: FloatingActionButton? = null
    private var mDrawerLayout: DrawerLayout? = null
    internal var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private lateinit var mNavbar: NavigationView
    private lateinit var mUserDB: DatabaseReference
    private lateinit var proImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var layout: LinearLayout

    private val rotateAnimOpen:Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateAnimClose:Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val FromBottomAnim:Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottomAnim:Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private val clicable=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialzed
        toolbar = findViewById(R.id.main_activity_toolbar)
        mViewPager = findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mBottomBar = findViewById(R.id.main_activity_bottomBar)

        mDrawerLayout = findViewById(R.id.main_activity_drawer)
        mNavbar = findViewById(R.id.nav_bar)
        //header initialize

        var view =mNavbar.getHeaderView(0)

        name = view.findViewById(R.id.header_circle_name)
        username = view.findViewById(R.id.header_circle_user)
        proImage = view.findViewById(R.id.header_circle_image)

        mAuth = FirebaseAuth.getInstance()
        val mUser1: FirebaseUser? = mAuth?.currentUser
        mUser = mUser1
        mUserDB = FirebaseDatabase.getInstance().getReference("Users")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle?.drawerArrowDrawable?.color = Color.WHITE
        mDrawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle?.syncState()

        mViewPager?.adapter = sectionPagerAdapter
        mBottomBar?.onItemSelectedListener = this
        mBottomBar?.setOnClickListener {
            View.OnClickListener {

            }
        }

        //FAB anime zoon
        add = findViewById(R.id.fab_add)
        addPost=findViewById(R.id.fab_edit)
        addMessage=findViewById(R.id.fab_message)

        add?.setOnClickListener(View.OnClickListener {
            setVisibilty()
            setAnimation()
        })
        mViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                println(position)
                mBottomBar?.itemActiveIndex = position
            }

        })



        mNavbar.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.menu_bar_quit) {
                FirebaseAuth.getInstance().signOut()
                val intent: Intent = Intent(applicationContext, StarterActivity::class.java)
                startActivity(intent)
                this@MainActivity.finish()
            }
            true
        }
        mUserDB.child(mUser?.uid as String)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uriImage = snapshot.child("image").value
                    var thename = snapshot.child("name_surname").value as String
                    var thesername = snapshot.child("username").value as String
                    if (uriImage == null) {
                        uriImage = "emtpy"
                    }
                    name.setText(thename)
                    username.setText(thesername)
                    Picasso.get().load(uriImage as String).into(proImage)
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })


    }

    private fun setAnimation() {

    }

    private fun setVisibilty(clicable:Boolean) {
        if (!clicable){
            addPost.visibility=View.VISIBLE
            addMessage.visibility=View.VISIBLE
        }
        else{
            addPost.visibility=View.VISIBLE
            addMessage.visibility=View.VISIBLE
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle?.onOptionsItemSelected(item)!!) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelect(pos: Int): Boolean {
        if (pos == 0) {
            mViewPager?.setCurrentItem(0)
        }
        if (pos == 1) {
            mViewPager?.setCurrentItem(1)
        }
        if (pos == 2) {
            mViewPager?.setCurrentItem(2)
        }
        if (pos == 3) {
            mViewPager?.setCurrentItem(3)
        }
        return true
    }

    override fun onStart() {
        if (mUser == null) {
            val intent: Intent = Intent(this, StarterActivity::class.java).apply {
            }
            startActivity(intent)
            super.finish()
        } else {

        }
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_action, menu)
        return super.onCreateOptionsMenu(menu)
    }

}



