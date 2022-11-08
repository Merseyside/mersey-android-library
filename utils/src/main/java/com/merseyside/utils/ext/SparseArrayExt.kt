package com.merseyside.utils.ext

import android.util.SparseArray

fun <T> SparseArray<T>.values(): List<T> {
    val values = ArrayList<T>()
    forEachValue {
        values.add(it)
    }

    return values
}

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

inline fun <T> SparseArray<T>.findKey(predicate: (Pair<Int, T>) -> Boolean): Int? {
    forEach {
        if (predicate(it)) return it.first
    }

    return null
}

inline fun <T> SparseArray<T>.findValue(predicate: (Pair<Int, T>) -> Boolean): T? {
    forEach {
        if (predicate(it)) return it.second
    }

    return null
}

inline fun <T> SparseArray<T>.filterKeys(predicate: (Int) -> Boolean): List<T> {
    val filteredList = ArrayList<T>()
    forEach {
        if (predicate(it.first)) filteredList.add(it.second)
    }

    return filteredList
}

inline fun <T> SparseArray<T>.filterValues(predicate: (T) -> Boolean): List<T> {
    val filteredList = ArrayList<T>()
    forEachValue {
        if (predicate(it)) filteredList.add(it)
    }

    return filteredList
}

fun <T> SparseArray<T>.containsKey(key: Int): Boolean {
    return get(key) != null
}