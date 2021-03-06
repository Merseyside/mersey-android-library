package com.merseyside.archy.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModelFactory<T: BaseViewModel> : ViewModelProvider.Factory {

    override fun <M : ViewModel?> create(modelClass: Class<M>): M {
        return getViewModel() as M
    }

    abstract fun getViewModel(): T
}