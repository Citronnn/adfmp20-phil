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
import kotlinx.android.synthetic.main.fragment_filters_tab.*


/**
 * A simple [Fragment] subclass.
 */
class FiltersTab : Fragment() {

    private val filters = object {
        var phils = 1
        var schools = 1
        var meanings = 0
        var ages = 0
        var topGT = 1
        var yearStart = -300
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
        val philsFilter = view.findViewById<CheckBox>(R.id.philsFilter)
        val meansFilter = view.findViewById<CheckBox>(R.id.meansFilter)
        val agesFilter = view.findViewById<CheckBox>(R.id.agesFilter)
        val googleTrendsFilter = view.findViewById<CheckBox>(R.id.googleTrendsFilter)
        val editCount = view.findViewById<EditText>(R.id.editCount)
        val increaseButton = view.findViewById<ImageButton>(R.id.increaseButton)
        val decreaseButton = view.findViewById<ImageButton>(R.id.decreaseButton)
        val layoutForGoogleTrends = view.findViewById<LinearLayout>(R.id.layoutForGoogleTrends)
        val buttonClearFilters: Button = view.findViewById<Button>(R.id.buttonClearFilters)
        buttonClearFilters.setOnClickListener {
            setFilters(rangeBar, schoolsFilter, philsFilter,
                meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends)
        }
        rangeBar.tickStart = (-800).toFloat()
        rangeBar.tickEnd = 2000F
        setFilters(rangeBar, schoolsFilter, philsFilter,
            meansFilter, agesFilter, googleTrendsFilter, layoutForGoogleTrends)
        editCount.setText("50")
        editCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Integer.parseInt(editCount.text.toString()) > 100) {
                    editCount.setText("100")
                }
                if (Integer.parseInt(editCount.text.toString()) < 0) {
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
                Log.d("kek", "$leftPinIndex, $rightPinIndex")
            }

            override fun onTouchEnded(rangeBar: RangeBar) {}
            override fun onTouchStarted(rangeBar: RangeBar) {}
        })
        schoolsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "школы активно")
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "школы не активно")
            }
        })
        philsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                Log.d("kek", "философы активно")
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "философы не активно")
            }
        })
        meansFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "понятия активно")
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "понятия не активно")
            }
        })
        agesFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "эпохи активно")
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "эпохи не активно")
            }
        })
        googleTrendsFilter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) { // делаем работу, если кнопка стала активной
                Log.d("kek", "топ активно")
                layoutForGoogleTrends.visibility = View.VISIBLE
            } else { // делаем работу, если кнопка перестала быть активной
                Log.d("kek", "топ не активно")
                layoutForGoogleTrends.visibility = View.INVISIBLE

            }
        })
        return view
    }
    fun setFilters(rangeBar: RangeBar, schoolsFilter:CheckBox, philsFilter:CheckBox,
                   meansFilter: CheckBox, agesFilter: CheckBox, googleTrendsFilter: CheckBox,
                   layoutForGoogleTrends: LinearLayout) {
        philsFilter.isChecked = this.filters.phils == 1
        agesFilter.isChecked = this.filters.ages == 1
        schoolsFilter.isChecked = this.filters.schools == 1
        meansFilter.isChecked = this.filters.meanings == 1
        if (this.filters.topGT == 1) {
            googleTrendsFilter.isChecked = true
            layoutForGoogleTrends.visibility = View.VISIBLE
        } else {
            googleTrendsFilter.isChecked = false
            layoutForGoogleTrends.visibility = View.INVISIBLE
        }
        rangeBar.setRangePinsByValue((this.filters.yearStart).toFloat(), (this.filters.yearEnd).toFloat())
    }

}
