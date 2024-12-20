/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests MainActivity-specific behaviors, including UI state and data loading.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.main

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonListRepository: PokemonListRepository = mockk()

    @BindValue
    val fetchPokemonListUseCase: FetchPokemonListUseCase = FetchPokemonListUseCase(pokemonListRepository)

    private val mockPokemonList = MockUtil.generatePokemonList(50, startIndex = 1)

    @Before
    fun setUp() {
        hiltRule.inject()
        mockPokemonRepository()
    }

    @Test
    fun mainActivity_displaysRecyclerViewAndAppBar() {
        val scenario = setupScenario<MainActivity>(
            lifecycleState = Lifecycle.State.RESUMED
        )
        scenario.use {
            onView(withId(R.id.appBarLayout)).check(matches(isDisplayed()))
            onView(withId(R.id.progressbar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        }
    }

    private fun mockPokemonRepository() {
        coEvery { pokemonListRepository.fetchPokemonList(any()) } returns flowOf(mockPokemonList)
    }
}
