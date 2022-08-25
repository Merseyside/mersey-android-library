package com.merseyside.adapters.listDelegates.utils

import com.merseyside.merseyLib.kotlin.logger.ILogger

data class UpdateTransaction<Item, Model>(
    var modelsToRemove: List<Model> = emptyList(),
    var modelsToUpdate: List<Pair<Model, Item>> = emptyList(),
    var itemsToAdd: List<Item> = emptyList()
): ILogger {
    fun isEmpty(): Boolean {
        return modelsToRemove.isEmpty() && modelsToUpdate.isEmpty() && itemsToAdd.isEmpty()
    }

    fun log(): UpdateTransaction<Item, Model> {
        modelsToRemove.log("TO REMOVE\n")
        modelsToUpdate.log("TO UPDATE\n")
        itemsToAdd.log("TO ADD\n")

        return this
    }

    override val tag: String = "UpdateTransaction"
}
