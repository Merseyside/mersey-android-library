package com.merseyside.adapters.utils

import com.merseyside.adapters.extensions.Behaviour

class UpdateRequest<Item>(val list: List<Item>) {

    constructor(item: Item): this(listOf(item))

    var isAddNew = true
        private set

    var isDeleteOld = true
        private set

    class Builder<Item>(list: List<Item>) {

        constructor(item: Item): this(listOf(item))

        private val request: UpdateRequest<Item> = UpdateRequest(list)

        fun isAddNew(bool: Boolean): Builder<Item> {
            request.isAddNew = bool
            return this
        }

        fun isDeleteOld(bool: Boolean): Builder<Item> {
            request.isDeleteOld = bool
            return this
        }

        fun build(): UpdateRequest<Item> {
            return request
        }
    }

    companion object {
        internal fun <Item> fromBehaviour(items: List<Item>, behaviour: Behaviour): UpdateRequest<Item> {
            behaviour as Behaviour.UPDATE
            return Builder(items)
                .isAddNew(behaviour.addNew)
                .isDeleteOld(behaviour.removeOld)
                .build()
        }
    }
}