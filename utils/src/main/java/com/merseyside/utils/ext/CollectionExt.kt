package com.merseyside.utils.ext

import com.merseyside.utils.time.Millis
import com.merseyside.utils.time.TimeUnit
import com.merseyside.utils.time.plus
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Collection<*>?.isNotNullAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndEmpty != null)
    }

    return this != null && this.isNotEmpty()
}

fun <T: Any> List<T>.removeEqualItems(): List<T> {
    return this.toSet().toList()
}

fun <T: Any> List<T>.unique(predicate: (T, T) -> Boolean): List<T> {
    return if (isNotEmpty()) {
        val uniqueList = ArrayList<T>()

        forEachIndexed { index, value ->
            if (index.isZero()) {
                uniqueList.add(value)
            } else {

                val found = uniqueList.find { predicate.invoke(it, value) }

                if (found == null) uniqueList.add(0, value)
            }
        }

        uniqueList.reversed()
    } else {
        this
    }
}

fun <T: Any, R : Comparable<R>> List<T>.minByNullable(selector: (T) -> R?): T? {

    var minValue: R? = null
    var minElement: T? = null

    forEach { value ->
        val selectorValue = selector(value)

        if (selectorValue != null) {
            if (minElement == null) {
                minElement = value
                minValue = selectorValue
            } else {
                if (minValue!!.compareTo(selectorValue) == 1) {
                    minElement = value
                    minValue = selectorValue
                }
            }
        }
    }

    return minElement
}

fun <T: Any> List<T?>.forEachNotNull(action: (T) -> Unit): Unit {
    return this.filterNotNull().forEach(action)
}

fun List<Boolean>.forEachIsTrue(): Boolean {
    return this.find { !it } != null
}

fun <T: Any> List<List<T>>.union(): List<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptyList()
    if (size == 1) return first()

    var resultList = first().toSet()

    (1 until size).forEach { index ->
        resultList = resultList.union(get(index))
    }

    return resultList.toList()
}

fun <T: Any> List<List<T>>.intersect(): List<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptyList<T>()
    if (size == 1) return first()

    var resultList = first().toSet()

    (1 until size).forEach { index ->
        resultList = resultList.intersect(get(index))
    }

    return resultList.toList()
}

fun <K, V> Map<out K, V>.forEachEntry(action: (key: K, value: V) -> Unit) {
    forEach { entry -> action(entry.key, entry.value) }
}

fun List<TimeUnit>.sum(): TimeUnit {
    var sum = Millis()
    forEach { sum += it }

    return sum
}