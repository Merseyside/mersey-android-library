package com.merseyside.archy.domain.model

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: String) : DataState<Nothing>()
    object Empty : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}