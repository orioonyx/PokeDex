/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */
package com.github.orioonyx.core_test

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

object TestDispatcherProvider {
    val main: TestDispatcher = StandardTestDispatcher()
    val io: TestDispatcher = StandardTestDispatcher()
    val default: TestDispatcher = StandardTestDispatcher()
}
