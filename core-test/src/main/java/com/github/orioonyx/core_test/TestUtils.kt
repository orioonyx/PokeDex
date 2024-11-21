/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Utility functions for setting up ActivityScenario in UI tests.
 * Provides reusable methods to configure Intent and Lifecycle state for activity testing.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.core_test

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

object TestUtils {

    inline fun <reified T : androidx.activity.ComponentActivity> setupScenario(
        intentConfig: Intent.() -> Unit = {},
        lifecycleState: Lifecycle.State = Lifecycle.State.CREATED
    ): ActivityScenario<T> {
        val intent = Intent(ApplicationProvider.getApplicationContext(), T::class.java).apply(intentConfig)
        return ActivityScenario.launch<T>(intent).apply {
            moveToState(lifecycleState)
        }
    }
}