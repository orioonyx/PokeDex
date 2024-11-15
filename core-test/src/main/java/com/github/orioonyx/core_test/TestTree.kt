/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * `TestTree` class for capturing Timber logs in tests.
 * Directs Timber log output to standard output for easier verification.
 *
 * Usage:
 * - In the test's `@Before` method, use `Timber.plant(TestTree())` to set Timber to use `TestTree`.
 * - In the test's `@After` method, use `Timber.uprootAll()` to remove all planted Timber trees.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.core_test

import timber.log.Timber

class TestTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("Timber Log: [$tag] $message")
    }
}
