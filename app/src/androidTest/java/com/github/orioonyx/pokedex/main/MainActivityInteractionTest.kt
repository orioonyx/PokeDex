/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests MainActivity to verify user interactions, including item clicks and infinite scrolling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.main

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
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
import com.github.orioonyx.core_test.TestTree
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.github.orioonyx.pokedex.ui.main.MainActivity
import com.github.orioonyx.pokedex.ui.main.PokemonAdapter
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.google.common.truth.Truth.assertThat
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
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityInteractionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonListRepository: PokemonListRepository = mockk()

    @BindValue
    val fetchPokemonListUseCase: FetchPokemonListUseCase = FetchPokemonListUseCase(pokemonListRepository)

    @Before
    fun setUp() {
        Timber.plant(TestTree())
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)

        val firstPageList = MockUtil.generatePokemonList(50, startIndex = 1)
        coEvery { pokemonListRepository.fetchPokemonList(1) } returns flowOf(firstPageList)

        Intents.init()
    }

    @After
    fun tearDown() {
        Timber.uprootAll()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
        Intents.release()
    }

    @Test
    fun clickOnPokemonItem_navigatesToDetailActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)

            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<PokemonAdapter.PokemonViewHolder>(0, click()))

            intended(hasComponent(DetailActivity::class.java.name))
        }
    }

    @Test
    fun scrollToLastItem_displaysLastItemInView() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)

            scenario.onActivity { activity ->
                assertThat(activity.binding.adapter?.currentList?.size).isEqualTo(50)
            }

            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<PokemonAdapter.PokemonViewHolder>(49))

            onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))

            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<PokemonAdapter.PokemonViewHolder>(49, click()))

            intended(hasComponent(DetailActivity::class.java.name))
        }
    }

    @Test
    fun infiniteScroll_loadsAdditionalData() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)

            scenario.onActivity { activity ->
                assertThat(activity.binding.adapter?.currentList?.size).isEqualTo(50)
            }

            val secondPageList = MockUtil.generatePokemonList(50, startIndex = 51)
            coEvery { pokemonListRepository.fetchPokemonList(2) } returns flowOf(secondPageList)

            scenario.onActivity { activity ->
                activity.findViewById<RecyclerView>(R.id.recyclerView).smoothScrollToPosition(49)
            }

            scenario.onActivity { activity ->
                activity.findViewById<RecyclerView>(R.id.recyclerView).smoothScrollToPosition(99)
            }

            await.atMost(10, TimeUnit.SECONDS).untilAsserted {
                scenario.onActivity { activity ->
                    val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
                    assertThat(recyclerView.adapter?.itemCount).isEqualTo(100)
                }
            }
        }
    }
}
