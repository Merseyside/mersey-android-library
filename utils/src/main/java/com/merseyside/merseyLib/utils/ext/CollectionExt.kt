package com.merseyside.merseyLib.utils.ext

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