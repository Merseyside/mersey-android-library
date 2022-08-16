@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.extensions

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.interfaces.ISelectableAdapter
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

internal fun CoroutineScope.asynchronously(block: suspend CoroutineScope.() -> Unit) =
    launch(context = Dispatchers.Default, block = block)