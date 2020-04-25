package com.merseyside.merseyLib.model

import androidx.annotation.CallSuper
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.ext.log

abstract class BaseExpandableAdapterViewModel<M, T: Any>(
    obj: M,
    isExpanded: Boolean = false,
    isExpandable: Boolean = true
): BaseSelectableAdapterViewModel<M>(obj, isExpanded && isExpandable) {

    private var isExpanded = MutableLiveData<Boolean>()

    fun getExpanded(): LiveData<Boolean> = isExpanded

    private var isExpandable = MutableLiveData<Boolean>()

    fun getExpandable(): LiveData<Boolean> = isExpandable

    init {
        setExpanded(isExpanded)
        setExpandable(isExpandable)
    }

    fun setExpanded(isExpanded: Boolean) {
        if (isExpanded() != isExpanded) {
            this.isExpanded.value = isExpanded
            onExpanded(isExpanded)
        }
    }

    fun setExpandable(isExpandable: Boolean) {
        if (this.isExpandable() != isExpandable) {
            this.isExpandable.value = isExpandable
        }
    }

    fun isExpanded(): Boolean {
        return isExpanded.value ?: false
    }

    fun isExpandable(): Boolean {
        return (isExpandable.value ?: false).log()
    }

    @CallSuper
    override fun onSelectedChanged(isSelected: Boolean) {
        Logger.log(this, isSelected)
        if (isExpandable()) {
            setExpanded(isSelected)
        }
    }

    internal fun getData(): List<T>? {
        val data = getExpandedData()

        if (data.isNullOrEmpty()) {
            setExpandable(false)
        } else {
            setExpandable(true)
        }

        return data
    }

    protected abstract fun getExpandedData(): List<T>?

    abstract fun onExpanded(isExpanded: Boolean)

}