package com.merseyside.archy.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModelFactory<T: BaseViewModel> : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <M : ViewModel?> create(modelClass: Class<M>): M {
        return getViewModel() as M
    }

    abstract fun getViewModel(): T
}