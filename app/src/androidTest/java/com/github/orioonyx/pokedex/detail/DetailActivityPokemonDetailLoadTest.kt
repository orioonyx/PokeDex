/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tests DetailActivity to verify successful loading and display of Pokemon details.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.detail

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
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
class DetailActivityPokemonDetailLoadTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val pokemonDetailRepository: PokemonDetailRepository = mockk()

    @BindValue
    val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = FetchPokemonDetailUseCase(pokemonDetailRepository)

    private val mockPokemonDetail = MockUtil.mockPokemonDetail()

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)

        // Mock repository to return a predefined Pokemon detail
        coEvery { pokemonDetailRepository.fetchPokemonDetail(any()) } returns flowOf(mockPokemonDetail)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadPokemonDetail_displaysDataInUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon())
        }

        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.moveToState(Lifecycle.State.STARTED)

            scenario.onActivity { activity ->
                activity.viewModel.fetchPokemonDetail("bulbasaur")

                // Verify UI displays the mock Pokemon details
                assertThat(activity.binding.pokemonDetail).isEqualTo(mockPokemonDetail)
                assertThat(activity.viewModel.isLoading.value).isFalse()
            }
        }
    }
}
