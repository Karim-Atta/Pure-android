package com.example.instabugandroidchallenge.countedwords.presentation.view

import android.content.res.Resources
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.instabugandroidchallenge.R
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.util.EspressoIdlingResourceRule
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.*

import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    val defaultFirstItem = CountedWord("Product", 8)
    val defaultFirstItemAscending = CountedWord("yeah", 8)
    val defaultFirstItemDescending = CountedWord("and", 39)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get: Rule
    val espressoIdlingResoureRule = EspressoIdlingResourceRule()

    @Test
    fun a_test_isCountedWords_Visible_onAppLaunch() {
        onView(withId(R.id.countedWordsList))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test fun b_test_isFirst_item_of_list_visible() {
        onView(withId(R.id.countedWordsList))
            .check(matches(atPositionOnView(0, withText(defaultFirstItem.word), R.id.tvWord)))
    }

    @Test
    fun c_click_onsorting_icon_should_sort_the_descending_thenanother_click_sort_it_ascending(){
        onView(withId(R.id.action_sort)).perform(click())
        onView(withId(R.id.countedWordsList))
            .check(matches(atPositionOnView(0, withText(defaultFirstItemDescending.word), R.id.tvWord)))

        onView(withId(R.id.action_sort)).perform(click())
        onView(withId(R.id.countedWordsList))
            .check(matches(atPositionOnView(0, withText(defaultFirstItemAscending.word), R.id.tvWord)))
    }

    @Test
    fun d_test_search_query_should_filter_list(){
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(
            Resources.getSystem().getIdentifier("search_src_text",
            "id", "android"))).perform(clearText(),typeText("bu"))
        onView(withId(R.id.countedWordsList))
            .check(matches(atPositionOnView(0, withText(containsString("bu")), R.id.tvWord)))
    }

    @Test
    fun d_test_search_query_with_not_found_result_should_show_empty_error(){
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(
            Resources.getSystem().getIdentifier("search_src_text",
                "id", "android"))).perform(clearText(),typeText("busijnaklnness"))
        onView(withId(R.id.tv_error)).check(matches(withText("empty counted words")))

    }
    private fun atPositionOnView(
        position: Int, itemMatcher: Matcher<View?>,
        targetViewId: Int
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView: View = viewHolder!!.itemView.findViewById(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }

    }
}