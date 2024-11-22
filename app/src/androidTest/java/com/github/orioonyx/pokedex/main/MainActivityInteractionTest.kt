/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests MainActivity to verify user interactions, including item clicks and infinite scrolling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.main

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestUtils.setupScenario
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.github.orioonyx.pokedex.ui.main.MainActivity
import com.github.orioonyx.pokedex.ui.main.PokemonAdapter
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.awaitility.kotlin.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityInteractionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonListRepository: PokemonListRepository = mockk()

    @BindValue
    val pokemonDetailRepository: PokemonDetailRepository = mockk()

    @BindValue
    val fetchPokemonListUseCase: FetchPokemonListUseCase = FetchPokemonListUseCase(pokemonListRepository)

    @BindValue
    val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = FetchPokemonDetailUseCase(pokemonDetailRepository)

    private val mockPokemonList = MockUtil.generatePokemonList(50) {
        this.copy(name = "ditto", url = "https://pokeapi.co/api/v2/pokemon/1/")
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        Intents.init()
        setupMockRepositories()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
        Intents.release()
    }

    @Test
    fun clickOnPokemonItem_navigatesToDetailActivity() {
        coEvery { pokemonDetailRepository.fetchPokemonDetail("ditto") } returns flowOf(MockUtil.mockPokemonDetail())

        val scenario = setupScenario<MainActivity>(lifecycleState = Lifecycle.State.RESUMED)
        scenario.use {
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<PokemonAdapter.PokemonViewHolder>(0, click()))
            intended(hasComponent(DetailActivity::class.java.name))
        }
    }

    @Test
    fun scrollToLastItem_displaysLastItemInView() {
        val scenario = setupScenario<MainActivity>(lifecycleState = Lifecycle.State.RESUMED)
        scenario.use {
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<PokemonAdapter.PokemonViewHolder>(49))
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun infiniteScroll_loadsAdditionalData() {
        coEvery { pokemonListRepository.fetchPokemonList(2) } returns flowOf(MockUtil.generatePokemonList(50, startIndex = 51))

        val scenario = setupScenario<MainActivity>(lifecycleState = Lifecycle.State.RESUMED)
        scenario.use {
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<PokemonAdapter.PokemonViewHolder>(49))
            await.atMost(10, TimeUnit.SECONDS).untilAsserted {
                onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            }
        }
    }

    private fun setupMockRepositories() {
        coEvery { pokemonListRepository.fetchPokemonList(any()) } returns flowOf(mockPokemonList)
    }
}
