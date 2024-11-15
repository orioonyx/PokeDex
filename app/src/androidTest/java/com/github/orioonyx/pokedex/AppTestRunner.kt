/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Custom test runner for setting up Hilt in Android instrumented tests.
 * Replaces the default application with HiltTestApplication to allow dependency injection.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

@Suppress("unused")
class AppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
