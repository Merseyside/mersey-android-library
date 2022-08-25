package com.merseyside.adapters.extensions

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.runWithDefault

fun SortedList<*>.isEmpty(): Boolean {
    return this.size() == 0
}

fun SortedList<*>.isNotEmpty(): Boolean {
    return !isEmpty()
}

@Throws(IllegalArgumentException::class)
fun <Model : ComparableAdapterParentViewModel<out Parent, Parent>, Parent> SortedList<Model>.isEquals(
    list: List<Model>
): Boolean {

    if (this.size() != list.size) {
        return false
    } else {

        list.forEachIndexed { index, model ->
            val value = this.get(index)
            if (!value.areItemsTheSame(model.item)) {
                return@isEquals false
            }
        }

        return true
    }
}

fun <Model : ComparableAdapterParentViewModel<out Parent, Parent>, Parent> SortedList<Model>.isNotEquals(
    list: List<Model>
): Boolean = !this.isEquals(list)

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

internal fun <Model> SortedList<Model>.removeAll(list: List<Model>) {
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

fun <Item> SortedList<Item>.getAll(): List<Item> {
    val list = mutableListOf<Item>()
    forEach { list.add(it) }
    return list
}

fun SortedList<*>.recalculatePositions() {
    val models = getAll()
    models.forEach { model ->
        val pos = indexOf { it == model }
        recalculatePositionOfItemAt(pos)
    }
}