/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests app's initial load to verify main UI elements are displayed correctly in MainActivity.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.pokedex.ui.main.MainActivity
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppLoadTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun appStarts_displaysMainScreen() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)

            // Check if main UI elements are displayed correctly
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            onView(withId(R.id.appBarLayout)).check(matches(isDisplayed()))
            onView(withId(R.id.progressbar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        }
    }
}
