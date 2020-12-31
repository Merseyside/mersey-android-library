package com.merseyside.utils.concurency

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface Locker {

    val mutex: Mutex

    suspend fun <T> withLock(block: () -> T): T {
        return mutex.withLock { block() }
    }
}