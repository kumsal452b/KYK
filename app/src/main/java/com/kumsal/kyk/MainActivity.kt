package com.kumsal.kyk


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var mViewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var mTableLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.main_appbar)
        mViewPager=findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter= SectionPagerAdapter(supportFragmentManager)
        mTableLayout=findViewById(R.id.main_activity_tabLayout)
//        setSupportActionBar(toolbar)
        mViewPager.adapter=sectionPagerAdapter
        mTableLayout.setBackgroundColor(Color.BLACK)

        mTableLayout.setupWithViewPager(mViewPager)


    }
}