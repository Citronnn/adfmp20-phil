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
        var phils = 0
        var schools = 0
        var meanings = 0
        var ages = 0
        var topGT = 0
        var countGT: Int = 50
        var yearStart = 0
        var yearEnd = 0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filters_tab, container, false)
        val rangeBar = view.findViewById<RangeBar>(R.id.rangeBar)
        val schoolsFilter = view.findViewById<CheckBox>(R.id.schoolsFilter)
        val philsFilter = view.findViewById<CheckBox>(R.id.philsFilter)
        val meansFilter = view.findViewById<CheckBox>(R.id.meansFilter)
        val agesFilter = view.findViewById<CheckBox>(R.id.agesFilter)
        val googleTrendsFilter = view.findViewById<CheckBox>(R.id.googleTrendsFilter)
        val editCount = view.findViewById<EditText>(R.id.editCount)
        val increaseButton = view.findViewById<ImageButton>(R.id.increaseButton)
        val decreaseButton = view.findViewById<ImageButton>(R.id.decreaseButton)
        val layoutForGoogleTrends = view.findViewById<LinearLayout>(R.id.layoutForGoogleTrends)
        val buttonClearFilters: Button = view.findViewById<Button>(R.id.buttonClearFilters)
        val activity = FiltersActivity()
        val data = activity.getFilters()
        FiltersObject.phils = data.phils
        FiltersObject.schools = data.schools
        FiltersObject.countGT = data.countGT
        FiltersObject.meanings = data.meanings
        FiltersObject.ages = data.ages
        FiltersObject.topGT = data.topGT
        FiltersObject.yearStart = data.yearStart
        FiltersObject.yearEnd = data.yearEnd
        buttonClearFilters.setOnClickListener {
            setFilters(rangeBar, schoolsFilter, philsFilter,
                meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends,
                editCount)
        }
        rangeBar.tickStart = (-800).toFloat()
        rangeBar.tickEnd = 2000F
        setFilters(rangeBar, schoolsFilter, philsFilter,
            meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends,
            editCount)
        editCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                FiltersObject.countGT = Integer.parseInt(editCount.text.toString())
                if (Integer.parseInt(editCount.text.toString()) > 100) {
                    FiltersObject.countGT = 100
                    editCount.setText("100")
                }
                if (Integer.parseInt(editCount.text.toString()) < 0) {
                    FiltersObject.countGT = 0
                    editCount.setText("0")
                }
                activity.setFilters(FiltersObject)
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
                FiltersObject.yearStart = leftPinIndex.toInt()
                FiltersObject.yearEnd = rightPinIndex.toInt()
                activity.setFilters(FiltersObject)
                Log.d("kek", "$leftPinIndex, $rightPinIndex")
            }

            override fun onTouchEnded(rangeBar: RangeBar) {}
            override fun onTouchStarted(rangeBar: RangeBar) {}
        })
        schoolsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "школы активно")
                FiltersObject.schools = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "школы не активно")
                FiltersObject.schools = 0
            }
            activity.setFilters(FiltersObject)
        })
        philsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                Log.d("kek", "философы активно")
                FiltersObject.phils = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "философы не активно")
                FiltersObject.phils = 0
            }
            activity.setFilters(FiltersObject)
        })
        meansFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "понятия активно")
                FiltersObject.meanings = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "понятия не активно")
                FiltersObject.meanings = 0
            }
            activity.setFilters(FiltersObject)
        })
        agesFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "эпохи активно")
                FiltersObject.ages = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "эпохи не активно")
                FiltersObject.ages = 0
            }
            activity.setFilters(FiltersObject)
        })
        googleTrendsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "топ активно")
                layoutForGoogleTrends.visibility = View.VISIBLE
                FiltersObject.topGT = 1
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "топ не активно")
                layoutForGoogleTrends.visibility = View.INVISIBLE
                FiltersObject.topGT = 0

            }
            activity.setFilters(FiltersObject)
        })
        return view
    }
    fun setFilters(rangeBar: RangeBar, schoolsFilter:CheckBox, philsFilter:CheckBox,
                   meansFilter: CheckBox, agesFilter: CheckBox, googleTrendsFilter: CheckBox,
                   layoutForGoogleTrends: LinearLayout, editCount: EditText) {
        philsFilter.isChecked = FiltersObject.phils == 1
        editCount.setText(FiltersObject.countGT.toString())
        agesFilter.isChecked = FiltersObject.ages == 1
        schoolsFilter.isChecked = FiltersObject.schools == 1
        meansFilter.isChecked = FiltersObject.meanings == 1
        if (FiltersObject.topGT == 1) {
            googleTrendsFilter.isChecked = true
            layoutForGoogleTrends.visibility = View.VISIBLE
        } else {
            googleTrendsFilter.isChecked = false
            layoutForGoogleTrends.visibility = View.INVISIBLE
        }
        rangeBar.setRangePinsByValue((FiltersObject.yearStart).toFloat(), (FiltersObject.yearEnd).toFloat())
    }

}
