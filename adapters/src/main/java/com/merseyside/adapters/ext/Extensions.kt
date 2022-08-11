@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.ext

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.interfaces.ISelectableAdapter
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

inline fun <Item> BaseAdapter<Item, *>.findPosition(predicate: (item: Item) -> Boolean): Int {
    return getAll().find { predicate(it) }?.run {
        getPositionOfItem(this)
    } ?: BaseAdapter.NO_ITEM
}

inline fun <Item> BaseAdapter<Item, *>.findFirst(predicate: (item: Item) -> Boolean): Item? {
    return getAll().find { predicate(it) }
}

inline fun <Item> BaseAdapter<Item, *>.findLast(predicate: (item: Item) -> Boolean): Item? {
    return getAll().findLast { predicate(it) }
}

inline fun <Item> BaseAdapter<Item, *>.findAll(predicate: (Item) -> Boolean): List<Item> {
    val list = mutableListOf<Item>()
    getAll().forEach { if (predicate(it)) list.add(it) }

    return list
}

internal inline fun <Model> SortedList<Model>.indexOf(predicate: (Model) -> Boolean): Int {
    forEachIndexed { index, obj -> if (predicate(obj)) return index }

    return SortedList.INVALID_POSITION
}

internal fun <Model> SortedList<Model>.removeAll(list: List<Model>) {
    list.forEach { remove(it) }
}

fun <Item : Any> List<BaseAdapter<Item, *>>.onItemClicked(onClick: (item: Item) -> Unit): OnItemClickListener<Item> {
    val listener = object : OnItemClickListener<Item> {
        override fun onItemClicked(obj: Item) {
            onClick.invoke(obj)
        }
    }

    forEach { it.setOnItemClickListener(listener) }

    return listener
}

fun <Item> HasOnItemClickListener<Item>.onItemClicked(
    onClick: (item: Item) -> Unit
): OnItemClickListener<Item> {
    val listener = object : OnItemClickListener<Item> {
        override fun onItemClicked(obj: Item) {
            onClick.invoke(obj)
        }
    }
    setOnItemClickListener(listener)

    return listener
}

fun <Item : Any> HasOnItemSelectedListener<Item>.onItemSelected(
    onSelected: (
        item: Item,
        isSelected: Boolean,
        isSelectedByUser: Boolean
    ) -> Unit
): OnItemSelectedListener<Item> {
    val listener = object : OnItemSelectedListener<Item> {
        override fun onSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean) {
            onSelected.invoke(item, isSelected, isSelectedByUser)
        }

        override fun onSelectedRemoved(
            adapterList: ISelectableAdapter<Item, *>,
            items: List<Item>
        ) {}
    }
    addOnItemSelectedListener(listener)

    return listener
}

fun <Item : Any> List<SelectableAdapter<Item, *>>.onItemSelected(
    onSelected: (
        item: Item,
        isSelected: Boolean,
        isSelectedByUser: Boolean
    ) -> Unit
): OnItemSelectedListener<Item> {
    val listener = object : OnItemSelectedListener<Item> {
        override fun onSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean) {
            onSelected.invoke(item, isSelected, isSelectedByUser)
        }

        override fun onSelectedRemoved(
            adapterList: ISelectableAdapter<Item, *>,
            items: List<Item>
        ) {}
    }

    forEach { it.addOnItemSelectedListener(listener) }

    return listener
}

fun <Item> SortedList<Item>.batchedUpdate(block: SortedList<Item>.() -> Unit) {
    beginBatchedUpdates()
    block()
    endBatchedUpdates()
}

fun <Item> SortedList<Item>.getAll(): List<Item> {
    val list = mutableListOf<Item>()
    forEach { list.add(it) }
    return list
}

internal fun CoroutineScope.asynchronously(block: suspend CoroutineScope.() -> Unit) =
    launch(context = Dispatchers.Default, block = block)