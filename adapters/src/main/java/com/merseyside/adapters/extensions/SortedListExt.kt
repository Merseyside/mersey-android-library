package com.merseyside.adapters.extensions

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.list.SortedList

fun SortedList<*>.isEmpty(): Boolean {
    return this.size() == 0
}

fun SortedList<*>.isNotEmpty(): Boolean {
    return !isEmpty()
}

@Throws(IllegalArgumentException::class)
fun <Model : AdapterParentViewModel<out Parent, Parent>, Parent> SortedList<Model>.isEquals(
    list: List<Model>
): Boolean {

    if (this.size() != list.size) {
        return false
    } else {

        list.forEachIndexed { index, model ->
            val value = this[index]
            if (!value.areItemsTheSame(model.item)) {
                return@isEquals false
            }
        }

        return true
    }
}

internal inline fun <Model> SortedList<Model>.forEach(onValue: (Model) -> Unit) {
    forEachIndexed { _, item -> onValue(item) }
}

internal inline fun <Model> SortedList<Model>.forEachIndexed(onValue: (Int, model: Model) -> Unit) {
    for (i in 0 until size()) {
        onValue(i, get(i))
    }
}

internal inline fun <Model> SortedList<Model>.find(predicate: (model: Model) -> Boolean): Model? {
    forEach { if (predicate(it)) return it }

    return null
}

internal inline fun <Model> SortedList<Model>.indexOf(predicate: (Model) -> Boolean): Int {
    forEachIndexed { index, obj -> if (predicate(obj)) return index }

    return SortedList.INVALID_POSITION
}

internal suspend fun <Model> SortedList<Model>.contains(model: Model): Boolean {
    return indexOf(model) != SortedList.INVALID_POSITION
}

internal suspend fun <Model> SortedList<Model>.removeAll(list: List<Model>) {
    list.forEach { remove(it) }
}

suspend inline fun <Item> SortedList<Item>.batchedUpdate(crossinline block: suspend SortedList<Item>.() -> Unit) {
    try {
        beginBatchedUpdates()
        //runWithDefault {
        block()
        //}
    } finally {
        endBatchedUpdates()
    }
}

suspend fun <Model> SortedList<Model>.recalculatePositions() {
    val models = getAll()
    clear()
    addAll(models)
}

suspend fun <Model> SortedList<Model>.recalculatePositionsWithAnimation() {
    val models = getAll()
    models.forEach { model ->
        val pos = indexOf { it == model  }
        if (pos != SortedList.INVALID_POSITION) {
            recalculatePositionOfItemAt(pos)
        }
    }
}

fun <Model> SortedList<Model>.toList(): List<Model> {
    return getAll()
}

fun <Model> SortedList<Model>.subList(fromIndex: Int, toIndex: Int): List<Model> {
    val list = toList()
    return list.subList(fromIndex, toIndex)
}