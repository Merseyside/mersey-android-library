package com.merseyside.utils.concurency

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex

/**
 * Spin locker based on coroutine mutex.
 * Be careful and call it only from blocking functions.
 */
interface Locker {
    val mutex: Mutex

    fun lock() = runBlocking {
        mutex.lock()
    }

    fun unlock() = runBlocking {
        mutex.unlock()
    }
}

/**
 * To prevent infinity looping use only blocking methods inside [block]
 */
inline fun <R> Locker.withLock(block: () -> R): R {
    lock()

    try {
        return block()
    } finally {
        unlock()
    }
}