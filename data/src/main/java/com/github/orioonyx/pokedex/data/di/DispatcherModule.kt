package com.github.orioonyx.pokedex.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @IoDispatcher
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
