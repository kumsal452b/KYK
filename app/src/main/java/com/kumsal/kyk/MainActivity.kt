package com.kumsal.kyk


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnTabSelectListener
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var mViewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var mBottomBar: SmoothBottomBar
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var mAuth:FirebaseAuth
    private var mUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialzed
        toolbar = findViewById(R.id.main_activity_toolbar)
        mViewPager = findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mBottomBar = findViewById(R.id.main_activity_bottomBar)
        mFloatingActionButton = findViewById(R.id.fab)
        mDrawerLayout = findViewById(R.id.main_activity_drawer)
        mAuth = FirebaseAuth.getInstance()
        val mUser1:FirebaseUser?=mAuth.currentUser
        mUser=mUser1

        setSupportActionBar(toolbar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle.drawerArrowDrawable.color=Color.WHITE
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        mViewPager.adapter = sectionPagerAdapter


        mBottomBar.onItemSelectedListener = this
        mBottomBar.setOnClickListener {
            View.OnClickListener {
                println(mBottomBar.onItemSelected)
            }
        }
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

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
                mBottomBar.itemActiveIndex = position
            }

        })

        FirebaseAuth.getInstance().signOut()
        mFloatingActionButton.setOnClickListener(View.OnClickListener {
            println("selammmbutton calist")
        })
        setupWindowAnimations()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelect(pos: Int): Boolean {
        if (pos == 0) {
            mViewPager.setCurrentItem(0)
        }
        if (pos == 1) {
            mViewPager.setCurrentItem(1)
        }
        if (pos == 2) {
            mViewPager.setCurrentItem(2)
        }
        if (pos == 3) {
            mViewPager.setCurrentItem(3)
        }
        return true
    }

    override fun onStart() {
        if (mUser==null){
            val intent:Intent=Intent(this,StarterActivity::class.java ).apply {
            }
            startActivity(intent)
            setupWindowAnimations()
            super.finish()
        }
        super.onStart()
    }
    fun setupWindowAnimations(){
        val fade:Slide=Slide()
        println(fade)
            window.enterTransition=fade
    }

}



