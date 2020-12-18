package com.kumsal.kyk


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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


class MainActivity : AppCompatActivity(), OnItemSelectedListener,View.OnClickListener {
    private var toolbar: Toolbar? = null
    private var mViewPager: ViewPager? = null
    private var sectionPagerAdapter: SectionPagerAdapter? = null
    private var mBottomBar: SmoothBottomBar? = null

    private var add: FloatingActionButton? = null
    private var addPost: FloatingActionButton? = null
    private var addMessage: ExtendedFloatingActionButton? = null

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


    var isOpen: Boolean = false
    var interPolator: OvershootInterpolator = OvershootInterpolator()

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

        var view = mNavbar.getHeaderView(0)

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
        initial()
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
    private fun initial() {
        add = findViewById(R.id.fab_add)
        addPost = findViewById(R.id.fab_edit)
        addMessage = findViewById(R.id.fab_message)

        addMessage?.alpha = 0F
        addPost?.alpha = 0F

        addMessage?.translationY = 100F
        addPost?.translationY = 100F

        add?.setOnClickListener(this)
        addPost?.setOnClickListener(this)
        addMessage?.setOnClickListener(this)

    }

    private fun closeFABMenu() {
        isOpen = false
        add?.animate()?.setInterpolator(interPolator)?.rotationBy(45f)?.setDuration(300)?.start()
        addMessage?.animate()?.translationY(100F)?.alpha(0F)?.setInterpolator(interPolator)?.setDuration(300)
            ?.start()
        addPost?.animate()?.translationY(100F)?.alpha(0F)?.setInterpolator(interPolator)?.setDuration(300)
            ?.start()

    }

    private fun showFABMenu() {
        isOpen = true
        add?.animate()?.setInterpolator(interPolator)?.rotationBy(45f)?.setDuration(300)?.start()
        addMessage?.animate()?.translationY(0F)?.alpha(1F)?.setInterpolator(interPolator)?.setDuration(300)
            ?.start()
        addPost?.animate()?.translationY(0F)?.alpha(1F)?.setInterpolator(interPolator)?.setDuration(300)
            ?.start()


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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add ->
                if (!isOpen) {
                    showFABMenu()
                } else {
                    closeFABMenu()
                }
            R.id.fab_message ->
                println("")
            R.id.fab_edit ->
                println("fab2")


        }
    }

}
