/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests the app's initial load to verify the first screen and overall app stability.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppLoadTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun appStarts_displaysFirstScreen() {
        val scenario = setupScenario<MainActivity>()
        scenario.use {
            onView(withId(R.id.mainContainer)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun appDoesNotCrashOnLaunch() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assert(appContext.packageName == "com.github.orioonyx.pokedex")
    }
}
