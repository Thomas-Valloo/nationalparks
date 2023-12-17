package com.valloo.demo.nationalparks.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.WorkManagerTestInitHelper
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.ui.home.HomeFragment
import com.valloo.demo.nationalparks.utils.launchFragmentWithTestNavHostController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        WorkManagerTestInitHelper.initializeTestWorkManager(
            InstrumentationRegistry.getInstrumentation().targetContext
        )
    }

    /** Test navigation to the parks list. */
    @Test
    fun testNavigationToParksListFragment() {
        val navController = launchFragmentWithTestNavHostController<HomeFragment>()

        onView(ViewMatchers.withId(R.id.seeParksButton)).perform(ViewActions.click())
        assert(navController.currentDestination?.id == R.id.navigation_parkList)
    }

    /** Test navigation to the events list. */
    @Test
    fun testNavigationToEventsListFragment() {
        val navController = launchFragmentWithTestNavHostController<HomeFragment>()

        onView(ViewMatchers.withId(R.id.seeEventsButton))
            .perform(ViewActions.scrollTo())
            .perform(ViewActions.click())
        assert(navController.currentDestination?.id == R.id.navigation_eventViewPager)
    }
}
