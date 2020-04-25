package com.merseyside.merseyLib.adapters

class UpdateRequest<T>(val list: List<T>) {

    var isAddNew = true
        private set

    var isDeleteOld = false
        private set

    class Builder<T>(list: List<T>) {

        private val request: UpdateRequest<T> = UpdateRequest(list)

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