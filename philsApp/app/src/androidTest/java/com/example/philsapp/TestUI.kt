package com.example.philsapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class TestUI {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun graphVisible() {
        FiltersActivity.Filters.countGT = 1 // to limit Philosophers
        onView(allOf(withId(R.id.graph), hasMinimumChildCount(2))).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnGraphNode() {
        FiltersActivity.Filters.countGT = 20 // set default val back
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