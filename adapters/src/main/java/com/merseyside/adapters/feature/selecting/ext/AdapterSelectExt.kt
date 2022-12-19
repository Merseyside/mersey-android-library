package com.merseyside.adapters.feature.selecting.ext

import com.merseyside.adapters.feature.selecting.AdapterSelect
import com.merseyside.adapters.model.VM

fun <Item, Model : VM<Item>> AdapterSelect<Item, Model>.selectIf(predicate: (Item) -> Boolean): List<Item> {
    return modelList.getItems()
        .filter(predicate)
        .onEach { selectItem(it) }
        .toList()
}

fun <Item, Model : VM<Item>> AdapterSelect<Item, Model>.selectFirstIf(predicate: (Item) -> Boolean): Item? {

    return modelList.asSequence()
        .map { it.item }
        .firstOrNull(predicate)
        ?.also { selectItem(it) }
}