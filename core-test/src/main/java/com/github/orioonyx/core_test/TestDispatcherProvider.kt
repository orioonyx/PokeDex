/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.core_test

import com.github.orioonyx.pokedex.core.utils.DispatcherProvider
import kotlinx.coroutines.test.StandardTestDispatcher

object TestDispatcherProvider : DispatcherProvider(
    main = StandardTestDispatcher(),
    io = StandardTestDispatcher(),
    default = StandardTestDispatcher()
)