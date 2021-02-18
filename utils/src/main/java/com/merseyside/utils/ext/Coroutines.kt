package com.merseyside.utils.ext

import com.merseyside.utils.time.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }

suspend fun delay(timeUnit: TimeUnit) = kotlinx.coroutines.delay(timeUnit.toMillisLong())