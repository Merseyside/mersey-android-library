package com.merseyside.utils.ext

fun Collection<*>?.isNotNullAndEmpty(): Boolean {
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