package com.merseyside.adapters.utils

class UpdateRequest<Item>(val list: List<Item>) {

    constructor(item: Item): this(listOf(item))

    var isAddNew = true
        private set

    var isDeleteOld = false
        private set

    class Builder<Item>(list: List<Item>) {

        constructor(item: Item): this(listOf(item))

        private val request: UpdateRequest<Item> =
            UpdateRequest(list)

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
}