package com.example.philsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*


class FiltersActivity : AppCompatActivity() {
    val TAG = "kek"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    fun showFilters() {
        Log.d(TAG, "current")
    }
    fun showGraph() {
        Log.d(TAG, "graph")
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 2)
    }
    fun showTimeLine() {
        Log.d(TAG, "time line")
        val myIntent = Intent(this, TimeLineActivity::class.java)
        startActivityForResult(myIntent, 3)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when(requestCode) {
            1 -> Log.d(TAG, "код 1")
            2 -> Log.d(TAG, "код 2")
            3 -> Log.d(TAG, "код 3")
            else -> Log.d(TAG, "другой")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        navbar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_filters -> showFilters()
                R.id.nav_graph -> showGraph()
                R.id.nav_timeline -> showTimeLine()
                else -> {
                    Log.d("kek", "Назад")
                }
            }
            true
        }
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager) as ViewPager
        mViewPager!!.setAdapter(mSectionsPagerAdapter)

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout

        mViewPager!!.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
    }
}

class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment { // getItem is called to instantiate the fragment for the given page.
// Return a PlaceholderFragment (defined as a static inner class below).
//return PlaceholderFragment.newInstance(position + 1);
        return when (position) {
            0 -> {
                FiltersTab()
            }
            else -> SearchTab()
        }
    }

    override fun getCount(): Int { // Show 3 total pages.
        return 3
    }
}
