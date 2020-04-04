package com.example.philsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.philsapp.ClickableLinks.reformatText
import com.example.philsapp.api.*
import kotlinx.android.synthetic.main.activity_info_card.*
import kotlin.math.abs


class InfoCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_card)
        var onlyHead = false
        val db = Database(this)
        if (MainActivity.onClickResults.selectedType === "phil") {
            val data = db.getOnePhilosopher(MainActivity.onClickResults.selectedId)
            setFieldsForPhil(data.name, data.abstract, data.wikipediaLink, data.wikiPagePopularity,
                data.gender, data.birthDate, data.deathDate, data.nationalities,data.names, data.wasBorn,
                data.died, data.wasInfluencedBy, data.influencedOn, data.notableIdeas, data.mainInterests,
                data.schools)
        } else {
            onlyHead = true
            when (MainActivity.onClickResults.selectedType) {
                "school" -> {
                    val data = db.getOneSchool(MainActivity.onClickResults.selectedName)
                    setFieldsForOther(data.name, data.abstract, data.wikipediaLink, data.wikiPagePopularity)
                }
                "meaning" -> {
                    val data = db.getOneIdea(MainActivity.onClickResults.selectedName)
                    setFieldsForOther(data.name, data.abstract, data.wikipediaLink, data.wikiPagePopularity)
                }
                "age" -> {
                    val data = db.getOneEra(MainActivity.onClickResults.selectedName)
                    setFieldsForOther(data.name, data.abstract, data.wikipediaLink, data.wikiPagePopularity)
                }
            }
        }
       if (onlyHead) {
            layoutForGender.visibility = View.GONE
            layoutForBirthDay.visibility = View.GONE
            layoutForDeathDay.visibility = View.GONE
            layoutForNationality.visibility = View.GONE
            nationalityPhil.visibility = View.GONE
            layoutForListNames.visibility = View.GONE
            layoutForListBirthPlaces.visibility = View.GONE
           layoutForInfluenceByHim.visibility = View.GONE
            layoutForListDeathPlaces.visibility = View.GONE
            layoutForInfluence.visibility = View.GONE
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
    fun setFieldsForOther(name: String, abstract: String?, wiki: String?, ratingWiki: Int?) {
        titleContent.text = name
        if (abstract != null) {
            abstractContent.text = abstract
        } else {
            layoutForAbstract.visibility = View.GONE
        }
        if (wiki != null) {
            wikiContent.text = Html.fromHtml("<a href=\"$wiki\">$wiki</a>", null, null)
            wikiContent.linksClickable = true
            wikiContent.movementMethod = LinkMovementMethod.getInstance()
            val text: CharSequence = wikiContent.text
            if (text is Spannable) {
                wikiContent.text = reformatText(text)
            }
        } else {
            layoutForWiki.visibility = View.GONE
        }
        if (ratingWiki != null && ratingWiki>0) {
            ratingWikiContent.text = ratingWiki.toString()
        } else {
            layoutForWikiRating.visibility = View.GONE
        }
    }
    fun setFieldsForPhil(name: String, abstract: String?, wiki: String?, ratingWiki: Int?,
                         gender: String?, birthDay: String?, deathDay: String?,
                         nationalities: ArrayList<String>?, names: ArrayList<String>?,
                         birthPlaces: ArrayList<Place>?, deathPlaces: ArrayList<Place>?,
                         influence: ArrayList<Philosopher>?, influenceByHim: ArrayList<Philosopher>?,
                         ideas: ArrayList<NotableIdea>?, interests: ArrayList<MainInterest>?,
                         schools: ArrayList<PhilosophicalSchool>?
                         )  {

        titleContent.text = name
        if (abstract != null) {
            abstractContent.text = abstract
        } else {
            layoutForAbstract.visibility = View.GONE
        }
        if (wiki != null) {
            wikiContent.text = Html.fromHtml("<a href=\"$wiki\">$wiki</a>", null, null)
            wikiContent.linksClickable = true
            wikiContent.movementMethod = LinkMovementMethod.getInstance()
            val text: CharSequence = wikiContent.text
            if (text is Spannable) {
                wikiContent.text = reformatText(text)
            }
        } else {
            layoutForWiki.visibility = View.GONE
        }
        if (ratingWiki != null) {
            ratingWikiContent.text = ratingWiki.toString()
        } else {
            layoutForWikiRating.visibility = View.GONE
        }
        if (gender != null) {
            genderPhil.text = gender
        } else {
            layoutForGender.visibility = View.GONE
        }
        if (birthDay != null) {
            val year = birthDay.substring(0,4).toInt()
            birthDayPhil.text = if (year >= 0) "$year н.э." else "${abs(year)} до н.э."
        } else {
            layoutForBirthDay.visibility = View.GONE
        }
        if (deathDay != null) {
            val year = deathDay.substring(0,4).toInt()
            deathDayPhil.text = if (year >= 0) "$year н.э." else "${abs(year)} до н.э."
        } else {
            layoutForDeathDay.visibility = View.GONE
        }
        if (nationalities != null && nationalities.size > 0) {
            var natList = ""
            var count = 0
            nationalities.forEach {
                natList += it
                count++
                if (count < nationalities.size) {
                    natList += ", "
                }
            }
            nationalityPhil.text = natList
        } else {
            layoutForNationality.visibility = View.GONE
        }
        if (names != null && names.size > 0) {
            var namesList = ""
            var count = 0
            names.forEach {
                namesList += it
                count++
                if (count < names.size) {
                    namesList += ", "
                }
            }
            listNamesPhil.text = namesList
        } else {
            layoutForListNames.visibility = View.GONE
        }
        if (birthPlaces != null && birthPlaces.size > 0) {
            var birthList = ""
            var count = 0
            birthPlaces.forEach {
                birthList += it.name
                count++
                if (count < birthPlaces.size) {
                    birthList += ", "
                }
            }
            listBirthPlacesPhil.text = birthList
        } else {
            layoutForListBirthPlaces.visibility = View.GONE
        }
        if (deathPlaces != null && deathPlaces.size > 0) {
            var deathList = ""
            var count = 0
            deathPlaces.forEach {
                deathList += it.name
                count++
                if (count < deathPlaces.size) {
                    deathList += ", "
                }
            }
            listDeathPlacesPhil.text = deathList
        } else {
            layoutForListDeathPlaces.visibility = View.GONE
        }
        if (influence != null && influence.size > 0) {
            var philsList = ""
            var count = 0
            influence.forEach {
                philsList += it.name
                count++
                if (count < influence.size) {
                    philsList += ", "
                }
            }
            influencePhil.text = philsList
        } else {
            layoutForInfluence.visibility = View.GONE
        }
        if (influenceByHim != null && influenceByHim.size > 0) {
            var philsList = ""
            var count = 0
            influenceByHim.forEach {
                philsList += it.name
                count++
                if (count < influenceByHim.size) {
                    philsList += ", "
                }
            }
            influenceByHimPhil.text = philsList
        } else {
            layoutForInfluenceByHim.visibility = View.GONE
        }
        if (ideas != null && ideas.size > 0) {
            var ideasList = ""
            var count = 0
            ideas.forEach {
                ideasList += it.name
                count++
                if (count < ideas.size) {
                    ideasList += ", "
                }
            }
            ideasPhil.text = ideasList
        } else {
            layoutForIdeas.visibility = View.GONE
        }
        if (interests != null && interests.size > 0) {
            var interestsList = ""
            var count = 0
            interests.forEach {
                interestsList += it.name
                count++
                if (count < interests.size) {
                    interestsList += ", "
                }
            }
            interestsPhil.text = interestsList
        } else {
            layoutForInterests.visibility = View.GONE
        }
        if (schools != null && schools.size > 0) {
            var schoolsList = ""
            var count = 0
            schools.forEach {
                schoolsList += it.name
                count++
                if (count < schools.size) {
                    schoolsList += ", "
                }
            }
            schoolsPhil.text = schoolsList
        } else {
            layoutForSchools.visibility = View.GONE
        }
    }
}
