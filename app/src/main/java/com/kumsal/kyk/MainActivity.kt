package com.kumsal.kyk


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnTabSelectListener

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var mViewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var mBottomBar: BottomBar
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialzed
        toolbar = findViewById(R.id.main_appbar)
        mViewPager = findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mBottomBar = findViewById(R.id.main_activity_bottomBar)
        mFloatingActionButton=findViewById(R.id.fab)
        mDrawerLayout=findViewById(R.id.main_activity_drawer)

        setSupportActionBar(toolbar)
        actionBarDrawerToggle= ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close)
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        mViewPager.adapter = sectionPagerAdapter
        mBottomBar.setBackgroundColor(Color.BLUE)
        mBottomBar.setOnTabSelectListener(OnTabSelectListener { tabId: Int ->
            run {
                if (tabId == R.id.tab_home) {

                }
            }
        })
        mViewPager.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            println("selam")
        }


        mFloatingActionButton.setOnClickListener(View.OnClickListener {
            println("selammmbutton calist")
        })
    }
}



