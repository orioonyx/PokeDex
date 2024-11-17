/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */
package com.github.orioonyx.core_test.di

import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.core.di.DispatcherModule

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherModule::class]
)

object TestDispatcherModule {
    @Provides
    fun provideTestDispatcherProvider(): TestDispatcherProvider {
        return TestDispatcherProvider
    }
}
