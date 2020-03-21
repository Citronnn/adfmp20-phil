package com.example.philsapp


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_card_view.view.*


class RVAdapter internal constructor(var cards: ArrayList<SearchCard>, var func: (Int) -> Unit) :
    RecyclerView.Adapter<RVAdapter.CardViewHolder>() {

    class CardViewHolder internal constructor(var cardView: CardView) : RecyclerView.ViewHolder(
        cardView
    )
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val cv = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_card_view, parent, false) as CardView
        cv.setOnClickListener {
            func(Integer.parseInt(cv.positionInList.text.toString()))
            Log.d("kek", cv.positionInList.text.toString())
        }
        return CardViewHolder(cv)
    }

    override fun onBindViewHolder(
        cardViewHolder: CardViewHolder,
        position: Int
    ) {
        val cardView = cardViewHolder.cardView

        val title = cardView.findViewById<View>(R.id.title) as TextView
        title.setText(cards[position].title)
        val positionInList = cardView.findViewById<View>(R.id.positionInList) as TextView
        positionInList.setText((position + 1).toString())
        val content = cardView.findViewById<View>(R.id.type) as TextView
        content.setText(cards[position].type)
        val years = cardView.findViewById<View>(R.id.years) as TextView
        years.setText(cards[position].years)
        if (cards[position].years == null) {
            cardView.findViewById<View>(R.id.yearsLabel).visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

}