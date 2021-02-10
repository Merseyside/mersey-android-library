package com.merseyside.adapters.base

class UpdateRequest<T>(val list: List<T>) {

    constructor(item: T): this(listOf(item))

    var isAddNew = true
        private set

    var isDeleteOld = false
        private set

    class Builder<T>(list: List<T>) {

        constructor(item: T): this(listOf(item))

        private val request: UpdateRequest<T> =
            UpdateRequest(list)

        fun isAddNew(bool: Boolean): Builder<T> {
            request.isAddNew = bool

            return this
        }

        fun isDeleteOld(bool: Boolean): Builder<T> {
            request.isDeleteOld = bool

            return this
        }

        fun build(): UpdateRequest<T> {
            return request
        }
    }
}