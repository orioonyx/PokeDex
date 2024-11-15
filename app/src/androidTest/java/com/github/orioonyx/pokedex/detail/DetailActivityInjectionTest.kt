/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests DetailActivity to verify dependency injection and UI component initialization.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.detail

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailActivityInjectionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun verifyViewModelInjectionAndUIInitialization() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon())
        }

        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.CREATED)
            scenario.onActivity { activity ->
                assertThat(activity.viewModel).isNotNull()
                assertThat(activity.binding.pokemon).isNotNull()
                assertThat(activity.binding.imageRecyclerView.adapter).isNotNull()
                assertThat(activity.binding.shinyImageRecyclerView.adapter).isNotNull()
                assertThat(activity.binding.typeRecyclerView.adapter).isNotNull()
            }
        }
    }
}
