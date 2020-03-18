package com.example.philsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class TimeLineActivity : AppCompatActivity() {
    val TAG = "kek"
    fun showFilters() {
        Log.d(TAG, "filters")
        val myIntent = Intent(this, FiltersActivity::class.java)
        startActivityForResult(myIntent, 1)
    }
    fun showGraph() {
        Log.d(TAG, "graph")
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 2)
    }
    fun showTimeLine() {
        Log.d(TAG, "current")
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
        setContentView(R.layout.activity_time_line)
        navbar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_filters -> showFilters()
                R.id.nav_graph -> showGraph()
                R.id.nav_timeline -> showTimeLine()
                else -> {
                    Log.d("kek","Назад")
                }
            }
            true

        }
    }
}
