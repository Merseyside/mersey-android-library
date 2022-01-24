package com.merseyside.utils.ext

import android.util.SparseArray

inline fun <T> SparseArray<T>.forEachKey(block: (Int) -> Unit) {
    (0 until size()).forEach { index ->
        block(keyAt(index))
    }
}

inline fun <T> SparseArray<T>.forEachValue(block: (T) -> Unit) {
    (0 until size()).forEach { index ->
        block(valueAt(index))
    }
}

inline fun <T> SparseArray<T>.forEach(block: (Pair<Int, T>) -> Unit) {
    (0 until size()).forEach { index ->
        block(keyAt(index) to valueAt(index))
    }
}

inline fun <T> SparseArray<T>.findKey(predecate: (Pair<Int, T>) -> Boolean): Int? {
    forEach {
        if (predecate(it)) return it.first
    }

    return null
}

inline fun <T> SparseArray<T>.findValue(predecate: (Pair<Int, T>) -> Boolean): T? {
    forEach {
        if (predecate(it)) return it.second
    }

    return null
}

fun <T> SparseArray<T>.containsKey(key: Int): Boolean {
    return get(key) != null
}