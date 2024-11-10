package com.github.orioonyx.pokedex.utils

import androidx.test.espresso.idling.CountingIdlingResource


object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField val idlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }
}
