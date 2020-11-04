package com.kumsal.kyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.Toolbar

import androidx.viewpager.widget.ViewPager
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var mViewPager: ViewPager
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var mTableLayout: TableLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.main_appbar)
        mViewPager=findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter= SectionPagerAdapter()
        mTableLayout=findViewById(R.id.main_activity_tabLayout)

        setSupportActionBar(toolbar)

    }
}