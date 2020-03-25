package com.merseyside.merseyLib.domain.interactor.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal val computationContext: CoroutineDispatcher = Dispatchers.Default
internal val applicationContext: CoroutineDispatcher = Dispatchers.Main