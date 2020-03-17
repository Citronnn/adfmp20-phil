package com.example.philsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




/**
 * A simple [Fragment] subclass.
 */
class SearchTab : Fragment() {
    private var cards: ArrayList<SearchCard>? = null
    private var rv: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_tab, container, false)
        rv = view.findViewById<RecyclerView>(R.id.rv) as RecyclerView

        val llm = LinearLayoutManager(view.context)
        rv!!.setLayoutManager(llm)

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
        val adapter = cards?.let { RVAdapter(it) }
        rv!!.adapter = adapter
    }

}
