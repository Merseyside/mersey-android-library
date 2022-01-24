package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.ObservableBoolean
import com.merseyside.utils.mainThreadIfNeeds

abstract class ExpandableAdapterParentViewModel<Item: Parent, Parent, Data>(
    item: Item,
    private var isExpanded: Boolean = IS_EXPANDED_DEFAULT,
    private var isExpandable: Boolean = IS_EXPANDABLE_DEFAULT
) : SelectableAdapterParentViewModel<Item, Parent>(item, isExpanded && isExpandable) {

    val expandedObservable = ObservableBoolean(isExpanded)
    val expandableObservable = ObservableBoolean()

    init {
        setExpanded(isExpanded)
        setExpandable(isExpandable)
    }

    fun setExpanded(isExpanded: Boolean) {
        mainThreadIfNeeds {
            if (this.isExpanded != isExpanded) {
                this.isExpanded = isExpanded

                this.expandedObservable.set(isExpanded)

                onExpanded(isExpanded)
            }
        }
    }

    fun setExpandable(isExpandable: Boolean) {
        mainThreadIfNeeds {
            if (this.isExpandable != isExpandable) {
                this.isExpandable = isExpandable

                this.expandableObservable.set(isExpandable)
            }
        }
    }

    fun isExpanded(): Boolean {
        return isExpanded
    }

    fun isExpandable(): Boolean {
        return isExpandable
    }

    @CallSuper
    override fun onSelectedChanged(isSelected: Boolean) {
        if (isExpandable()) {
            setExpanded(isSelected)
        }
    }

    internal fun getExpandableData(): List<Data>? {
        val data = getExpandedData()
        setExpandable(!data.isNullOrEmpty())

        return data
    }

    protected abstract fun getExpandedData(): List<Data>?

    abstract fun onExpanded(isExpanded: Boolean)

    companion object {
        private const val IS_EXPANDED_DEFAULT = false
        private const val IS_EXPANDABLE_DEFAULT = true
    }
}