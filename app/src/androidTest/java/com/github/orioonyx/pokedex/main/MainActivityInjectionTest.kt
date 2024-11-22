/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests MainActivity to verify Hilt dependency injection and UI binding initialization.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.main

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.ui.main.MainActivity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityInjectionTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun verifyViewModelAndBindingInitialization() {
        val scenario = setupScenario<MainActivity>(
            lifecycleState = Lifecycle.State.CREATED
        )
        scenario.use { verifyActivityState(it) }
    }

    private fun verifyActivityState(scenario: androidx.test.core.app.ActivityScenario<MainActivity>) {
        scenario.onActivity { activity ->
            assertThat(activity.viewModel).isNotNull()
            assertThat(activity.binding.adapter).isNotNull()
            assertThat(activity.binding.lifecycleOwner).isEqualTo(activity)
        }
    }
}
