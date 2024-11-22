/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests DetailActivity to verify dependency injection and UI component initialization.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.detail

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailActivityInjectionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun verifyViewModelInjectionAndUIInitialization() {
        val scenario = setupScenario<DetailActivity>(
            intentConfig = { putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon()) },
            lifecycleState = Lifecycle.State.CREATED
        )
        scenario.use { verifyActivityState(it) }
    }

    private fun verifyActivityState(scenario: androidx.test.core.app.ActivityScenario<DetailActivity>) {
        scenario.onActivity { activity ->
            assertThat(activity.viewModel).isNotNull()
            assertThat(activity.binding.pokemon).isNotNull()
            assertThat(activity.binding.imageRecyclerView.adapter).isNotNull()
            assertThat(activity.binding.shinyImageRecyclerView.adapter).isNotNull()
            assertThat(activity.binding.typeRecyclerView.adapter).isNotNull()
        }
    }
}
