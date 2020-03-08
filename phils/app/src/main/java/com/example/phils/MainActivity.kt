package com.example.phils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    fun showFilterFragment() {
        val transaction = manager.beginTransaction()
        val fragment = FilterFragment()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun showGraphFragment() {
        val transaction = manager.beginTransaction()
        val fragment = GraphFragment()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun showTimeLineFragment() {
        val transaction = manager.beginTransaction()
        val fragment = TimeLineFragment()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFilterFragment()
        navbar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_filters -> showFilterFragment()
                R.id.nav_graph -> showGraphFragment()
                R.id.nav_timeline -> showTimeLineFragment()
                else -> {
                    Log.d("kek","Назад")
                }
            }
            true

        }
    }
}
