package com.example.philsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.philsapp.api.*
import kotlinx.android.synthetic.main.search_card_view.view.*
import java.lang.Math.abs


/**
 * A simple [Fragment] subclass.
 */
class SearchTab : Fragment() {
    private var cards: ArrayList<SearchCard>? = null
    private var rv: RecyclerView? = null
    fun onCardClickListener(position: Int): Unit {
        Log.d("kek", position.toString())
        FiltersActivity.SearchResults.selectedVariant = position - 1
        getActivity()?.intent?.putExtra("search", "search")
        getActivity()?.setResult(3, getActivity()?.intent)
        getActivity()?.finish()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_tab, container, false)
        rv = view.findViewById<RecyclerView>(R.id.rv) as RecyclerView
        val llm = LinearLayoutManager(view.context)
        rv!!.setLayoutManager(llm)
        val textForSearch = view.findViewById<EditText>(R.id.textForSearch)
        val buttonForSearch = view.findViewById<ImageButton>(R.id.buttonForSearch)
        val db = Database(getActivity()!!.applicationContext)
        buttonForSearch.setOnClickListener {
            FiltersActivity.SearchResults.wordForSearch = textForSearch.text.toString()
            FiltersActivity.SearchResults.listPhils = arrayListOf()
            FiltersActivity.SearchResults.listSchools = arrayListOf()
            FiltersActivity.SearchResults.listIdeas = arrayListOf()
            FiltersActivity.SearchResults.listEras = arrayListOf()
            FiltersActivity.SearchResults.listPhils = db.getAllPhilosophers(
                Filter(filter = arrayOf(
                    FilterBy("name", Operator.CONTAINS, FiltersActivity.SearchResults.wordForSearch))
                )
            )
            if (FiltersActivity.Filters.schools == 1) {
                FiltersActivity.SearchResults.listSchools = db.getAllSchools(
                    Filter(filter = arrayOf(
                        FilterBy("philosophicalSchool", Operator.CONTAINS, FiltersActivity.SearchResults.wordForSearch))
                    )
                )
            }
            if (FiltersActivity.Filters.meanings == 1) {

                FiltersActivity.SearchResults.listIdeas = db.getAllIdeas(
                    Filter(filter = arrayOf(
                        FilterBy("notableIdea", Operator.CONTAINS, FiltersActivity.SearchResults.wordForSearch))
                    )
                )
            }
            if (FiltersActivity.Filters.ages == 1) {
                FiltersActivity.SearchResults.listEras = db.getAllEras(
                    Filter(filter = arrayOf(
                        FilterBy("era", Operator.CONTAINS, FiltersActivity.SearchResults.wordForSearch))
                    )
                )
            }
            FiltersActivity.SearchResults.countResults = FiltersActivity.SearchResults.listEras.size +
                    FiltersActivity.SearchResults.listSchools.size +
                    FiltersActivity.SearchResults.listPhils.size +
                    FiltersActivity.SearchResults.listIdeas.size
            initializeData(view)
            initializeAdapter()

        }
        initializeData(view)
        initializeAdapter()
        return view
    }
    private fun initializeData(view: View) {
        val noSearchResultsLabel = view.findViewById<TextView>(R.id.noSearchResultsLabel)
        noSearchResultsLabel.visibility = View.GONE
        if (FiltersActivity.SearchResults.countResults == 0) {
            noSearchResultsLabel.visibility = View.VISIBLE
        }
        cards = ArrayList<SearchCard>()
        FiltersActivity.SearchResults.listPhils.forEach {
            Log.d("kek", it.name)
            var years = "Неизвестно"
            if (it.birthDate != null && it.deathDate != null) {
                val yearBirth = it.birthDate.substring(0, 4).toInt()
                val yearDeath = it.deathDate.substring(0, 4).toInt()
                years = "${if (yearBirth >= 0) "$yearBirth н.э." else "${abs(yearBirth)} до н.э."} - ${if (yearDeath >= 0) "$yearDeath н.э." else "${abs(yearDeath)} до н.э."}"
            }
            (cards as ArrayList<SearchCard>).add(SearchCard((it.name), "Философ", years))
        }
        FiltersActivity.SearchResults.listSchools.forEach {
            (cards as ArrayList<SearchCard>).add(SearchCard((it.name), "Школа", null))
        }
        FiltersActivity.SearchResults.listIdeas.forEach {
            (cards as ArrayList<SearchCard>).add(SearchCard((it.name), "Понятие", null))
        }
        FiltersActivity.SearchResults.listEras.forEach {
            (cards as ArrayList<SearchCard>).add(SearchCard((it.name), "Эра", null))
        }
    }

    private fun initializeAdapter() {
        val adapter = cards?.let { RVAdapter(it, ::onCardClickListener) }
        rv!!.adapter = adapter
    }

}
