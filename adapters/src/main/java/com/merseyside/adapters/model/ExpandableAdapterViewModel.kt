package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.merseyside.utils.mainThreadIfNeeds

abstract class ExpandableAdapterViewModel<M, T: Any>(
    obj: M,
    private var isExpanded: Boolean = IS_EXPANDED_DEFAULT,
    private var isExpandable: Boolean = IS_EXPANDABLE_DEFAULT
): SelectableAdapterViewModel<M>(obj, isExpanded && isExpandable) {

    val isExpandedObservable = ObservableField<Boolean>(isExpanded)
    val isExpandableObservable = ObservableBoolean()

    init {
        setExpanded(isExpanded)
        setExpandable(isExpandable)
    }

    fun setExpanded(isExpanded: Boolean) {
        mainThreadIfNeeds {
            this.isExpanded = isExpanded

            if (isExpandedObservable() != isExpanded) {
                this.isExpandedObservable.set(isExpanded)
            }

            onExpanded(isExpanded)
        }
    }

    fun setExpandable(isExpandable: Boolean) {
        mainThreadIfNeeds {

            if (this.isExpandableObservable() != isExpandable) {
                this.isExpandableObservable.set(isExpandable)
            }
        }
    }

    fun isExpanded(): Boolean {
        return isExpanded
    }

    fun isExpandable(): Boolean {
        return isExpandable
    }

    fun isExpandedObservable(): Boolean {
        return isExpandedObservable.get() ?: IS_EXPANDED_DEFAULT
    }

    fun isExpandableObservable(): Boolean {
        return isExpandableObservable.get()
    }

    @CallSuper
    override fun onSelectedChanged(isSelected: Boolean) {
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

    companion object {
        private const val IS_EXPANDED_DEFAULT = false
        private const val IS_EXPANDABLE_DEFAULT = true
    }

}