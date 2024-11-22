/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests DetailActivity's initialization and verifies UI behavior and ViewModel injection.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.detail

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
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
class DetailActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonDetailRepository: PokemonDetailRepository = mockk()

    @BindValue
    val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = FetchPokemonDetailUseCase(pokemonDetailRepository)

    private val mockPokemon = MockUtil.mockPokemon()
    private val mockPokemonDetail = MockUtil.mockPokemonDetail()

    @Before
    fun setUp() {
        hiltRule.inject()
        mockPokemonRepository()
    }

    @Test
    fun detailActivity_displaysPokemonDetails() {
        val scenario = setupScenario<DetailActivity>(
            intentConfig = { putExtra(DetailActivity.EXTRA_POKEMON, mockPokemon) },
            lifecycleState = Lifecycle.State.RESUMED
        )
        scenario.use {
            verifyUIComponents()
            verifyRecyclerViews()
        }
    }

    private fun mockPokemonRepository() {
        coEvery { pokemonDetailRepository.fetchPokemonDetail(mockPokemon.name) } returns flowOf(mockPokemonDetail)
    }

    private fun verifyUIComponents() {
        onView(withId(R.id.imageRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.shinyImageRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.typeRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.pokemonDetailContainer)).check(matches(isDisplayed()))
    }

    private fun verifyRecyclerViews() {
        onView(withId(R.id.imageRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.shinyImageRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.typeRecyclerView)).check(matches(isDisplayed()))
    }
}
