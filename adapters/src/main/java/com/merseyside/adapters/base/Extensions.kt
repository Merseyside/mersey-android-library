package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.utils.emptyMutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

fun SortedList<*>.isEmpty(): Boolean {
    return this.size() == 0
}

fun SortedList<*>.isNotEmpty(): Boolean {
    return !isEmpty()
}

@Throws(IllegalArgumentException::class)
fun <T : ComparableAdapterViewModel<M>, M : Any> SortedList<T>.isEquals(list: List<T>): Boolean {

    if (this.size() != list.size) {
        return false
    } else {

        list.forEachIndexed { index, t ->
            val value = this.get(index)
            if (!value.areItemsTheSame(t.getItem())) {
                return@isEquals false
            }
        }

        return true
    }
}

fun <T : ComparableAdapterViewModel<M>, M : Any> SortedList<T>.isNotEquals(
    list: List<T>
): Boolean = !this.isEquals(list)

internal inline fun <T> SortedList<T>.forEach(onValue: (T) -> Unit) {
    forEachIndexed { _, item -> onValue(item) }
}

internal inline fun <T> SortedList<T>.forEachIndexed(onValue: (Int, model: T) -> Unit) {
    for (i in 0 until size()) {
        onValue(i, get(i))
    }
}

internal inline fun <T> SortedList<T>.find(predicate: (model: T) -> Boolean): T? {
    forEach { if (predicate(it)) return it }

    return null
}

inline fun <M> BaseAdapter<M, *>.findPosition(predicate: (item: M) -> Boolean): Int {
    return getAll().find { predicate(it) }?.run {
        getPositionOfItem(this)
    } ?: -1
}

inline fun <M> BaseAdapter<M, *>.findFirst(predicate: (item: M) -> Boolean): M? {
    return getAll().find { predicate(it) }
}

inline fun <M> BaseAdapter<M, *>.findLast(predicate: (item: M) -> Boolean): M? {
    return getAll().findLast { predicate(it) }
}

inline fun <M> BaseAdapter<M, *>.findAll(predicate: (M) -> Boolean): List<M> {
    val list = emptyMutableList<M>()
    getAll().forEach { if (predicate(it)) list.add(it) }

    return list
}

internal inline fun <T> SortedList<T>.indexOf(predicate: (T) -> Boolean): Int {
    forEachIndexed { index, obj -> if (predicate(obj)) return index }

    return SortedList.INVALID_POSITION
}

internal fun <T> SortedList<T>.removeAll(list: List<T>) {
    list.forEach { remove(it) }
}

fun <M: Any> List<BaseAdapter<M, *>>.onItemClicked(onClick: (item: M) -> Unit): OnItemClickListener<M> {
    val listener = object : OnItemClickListener<M> {
        override fun onItemClicked(obj: M) {
            onClick.invoke(obj)
        }
    }

    forEach { it.setOnItemClickListener(listener) }

    return listener
}

fun <M> HasOnItemClickListener<M>.onItemClicked(onClick: (item: M) -> Unit): OnItemClickListener<M> {
    val listener = object : OnItemClickListener<M> {
        override fun onItemClicked(obj: M) {
            onClick.invoke(obj)
        }
    }
    setOnItemClickListener(listener)

    return listener
}

fun <M> HasOnItemSelectedListener<M>.onItemSelected(
    onSelected: (
        item: M,
        isSelected: Boolean,
        isSelectedByUser: Boolean
    ) -> Unit
): OnItemSelectedListener<M> {
    val listener = object : OnItemSelectedListener<M> {
        override fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean) {
            onSelected.invoke(item, isSelected, isSelectedByUser)
        }
    }
    setOnItemSelectedListener(listener)

    return listener
}

fun <M: Any> List<SelectableAdapter<M, *>>.onItemSelected(
    onSelected: (
        item: M,
        isSelected: Boolean,
        isSelectedByUser: Boolean
    ) -> Unit
): OnItemSelectedListener<M> {
    val listener = object : OnItemSelectedListener<M> {
        override fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean) {
            onSelected.invoke(item, isSelected, isSelectedByUser)
        }
    }

    forEach { it.setOnItemSelectedListener(listener) }

    return listener
}

internal fun CoroutineScope.asynchronously(block: suspend CoroutineScope.() -> Unit) =
    launch(context = Dispatchers.Default, block = block)