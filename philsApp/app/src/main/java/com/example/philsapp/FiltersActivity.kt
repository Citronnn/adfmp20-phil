package com.example.philsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.philsapp.api.Era
import com.example.philsapp.api.NotableIdea
import com.example.philsapp.api.Philosopher
import com.example.philsapp.api.PhilosophicalSchool
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_filters.*


class FiltersActivity : AppCompatActivity() {
    val TAG = "kek"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    object Filters {
        var schools = 1
        var meanings = 0
        var countGT = 20
        var ages = 0
        var topGT = 0
        var yearStart = 300
        var yearEnd = 1000
    }
    object SearchResults {
        var wordForSearch = ""
        var countResults = 0
        var selectedVariant = 0
        var listPhils = arrayListOf<Philosopher>()
        var listSchools = arrayListOf<PhilosophicalSchool>()
        var listIdeas = arrayListOf<NotableIdea>()
        var listEras = arrayListOf<Era>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        returnFromFilters.setOnClickListener {
            intent.putExtra("filters", "filters")
            setResult(1, intent)
            finish()
        }
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

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
            0 -> FiltersTab()
            else -> SearchTab()
        }
    }

    override fun getCount(): Int { // Show 3 total pages.
        return 2
    }
}
