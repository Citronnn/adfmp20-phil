package com.example.philsapp

import android.content.ClipDescription
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.search_bar.*
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView
import xyz.sangcomz.stickytimelineview.model.SectionInfo
import java.util.*
import kotlin.collections.ArrayList


class TimeLineActivity : AppCompatActivity(), OnItemClickListener {
    val TAG = "kek"
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when(resultCode) {
            1 -> Log.d(TAG, data.getStringExtra("filters"))
            3 -> {
                Log.d(TAG, data.getStringExtra("search"))
                layoutForSearch.visibility = View.VISIBLE
                textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
                textSearchAllCount.text = FiltersActivity.SearchResults.countResults.toString()
            }
            4 -> Log.d(TAG, data.getStringExtra("data"))
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
            clearSearchResults()
            intent.putExtra("filters", "qweqwe")
            setResult(2, intent)
            finish()
        }
        buttonSearchLeft.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                0 -> FiltersActivity.SearchResults.selectedVariant = FiltersActivity.SearchResults.countResults - 1
                else -> FiltersActivity.SearchResults.selectedVariant--
            }
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
        }
        buttonSearchRight.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                FiltersActivity.SearchResults.countResults - 1 -> FiltersActivity.SearchResults.selectedVariant = 0
                else -> FiltersActivity.SearchResults.selectedVariant++
            }
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
        }
        buttonSearchClose.setOnClickListener {
            clearSearchResults()
        }
        val recyclerView: TimeLineRecyclerView = findViewById(R.id.recycler_view)

        //Currently only LinearLayoutManager is supported.
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false)

        //Get data
        val philsList = getPhilsList()
        //Add RecyclerSectionItemDecoration.SectionCallback
        recyclerView.addItemDecoration(getSectionCallback(philsList))
        //Set Adapter
        recyclerView.adapter = PhilsAdapter(layoutInflater,
            philsList,
            R.layout.recycler_row, this)
    }
    fun clearSearchResults() {
        layoutForSearch.visibility = View.GONE
        FiltersActivity.SearchResults.wordForSearch = ""
        FiltersActivity.SearchResults.countResults = 0
        FiltersActivity.SearchResults.selectedVariant = 0
        FiltersActivity.SearchResults.listVariants = arrayListOf<Any>()
    }
    override fun onItemClicked(user: Phil) {
        Log.d("kek", user.name)
        MainActivity.ForSearchResults.selectedPhil = user
        MainActivity.ForSearchResults.fromActivity = "timeline"
        clearSearchResults()
        val myIntent = Intent(this, InfoCardActivity::class.java)
        startActivityForResult(myIntent, 4)
    }
    private fun getPhilsList(): List<Phil> = PhilsRepo().philsList


    //Get SectionCallback method
    private fun getSectionCallback(philsList: List<Phil>): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {
            //In your data, implement a method to determine if this is a section.
            override fun isSection(position: Int): Boolean =
                philsList[position].startYear != philsList[position - 1].startYear

            //Implement a method that returns a SectionHeader.
            override fun getSectionHeader(position: Int): SectionInfo? =
                SectionInfo(philsList[position].startYear, "")
        }
    }

}

interface OnItemClickListener{
    fun onItemClicked(user: Phil)
}
data class Phil(
    val type: String,
    val startYear: String,
    val name: String)
class PhilsAdapter(private val layoutInflater: LayoutInflater,
                    private val philsList: List<Phil>,
                    @param:LayoutRes private val rowLayout: Int,
                    val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<PhilsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = layoutInflater.inflate(rowLayout,
            parent,
            false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val phil = philsList[position]
        holder.fullName.text = phil.name
        holder.bind(phil,itemClickListener)

        when(phil.type) {
            "school" -> holder.fullName.setBackgroundColor(Color.parseColor("#f4e893"))
            "phil" -> holder.fullName.setBackgroundColor(Color.parseColor("#93f49a"))
            "age" -> holder.fullName.setBackgroundColor(Color.parseColor("#f49393"))
        }
    }

    override fun getItemCount(): Int = philsList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fullName: TextView = view.findViewById<View>(R.id.full_name_tv) as TextView
        fun bind(user: Phil,clickListener: OnItemClickListener)
        {
            itemView.setOnClickListener {
                clickListener.onItemClicked(user)
            }
        }
    }
}
class PhilsRepo {
    //solo
    val philsList: List<Phil>
        get() {
            val philsList = ArrayList<Phil>()
            philsList.add(Phil( "phil","2215", "Философ3"))
            philsList.add(Phil("phil","1946", "Философ1"))
            philsList.add(Phil("school","1946", "Школа1"))

            philsList.add(Phil( "phil","1244", "Философ2"))
            philsList.add(Phil("school","1244", "Школа2"))
            philsList.add(Phil( "phil","800", "Философ4"))
            philsList.add(Phil( "age","400", "Эпоха3"))
            philsList.add(Phil( "age","-400", "Эпоха2"))
            philsList.add(Phil( "age","-1400", "Эпоха1"))

            return philsList
        }
}