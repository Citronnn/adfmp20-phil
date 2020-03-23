package com.example.philsapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.appyvet.materialrangebar.RangeBar
import com.appyvet.materialrangebar.RangeBar.OnRangeBarChangeListener


/**
 * A simple [Fragment] subclass.
 */
class FiltersTab : Fragment() {

    object FiltersObject {
        var schools = 1
        var meanings = 0
        var ages = 0
        var topGT = 0
        var countGT: Int = 20
        var yearStart = 300
        var yearEnd = 1000
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filters_tab, container, false)
        val rangeBar = view.findViewById<RangeBar>(R.id.rangeBar)
        val schoolsFilter = view.findViewById<CheckBox>(R.id.schoolsFilter)
        val meansFilter = view.findViewById<CheckBox>(R.id.meansFilter)
        val agesFilter = view.findViewById<CheckBox>(R.id.agesFilter)
        val googleTrendsFilter = view.findViewById<CheckBox>(R.id.googleTrendsFilter)
        val editCount = view.findViewById<EditText>(R.id.editCount)
        val increaseButton = view.findViewById<ImageButton>(R.id.increaseButton)
        val decreaseButton = view.findViewById<ImageButton>(R.id.decreaseButton)
        val layoutForGoogleTrends = view.findViewById<LinearLayout>(R.id.layoutForGoogleTrends)
        val buttonClearFilters: Button = view.findViewById<Button>(R.id.buttonClearFilters)
        buttonClearFilters.setOnClickListener {
            dropFilters()
            setFilters(rangeBar, schoolsFilter,
                meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends,
                editCount)
        }
        rangeBar.tickStart = (-800).toFloat()
        rangeBar.tickEnd = 2000F
        setFilters(rangeBar, schoolsFilter,
            meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends,
            editCount)
        editCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                FiltersActivity.Filters.countGT = Integer.parseInt(editCount.text.toString())
                if (Integer.parseInt(editCount.text.toString()) > 25) {
                    FiltersActivity.Filters.countGT = 25
                    editCount.setText("25")
                }
                if (Integer.parseInt(editCount.text.toString()) < 0) {
                    FiltersActivity.Filters.countGT = 0
                    editCount.setText("0")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })
        increaseButton.setOnClickListener {
            editCount.setText("${Integer.parseInt(editCount.text.toString()) + 1}")
        }
        decreaseButton.setOnClickListener {
            editCount.setText("${Integer.parseInt(editCount.text.toString()) - 1}")
        }
        rangeBar.setOnRangeBarChangeListener(object : OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String,
                rightPinValue: String
            ) {
                FiltersActivity.Filters.yearStart = leftPinIndex.toInt()
                FiltersActivity.Filters.yearEnd = rightPinIndex.toInt()
                Log.d("kek", "$leftPinIndex, $rightPinIndex")
            }

            override fun onTouchEnded(rangeBar: RangeBar) {}
            override fun onTouchStarted(rangeBar: RangeBar) {}
        })
        schoolsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "школы активно")
                FiltersActivity.Filters.schools = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "школы не активно")
                FiltersActivity.Filters.schools = 0
            }
        })
        meansFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "понятия активно")
                FiltersActivity.Filters.meanings = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "понятия не активно")
                FiltersActivity.Filters.meanings = 0
            }
        })
        agesFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "эпохи активно")
                FiltersActivity.Filters.ages = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "эпохи не активно")
                FiltersActivity.Filters.ages = 0
            }
        })
        googleTrendsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "топ активно")
                layoutForGoogleTrends.visibility = View.VISIBLE
                FiltersActivity.Filters.topGT = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "топ не активно")
                layoutForGoogleTrends.visibility = View.INVISIBLE
                FiltersActivity.Filters.topGT = 0

            }
        })

        return view
    }
    fun setFilters(rangeBar: RangeBar, schoolsFilter:CheckBox,
                          meansFilter: CheckBox, agesFilter: CheckBox, googleTrendsFilter: CheckBox,
                          layoutForGoogleTrends: LinearLayout, editCount: EditText) {
        editCount.setText(FiltersActivity.Filters.countGT.toString())
        agesFilter.isChecked = FiltersActivity.Filters.ages == 1
        schoolsFilter.isChecked = FiltersActivity.Filters.schools == 1
        meansFilter.isChecked = FiltersActivity.Filters.meanings == 1
        if (FiltersActivity.Filters.topGT == 1) {
            googleTrendsFilter.isChecked = true
            layoutForGoogleTrends.visibility = View.VISIBLE
        } else {
            googleTrendsFilter.isChecked = false
            layoutForGoogleTrends.visibility = View.INVISIBLE
        }
        rangeBar.setRangePinsByValue((FiltersActivity.Filters.yearStart).toFloat(),
            (FiltersActivity.Filters.yearEnd).toFloat())
    }
    fun dropFilters() {
        FiltersActivity.Filters.countGT = FiltersObject.countGT
        FiltersActivity.Filters.ages = FiltersObject.ages
        FiltersActivity.Filters.schools = FiltersObject.schools
        FiltersActivity.Filters.meanings = FiltersObject.meanings
        FiltersActivity.Filters.topGT = FiltersObject.topGT
        FiltersActivity.Filters.yearStart = FiltersObject.yearStart
        FiltersActivity.Filters.yearEnd = FiltersObject.yearEnd
    }

}
