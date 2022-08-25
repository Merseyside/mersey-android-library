package com.merseyside.adapters.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> runWithDefault(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Default, block)