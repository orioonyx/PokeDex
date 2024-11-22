/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.core_test.di

import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.core.di.DispatcherModule
import com.github.orioonyx.pokedex.core.utils.DispatcherProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherModule::class]
)

object TestDispatcherModule {
    @Provides
    @Singleton
    fun provideTestDispatcherProvider(): DispatcherProvider {
        return TestDispatcherProvider
    }
}
