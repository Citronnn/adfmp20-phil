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
import com.example.philsapp.api.*
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.search_bar.*
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView
import xyz.sangcomz.stickytimelineview.model.SectionInfo
import java.util.*
import kotlin.collections.ArrayList


class TimeLineActivity : AppCompatActivity(), OnItemClickListener {
    val TAG = "kek"
    object CardsForTimeLine {
        var arrayCards = ArrayList<Phil>()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        var forSearch = false
        when(resultCode) {
            1 -> Log.d(TAG, data.getStringExtra("filters"))
            3 -> {
                Log.d(TAG, data.getStringExtra("search"))
                layoutForSearch.visibility = View.VISIBLE
                textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
                textSearchAllCount.text = FiltersActivity.SearchResults.countResults.toString()
                forSearch = true
            }
            4 -> Log.d(TAG, data.getStringExtra("data"))
            else -> Log.d(TAG, "другой")
        }
        initializeData(forSearch)
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
            initializeData(true)
        }
        buttonSearchRight.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                FiltersActivity.SearchResults.countResults - 1 -> FiltersActivity.SearchResults.selectedVariant = 0
                else -> FiltersActivity.SearchResults.selectedVariant++
            }
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
            initializeData(true)
        }
        buttonSearchClose.setOnClickListener {
            clearSearchResults()
            initializeData(false)
        }
        initializeData(false, true)
    }
    fun initializeData(forSearch:Boolean, firstTime:Boolean = false) {
        val recyclerView: TimeLineRecyclerView = findViewById(R.id.recycler_view)

        //Currently only LinearLayoutManager is supported.
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false)

        //Get data
        CardsForTimeLine.arrayCards = arrayListOf()
        val db = Database(this)
        if (forSearch) {
            var current = FiltersActivity.SearchResults.selectedVariant
            var typeForSearch = "phil"
            if (current > FiltersActivity.SearchResults.listPhils.size - 1) {
                typeForSearch = "school"
                current -= FiltersActivity.SearchResults.listPhils.size
                if (current > FiltersActivity.SearchResults.listSchools.size - 1) {
                    typeForSearch = "meaning"
                    current -= FiltersActivity.SearchResults.listSchools.size
                    if (current > FiltersActivity.SearchResults.listIdeas.size- 1) {
                        current -= FiltersActivity.SearchResults.listIdeas.size
                        typeForSearch = "age"
                    }
                }
            }
            when (typeForSearch) {
                "phil" -> {
                    val data = FiltersActivity.SearchResults.listPhils[current]
                    CardsForTimeLine.arrayCards.add(Phil("phil",
                        data.birthDate ?: "Неизвестно",
                        data.name, data.wikiPageId))
                }
                "school" -> {
                    val data = FiltersActivity.SearchResults.listSchools[current]
                    CardsForTimeLine.arrayCards.add(Phil("school",
                        "Неизвестно",
                        data.name, 0))
                }
                "meaning" -> {
                    val data = FiltersActivity.SearchResults.listIdeas[current]
                    CardsForTimeLine.arrayCards.add(Phil("meaning",
                        "Неизвестно",
                        data.name, 0))
                }
                "age" -> {
                    val data = FiltersActivity.SearchResults.listEras[current]
                    CardsForTimeLine.arrayCards.add(Phil("age",
                        "Неизвестно",
                        data.name, 0))
                }
            }
        } else {
            val data = db.getAllPhilosophers(
                Filter(order = arrayOf(
                        OrderBy(
                            "birthDate",
                            Order.ASC
                        )
                    ),
                    filter = arrayOf(FilterBy("birthDate", Operator.NOTNULL),
                        FilterBy("birthDate", Operator.GT, dateToJd(FiltersActivity.Filters.yearStart)),
                        FilterBy("birthDate", Operator.LT, dateToJd(FiltersActivity.Filters.yearEnd)))
                )
                //Filter(
                    //    ,filter = arrayOf(FilterBy("birthDate", Operator.GT, FiltersActivity.Filters.yearStart),
                    //      FilterBy("birthDate", Operator.LT, FiltersActivity.Filters.yearEnd))
                //)
            )
            val schools = mutableSetOf<PhilosophicalSchool>()
            val meanings = mutableSetOf<NotableIdea>()
            val eras = mutableSetOf<Era>()
            data.forEach { it ->
                    CardsForTimeLine.arrayCards.add(
                        Phil(
                            "phil",
                            it.birthDate ?: "",
                            it.name, it.wikiPageId
                        )
                    )
                if (FiltersActivity.Filters.schools == 1) {
                    it.schools.forEach {
                        schools.add(it)
                    }
                }
                if (FiltersActivity.Filters.meanings == 1) {
                    it.notableIdeas.forEach {
                        meanings.add(it)
                    }
                }
                if (FiltersActivity.Filters.ages == 1) {
                    it.eras.forEach {
                        eras.add(it)
                    }
                }
            }
            schools.forEach {
                if (it.firstPhilosopher != null) {
                    CardsForTimeLine.arrayCards.add(
                        Phil(
                            "school",
                            it.firstPhilosopher!!.birthDate ?: "",
                            it.name, 0
                        )
                    )
                }
            }
            meanings.forEach {
                if (it.firstPhilosopher != null) {
                    CardsForTimeLine.arrayCards.add(
                        Phil(
                            "meaning",
                            it.firstPhilosopher!!.birthDate ?: "",
                            it.name, 0
                        )
                    )
                }
            }
            eras.forEach {
                if (it.firstPhilosopher != null) {
                    CardsForTimeLine.arrayCards.add(
                        Phil(
                            "age",
                            it.firstPhilosopher!!.birthDate ?: "",
                            it.name, 0
                        )
                    )
                }
            }
        }
        CardsForTimeLine.arrayCards.sortBy {
            it.startYear
        }
        //Add RecyclerSectionItemDecoration.SectionCallback
        if (firstTime) recyclerView.addItemDecoration(getSectionCallback(CardsForTimeLine.arrayCards))
        //Set Adapter
        recyclerView.adapter = PhilsAdapter(layoutInflater,
            CardsForTimeLine.arrayCards,
            R.layout.recycler_row, this)
        recyclerView.adapter!!.notifyDataSetChanged()
    }
    fun clearSearchResults() {
        layoutForSearch.visibility = View.GONE
        FiltersActivity.SearchResults.wordForSearch = ""
        FiltersActivity.SearchResults.countResults = 0
        FiltersActivity.SearchResults.selectedVariant = 0
        FiltersActivity.SearchResults.listPhils = arrayListOf()
        FiltersActivity.SearchResults.listSchools = arrayListOf()
        FiltersActivity.SearchResults.listIdeas = arrayListOf()
        FiltersActivity.SearchResults.listEras= arrayListOf()
    }
    override fun onItemClicked(user: Phil) {
        MainActivity.onClickResults.selectedId = user.id
        MainActivity.onClickResults.selectedType = user.type
        MainActivity.onClickResults.selectedName = user.name
        clearSearchResults()
        val myIntent = Intent(this, InfoCardActivity::class.java)
        startActivityForResult(myIntent, 4)
    }


    //Get SectionCallback method
    private fun getSectionCallback(philsList: List<Phil>): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {
            //In your data, implement a method to determine if this is a section.
            override fun isSection(position: Int): Boolean =
                CardsForTimeLine.arrayCards[position].startYear !=
                        CardsForTimeLine.arrayCards[position - 1].startYear

            //Implement a method that returns a SectionHeader.
            override fun getSectionHeader(position: Int): SectionInfo? =
                SectionInfo(CardsForTimeLine.arrayCards[position].startYear, "")
        }
    }

}

interface OnItemClickListener{
    fun onItemClicked(user: Phil)
}
data class Phil(
    val type: String,
    val startYear: String,
    val name: String,
    val id: Int)
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
            "meaning" -> holder.fullName.setBackgroundColor(Color.parseColor("#61d2fd"))
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
