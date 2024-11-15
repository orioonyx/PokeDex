/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain.usecase

import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

class FetchPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonDetailRepository
) {
    operator fun invoke(name: String): Flow<PokemonDetail> {
        return repository.fetchPokemonDetail(name)
            .catch { exception ->
                Timber.e(exception, "Error fetching Pokemon detail")
            }
    }
}
