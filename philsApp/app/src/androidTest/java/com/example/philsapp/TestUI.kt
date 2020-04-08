package com.example.philsapp

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class TestUI {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
    }

    @Test
    fun graphVisible() {
        FiltersActivity.Filters.countGT = 1 // to limit Philosophers
        onView(allOf(withId(R.id.graph), hasMinimumChildCount(2))).check(matches(isDisplayed()))
    }

    @Test
    fun canClickOnGraphNode() {
        FiltersActivity.Filters.countGT = 20 // set default val back
        onView(withIndex(withId(R.id.nodeTextView), 0)).perform(click())
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