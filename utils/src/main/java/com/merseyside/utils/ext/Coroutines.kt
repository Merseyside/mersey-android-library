package com.merseyside.utils.ext

import com.merseyside.merseyLib.time.units.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

suspend fun delay(timeUnit: TimeUnit) = kotlinx.coroutines.delay(timeUnit.millis)