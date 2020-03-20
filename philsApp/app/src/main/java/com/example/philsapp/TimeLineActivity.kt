package com.example.philsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_time_line.*

class TimeLineActivity : AppCompatActivity() {
    val TAG = "kek"
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when(requestCode) {
            1 -> Log.d(TAG, data.getStringExtra("filters"))
            else -> Log.d(TAG, "другой")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        filters.setOnClickListener{
            Log.d(TAG, "filters")
            val myIntent = Intent(this, FiltersActivity::class.java)
            startActivityForResult(myIntent, 1)
        }
        gotoGraph.setOnClickListener{
            Log.d(TAG, "graph")
            intent.putExtra("filters", "qweqwe")
            setResult(2, intent)
            finish()
        }
    }
}
