package com.demo.habitcheck.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.habitcheck.R
import com.demo.habitcheck.ui.addnote.AddNoteActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddNoteActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(AddNoteActivity::class.java)

    @Test
    fun editTextTitle_isVisible() {
        Espresso.onView(withId(R.id.edtTitle))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun editTextTitle_Hint_isCorrect() {
        Espresso.onView(withId(R.id.edtTitle))
            .check(ViewAssertions.matches(withHint(R.string.edit_title)))
    }

    @Test
    fun editTextDes_isVisible() {
        Espresso.onView(withId(R.id.edtDesc))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun editTextDes_Hint_isCorrect() {
        Espresso.onView(withId(R.id.edtDesc))
            .check(ViewAssertions.matches(withHint(R.string.edit_desc)))
    }

    @Test
    fun taskHaveBlankError_isDisplay() {
        // Click done button
        Espresso.onView(withId(R.id.btnDone)).perform(ViewActions.click())

        // Check Snack bar form empty show
        Espresso.onView(withText(R.string.form_empty))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun listDay_isVisible() {
        // Click done button
        Espresso.onView(withId(R.id.rbSomeDay)).perform(ViewActions.click())

        // Check list day is visible
        Espresso.onView(withId(R.id.listDay)).check(ViewAssertions.matches(isDisplayed()))
    }
}