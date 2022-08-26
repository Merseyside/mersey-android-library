package com.merseyside.adapters.model

import androidx.databinding.ObservableBoolean
import com.merseyside.utils.mainThreadIfNeeds

abstract class ExpandableAdapterParentViewModel<Item: Parent, Parent, Data>(
    item: Item,
    isExpanded: Boolean = IS_EXPANDED_DEFAULT,
    isExpandable: Boolean = IS_EXPANDABLE_DEFAULT
) : NestedAdapterParentViewModel<Item, Parent, Data>(item) {

    var isExpanded: Boolean = isExpanded
        internal set(value) {
            mainThreadIfNeeds {
                if (field != value) {
                    field = value

                    this.expandedObservable.set(value)

                    onExpandedChanged(value)
                }
            }
        }

    var isExpandable: Boolean = isExpandable
        internal set(value) {
            mainThreadIfNeeds {
                if (field != value) {
                    field = value

                    this.expandableObservable.set(value)
                }
            }
        }

    val expandedObservable = ObservableBoolean(isExpanded)
    val expandableObservable = ObservableBoolean()

    internal var onExpandedCallback: ((ExpandableAdapterParentViewModel<Item, Parent, Data>) -> Unit)? = null

    fun onExpanded() {
        onExpandedCallback?.invoke(this)
    }

    open fun onExpandedChanged(isExpanded: Boolean) {
        notifyUpdate()
    }

    companion object {
        private const val IS_EXPANDED_DEFAULT = false
        private const val IS_EXPANDABLE_DEFAULT = true
    }
}