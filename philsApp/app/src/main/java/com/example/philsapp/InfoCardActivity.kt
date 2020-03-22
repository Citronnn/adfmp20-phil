package com.example.philsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_info_card.*

class InfoCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_card)
        titleContent.text = MainActivity().nodes[MainActivity.ForSearchResults.selectedNode].text
        abstractContent.text = "Краткое описание"
        wikiContent.text = "qwe.ru"
        ratingWikiContent.text = "155"
        genderPhil.text = "Мужской"
        birthDayPhil.text = "23.12.1092"
        deathDayPhil.text = "21.11.1159"
        nationalityPhil.text = "Шотландец"
        listNamesPhil.text = "Андрей, Артем"
        listBirthPlacesPhil.text = "Италия, Франция"
        listDeathPlacesPhil.text = "Англия, Киевская Русь"
        influencePhil.text = "Сократ"
        worksPhil.text = "Работа1, Работа2"
        ideasPhil.text = "Идея1, Идея2"
        interestsPhil.text = "Интерес1, Интерес2"
        schoolsPhil.text = "Школа1, Школа2"
        Log.d("kek", "qweqwe ${MainActivity.ForSearchResults.selectedNode} ${MainActivity().nodes[MainActivity.ForSearchResults.selectedNode].type}")
        if (MainActivity().nodes[MainActivity.ForSearchResults.selectedNode].type != "phil") {
            layoutForGender.visibility = View.GONE
            layoutForBirthDay.visibility = View.GONE
            layoutForDeathDay.visibility = View.GONE
            layoutForNationality.visibility = View.GONE
            nationalityPhil.visibility = View.GONE
            layoutForListNames.visibility = View.GONE
            layoutForListBirthPlaces.visibility = View.GONE
            layoutForListDeathPlaces.visibility = View.GONE
            layoutForInfluence.visibility = View.GONE
            layoutForWorks.visibility = View.GONE
            layoutForIdeas.visibility = View.GONE
            layoutForInterests.visibility = View.GONE
            layoutForSchools.visibility = View.GONE
        }
        returnFromInfoPage.setOnClickListener {
            intent.putExtra("data", "ok")
            setResult(4, intent)
            finish()
        }
    }
}