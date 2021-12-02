package com.demo.habitcheck.ui.home

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.habitcheck.MainActivity
import com.demo.habitcheck.R
import com.demo.habitcheck.data.model.Task
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val positionTest = 0

    private val notes = arrayOf(
        Task(
            null,
            "Title test",
            "Descriptio test",
            1
        ),
    )

    @Test
    fun recyclerview_isVisible() {
        onView(withId(R.id.rvTask))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun clickButton_openAddTaskActivity() {
        onView(withId(R.id.fab)).perform(ViewActions.click())

        onView(withId(R.id.edtDesc)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun clickNavigation_isNavigationVisible() {
        // Left Drawer should be closed.
        onView(withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open());

        // Confirm navigation bar visible
        onView(withId(R.id.nav_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}