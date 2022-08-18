package com.merseyside.adapters.extensions

import com.merseyside.adapters.callback.*
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun <Item> IBaseAdapter<Item, *>.findPosition(predicate: (item: Item) -> Boolean): Int {
    return getAll().find { predicate(it) }?.run {
        getPositionOfItem(this)
    } ?: -1
}

inline fun <Item> IBaseAdapter<Item, *>.findFirst(predicate: (item: Item) -> Boolean): Item? {
    return getAll().find { predicate(it) }
}

inline fun <Item> IBaseAdapter<Item, *>.findLast(predicate: (item: Item) -> Boolean): Item? {
    return getAll().findLast { predicate(it) }
}

inline fun <Item> IBaseAdapter<Item, *>.findAll(predicate: (Item) -> Boolean): List<Item> {
    val list = mutableListOf<Item>()
    getAll().forEach { if (predicate(it)) list.add(it) }

    return list
}

inline fun <Item> IBaseAdapter<Item, *>.removeIf(predicate: (Item) -> Boolean): List<Item> {
    val itemsToRemove = findAll(predicate)
    remove(itemsToRemove)
    return itemsToRemove
}

fun <Item> HasOnItemClickListener<Item>.onItemClicked(
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
        ) {
        }
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
        override fun onExpandedStateChanged(
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

internal fun CoroutineScope.asynchronously(block: suspend CoroutineScope.() -> Unit) =
    launch(context = Dispatchers.Default, block = block)