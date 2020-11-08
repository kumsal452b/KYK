package com.kumsal.kyk


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
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
        toolbar = findViewById(R.id.main_activity_toolbar)
        mViewPager = findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mBottomBar = findViewById(R.id.main_activity_bottomBar)
        mFloatingActionButton = findViewById(R.id.fab)
        mDrawerLayout = findViewById(R.id.main_activity_drawer)

        setSupportActionBar(toolbar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        mViewPager.adapter = sectionPagerAdapter

        mBottomBar.setOnTabSelectListener(OnTabSelectListener { tabId: Int ->
            run {
                if (tabId == R.id.tab_home) {
                    mViewPager.setCurrentItem(0)
                }
            }
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
                mBottomBar.setDefaultTabPosition(position)
            }

        })


        mFloatingActionButton.setOnClickListener(View.OnClickListener {
            println("selammmbutton calist")
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}



