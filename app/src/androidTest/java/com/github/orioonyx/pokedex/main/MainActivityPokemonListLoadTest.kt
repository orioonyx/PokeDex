/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests MainActivity to verify the display of Pokemon list after data loading.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.main

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.main.MainActivity
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityPokemonListLoadTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonListRepository: PokemonListRepository = mockk()

    @BindValue
    val fetchPokemonListUseCase: FetchPokemonListUseCase = FetchPokemonListUseCase(pokemonListRepository)

    private val mockPokemonList = listOf(MockUtil.mockPokemon(), MockUtil.mockPokemon())

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)

        // Mock repository to return a predefined Pokemon list
        coEvery { pokemonListRepository.fetchPokemonList(any()) } returns flowOf(mockPokemonList)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadPokemonList_displaysDataInUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.STARTED)

            scenario.onActivity { activity ->
                // Verify that UI displays the mock Pokemon list
                assertThat(activity.binding.adapter?.currentList).isEqualTo(mockPokemonList)
                assertThat(activity.viewModel.isLoading.value).isFalse()
            }
        }
    }
}
