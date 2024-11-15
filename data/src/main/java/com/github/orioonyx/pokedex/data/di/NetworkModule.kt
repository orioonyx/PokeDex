/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.di

import android.content.Context
import androidx.multidex.BuildConfig
import com.github.orioonyx.pokedex.data.mapper.ErrorResponseMapper
import com.github.orioonyx.pokedex.data.remote.api.PokemonApiService
import com.github.orioonyx.pokedex.data.remote.utils.PokemonConstants.BASE_URL
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePokemonApiService(retrofit: Retrofit): PokemonApiService =
        retrofit.create(PokemonApiService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        cacheInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(cacheInterceptor)
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
            }
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext appContext: Context): Cache {
        return Cache(
            File(appContext.applicationContext.cacheDir, "pokemon_cache"),
            10 * 1024 * 1024 // 10MB cache size
        )
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(): Interceptor = Interceptor { chain ->
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(30, TimeUnit.DAYS)
            .build()
        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }

    @Provides
    @Singleton
    fun provideErrorResponseMapper(): ErrorResponseMapper {
        return ErrorResponseMapper()
    }
}
