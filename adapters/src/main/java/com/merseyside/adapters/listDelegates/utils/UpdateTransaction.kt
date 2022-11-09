package com.merseyside.adapters.listDelegates.utils

data class UpdateTransaction<Item, Model>(
    var modelsToRemove: List<Model> = emptyList(),
    var modelsToUpdate: List<Pair<Model, Item>> = emptyList(),
    var itemsToAdd: List<Item> = emptyList()
) {
    fun isEmpty(): Boolean {
        return modelsToRemove.isEmpty() && modelsToUpdate.isEmpty() && itemsToAdd.isEmpty()
    }
}
