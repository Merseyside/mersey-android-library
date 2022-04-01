package com.merseyside.adapters.delegates

import android.util.SparseArray
import android.view.ViewGroup
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.utils.ext.containsKey
import com.merseyside.utils.ext.findKey
import com.merseyside.utils.ext.findValue

class DelegatesManager<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    vararg delegates: DelegateAdapter<out Parent, Parent, *>
) {

    private val delegates = SparseArray<DelegateAdapter<out Parent, Parent, Model>>()

    protected val count: Int
        get() = delegates.size()

    init {
        delegates.forEachIndexed { index, delegateAdapter ->
            addDelegate(delegateAdapter, index)
        }
    }

    constructor() : this(*arrayOf<DelegateAdapter<out Parent, Parent, Model>>())

    fun addDelegates(vararg delegates: DelegateAdapter<out Parent, Parent, *>) {
        val size = count
        delegates.forEachIndexed { index, delegate ->
            addDelegate(delegate, size + index)
        }
    }

    fun addDelegate(delegate: DelegateAdapter<out Parent, Parent, *>, key: Int = count) {
        if (!delegates.containsKey(key)) {
            delegate as? DelegateAdapter<out Parent, Parent, Model>
                ?: throw IllegalArgumentException("Passed delegate has wrong type!")
            delegates.put(key, delegate)
        } else throw IllegalArgumentException("View type already exists!")
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        return requireDelegate { getDelegateByViewType(viewType) }.createViewHolder(
            parent,
            viewType
        )
    }

    fun bindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        requireDelegate { getResponsibleDelegate(model) }.bindViewHolder(holder, model, position)
    }

    fun getViewTypeByItem(model: Model): Int {
        return if (count.isNotZero()) {
            delegates.findKey { it.second.isResponsibleFor(model.item as Any) }
                ?: throw IllegalArgumentException("No responsible delegates found!")
        } else throw IllegalStateException("Delegates are empty. Please, add delegates before using this!")
    }

    fun getDelegateByViewType(Int: Int): DelegateAdapter<out Parent, Parent, Model> {
        return requireDelegate { delegates.get(Int) }
    }

    fun getDelegateViewType(delegate: DelegateAdapter<out Parent, Parent, Model>): Int {
        val index = delegates.indexOfValue(delegate)
        return if (index >= 0) {
            delegates.keyAt(index)
        } else throw IllegalArgumentException("View type of passed delegate not found!")
    }

    private fun getResponsibleDelegate(model: Model): DelegateAdapter<out Parent, Parent, Model> {
        return if (count.isNotZero()) {
            requireDelegate { delegates.findValue { it.second.isResponsibleFor(model.item as Any) } }
        } else throw IllegalStateException("Delegates are empty. Please, add delegates before using this!")
    }

    internal fun createModels(items: List<Parent>): List<Model> {
        return items.map { item ->
            val delegate = requireDelegate {
                delegates.findValue {
                    it.second.isResponsibleFor(item as Any)
                }
            }
            delegate.createItemViewModel(item)
        }
    }

    private fun requireDelegate(
        block: () -> DelegateAdapter<out Parent, Parent, Model>?
    ): DelegateAdapter<out Parent, Parent, Model> {
        return block() ?: throw NullPointerException("Delegate was required but have null!")
    }
}

