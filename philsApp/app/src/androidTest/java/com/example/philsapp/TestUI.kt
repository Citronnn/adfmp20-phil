package com.example.philsapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.appyvet.materialrangebar.RangeBar
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.*
import org.junit.runner.RunWith

@BeforeClass
fun init() {
    FiltersActivity.Filters.countGT = 1
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestUI {

    private val ITEM_BELOW_THE_FOLD = 20

    @After
    fun afterEachTest() {
        FiltersActivity.Filters.countGT = 1
    }

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun graphVisible() {
        onView(allOf(withId(R.id.graph), hasMinimumChildCount(2))).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnGraphNode() {
        val nodeTitle = getText(withIndex(withId(R.id.nodeTextView), 0))
        onView(withIndex(withId(R.id.nodeTextView), 0)).perform(click())
        onView(withId(R.id.titleContent)).check(matches(isDisplayed())).check(matches(withText(nodeTitle)))
    }

    @Test
    fun searchPhilosopher() {
        onView(withId(R.id.filters)).perform(click())
        onView(allOf(withText("Поиск"), isDescendantOfA(withId(R.id.tabs)))).perform(click())
        onView(withId(R.id.textForSearch)).perform(typeText("Plato"))
        onView(withId(R.id.buttonForSearch)).perform(click())
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withText("Plato")).perform(click())
        onView(withId(R.id.titleContent)).check(matches(isDisplayed())).check(matches(withText("Plato")))
    }

    @Test
    fun clickOnTimeline() {
        onView(withId(R.id.gotoLine)).perform(click())
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(ITEM_BELOW_THE_FOLD, click()))
        onView(withId(R.id.titleContent)).check(matches(isDisplayed()))
    }

    @Test
    fun checkFiltersWithGraph() {
        onView(withId(R.id.filters)).perform(click())
        onView(withId(R.id.buttonClearFilters)).perform(click())
        onView(withId(R.id.rangeBar)).perform(setProgress(-800F, 1981F))
        onView(withId(R.id.schoolsFilter)).perform(click())
        onView(withId(R.id.meansFilter)).perform(click())
        onView(withId(R.id.agesFilter)).perform(click())
        onView(withId(R.id.googleTrendsFilter)).perform(click())
        onView(withId(R.id.returnFromFilters)).perform(click())
        onView(allOf(withId(R.id.graph), hasMinimumChildCount(2))).check(matches(isDisplayed()))

        onView(withId(R.id.filters)).perform(click())
        onView(withId(R.id.buttonClearFilters)).perform(click())
    }

    @Test
    fun checkFiltersWithTimeline() {
        onView(withId(R.id.gotoLine)).perform(click())
        onView(withId(R.id.filters)).perform(click())
        onView(withId(R.id.buttonClearFilters)).perform(click())
        onView(withId(R.id.rangeBar)).perform(setProgress(-800F, 1981F))
        onView(withId(R.id.schoolsFilter)).perform(click())
        onView(withId(R.id.meansFilter)).perform(click())
        onView(withId(R.id.agesFilter)).perform(click())
        onView(withId(R.id.googleTrendsFilter)).perform(click())
        onView(withId(R.id.returnFromFilters)).perform(click())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        onView(withId(R.id.filters)).perform(click())
        onView(withId(R.id.buttonClearFilters)).perform(click())
    }
}

fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?>? { // get view at pos from multiple views
    return object : TypeSafeMatcher<View?>() {
        var currentIndex = 0
        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}

fun getText(matcher: Matcher<View?>?): String? {
    val stringHolder = arrayOf<String?>(null)
    onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "getting text from a TextView"
        }

        override fun perform(uiController: UiController?, view: View) {
            val tv = view as TextView //Save, because of check in getConstraints()
            stringHolder[0] = tv.text.toString()
        }
    })
    return stringHolder[0]
}

fun setProgress(leftPin: Float, rightPin: Float): ViewAction? {
    return object : ViewAction {
        override fun perform(
            uiController: UiController,
            view: View
        ) {
            val rangeBar = view as RangeBar
            rangeBar.setRangePinsByValue(leftPin, rightPin)
        }

        override fun getDescription(): String {
            return "Set a progress on a RangeBar"
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(RangeBar::class.java)
        }
    }
}