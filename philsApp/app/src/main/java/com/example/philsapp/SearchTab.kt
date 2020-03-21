package com.example.philsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_card_view.view.*


/**
 * A simple [Fragment] subclass.
 */
class SearchTab : Fragment() {
    private var cards: ArrayList<SearchCard>? = null
    private var rv: RecyclerView? = null
    val activity = FiltersActivity()
    object SearchResultsObject {
        var wordForSearch = ""
        var countResults = 0
        var selectedVariant = 0
        var listVariants = arrayListOf<Any>()
    }
    fun onCardClickListener(position: Int): Unit {
        Log.d("kek", position.toString())
        activity.returnAfterSearch()
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
        buttonForSearch.setOnClickListener {
            SearchResultsObject.wordForSearch = textForSearch.text.toString()
            activity.setSearchResults(SearchResultsObject)
        }
        val data = activity.getSearch()
        SearchResultsObject.wordForSearch = data.wordForSearch
        SearchResultsObject.countResults = data.countResults
        SearchResultsObject.selectedVariant = data.selectedVariant
        SearchResultsObject.listVariants = data.listVariants
        initializeData()
        initializeAdapter()
        return view
    }
    private fun initializeData() {
        cards = ArrayList<SearchCard>()
        (cards as ArrayList<SearchCard>).add(SearchCard("Сократ", "Философ", "1234-5322"))
        (cards as ArrayList<SearchCard>).add(SearchCard("Марксизм", "Школа", null))
        (cards as ArrayList<SearchCard>).add(SearchCard("Античность", "Эпоха", null))
    }

    private fun initializeAdapter() {
        val adapter = cards?.let { RVAdapter(it, ::onCardClickListener) }
        rv!!.adapter = adapter
    }

}
