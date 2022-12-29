package com.merseyside.adapters.extensions

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.callback.*
import com.merseyside.adapters.feature.selecting.AdapterSelect
import com.merseyside.adapters.feature.selecting.callback.HasOnItemSelectedListener
import com.merseyside.adapters.feature.selecting.callback.OnItemSelectedListener
import com.merseyside.adapters.interfaces.ext.removeAsync

suspend inline fun <Item> BaseAdapter<Item, *>.findPosition(predicate: (item: Item) -> Boolean): Int {
    return getAll().find { predicate(it) }?.run {
        getPositionOfItem(this)
    } ?: -1
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

inline fun <Item> BaseAdapter<Item, *>.removeIfAsync(predicate: (Item) -> Boolean): List<Item> {
    val itemsToRemove = findAll(predicate)
    removeAsync(itemsToRemove)
    return itemsToRemove
}

fun <Item> HasOnItemClickListener<Item>.onClick(
    onClick: (item: Item) -> Unit
): OnItemClickListener<Item> {
    val listener = object : OnItemClickListener<Item> {
        override fun onItemClicked(item: Item) {
            onClick.invoke(item)
        }
    }
    setOnItemClickListener(listener)

    return listener
}

fun <Item> HasOnItemSelectedListener<Item>.onItemSelected(
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
            adapterList: AdapterSelect<Item, *>,
            items: List<Item>
        ) {}
    }
    addOnItemSelectedListener(listener)

    return listener
}

fun <Item> HasOnItemExpandedListener<Item>.onItemExpanded(
    onExpanded: (
        item: Item,
        isExpanded: Boolean,
        isExpandedByUser: Boolean
    ) -> Unit
): OnItemExpandedListener<Item> {
    val listener = object : OnItemExpandedListener<Item> {
        override fun onExpanded(
            item: Item,
            isExpanded: Boolean,
            isExpandedByUser: Boolean
        ) {
            onExpanded(item, isExpanded, isExpandedByUser)
        }
    }

    addOnItemExpandedListener(listener)
    return listener
}