package com.kumsal.kyk


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnTabSelectListener

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var mViewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var mBottomBar: BottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialzed
        toolbar=findViewById(R.id.main_appbar)
        mViewPager=findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter= SectionPagerAdapter(supportFragmentManager)
        mBottomBar=findViewById(R.id.main_activity_bottomBar)
        mViewPager.adapter=sectionPagerAdapter
        setSupportActionBar(toolbar)
        mBottomBar.setBackgroundColor(Color.BLUE)
        mBottomBar.setOnTabSelectListener(OnTabSelectListener { tabId: Int ->
            run {
                if (tabId == R.id.tab_favorites) {

                }
            }
        })




    }
}