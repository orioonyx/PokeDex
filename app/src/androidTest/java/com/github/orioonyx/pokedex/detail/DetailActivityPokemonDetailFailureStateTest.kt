/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests DetailActivity's error handling UI when Pokemon details fail to load.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.detail

import android.content.Intent
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_DETAILS
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailActivityPokemonDetailFailureStateTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonDetailRepository: PokemonDetailRepository = mockk()

    @BindValue
    val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = FetchPokemonDetailUseCase(pokemonDetailRepository)

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        coEvery { pokemonDetailRepository.fetchPokemonDetail(any()) } returns flow {
            throw RuntimeException("Failed to fetch Pokemon detail")
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadFailedPokemonDetail_displaysErrorUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon())
        }
        val errorMessage = ERROR_LOADING_POKEMON_DETAILS

        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.STARTED)
            scenario.onActivity { activity ->
                activity.viewModel.fetchPokemonDetail("unknown")

                // Verify error state in UI after data fetch failure
                assertThat(activity.binding.pokemonDetail).isNull()
                assertThat(activity.viewModel.isLoading.value).isFalse()
                assertThat(activity.viewModel.isFetchFailed.value).isTrue()
                assertThat(activity.binding.emptyView.isVisible).isTrue()
                assertThat(activity.viewModel.toastMessage.value?.peekContent()).isEqualTo(errorMessage)
            }
        }
    }

    @Test
    fun loadEmptyPokemonDetail_displaysErrorUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon())
        }
        val errorMessage = ERROR_LOADING_POKEMON_DETAILS

        // Return empty data to simulate empty state
        coEvery { pokemonDetailRepository.fetchPokemonDetail(any()) } returns flowOf(MockUtil.emptyPokemonDetail())

        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.STARTED)
            scenario.onActivity { activity ->
                activity.viewModel.fetchPokemonDetail("unknown")

                // Verify error state in UI after receiving empty data
                assertThat(activity.binding.pokemonDetail).isNotNull()
                assertThat(activity.viewModel.isLoading.value).isFalse()
                assertThat(activity.viewModel.isFetchFailed.value).isTrue()
                assertThat(activity.binding.emptyView.isVisible).isTrue()
                assertThat(activity.viewModel.toastMessage.value?.peekContent()).isEqualTo(errorMessage)
            }
        }
    }
}
