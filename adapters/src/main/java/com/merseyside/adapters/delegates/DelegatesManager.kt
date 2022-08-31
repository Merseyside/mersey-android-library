package com.merseyside.adapters.delegates

import android.util.SparseArray
import android.view.ViewGroup
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.utils.ext.containsKey
import com.merseyside.utils.ext.findKey
import com.merseyside.utils.ext.findValue

class DelegatesManager<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    vararg delegates: DelegateAdapter<out Parent, Parent, *>
) {

    private val delegates = SparseArray<DelegateAdapter<out Parent, Parent, Model>>()
    private lateinit var onDelegateRemoveCallback: (DelegateAdapter<out Parent, Parent, *>) -> Unit

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

    internal fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        requireDelegate { getResponsibleDelegate(model) }.onBindViewHolder(holder, model, position)
    }

    fun getViewTypeByItem(model: Model): Int {
        return if (count.isNotZero()) {
            delegates.findKey { it.second.isResponsibleFor(model.item) }
                ?: throw IllegalArgumentException("No responsible delegates found!")
        } else throw IllegalStateException("Delegates are empty. Please, add delegates before using this!")
    }

    fun getDelegateByViewType(Int: Int): DelegateAdapter<out Parent, Parent, Model> {
        return requireDelegate { delegates.get(Int) }
    }

    fun getDelegateKey(delegate: DelegateAdapter<out Parent, Parent, Model>): Int {
        val index = delegates.indexOfValue(delegate)
        return if (index >= 0) {
            delegates.keyAt(index)
        } else throw IllegalArgumentException("View type of passed delegate not found!")
    }

    fun hasDelegate(delegate: DelegateAdapter<out Parent, Parent, Model>): Boolean {
        return delegates.findValue { it.second == delegate } != null
    }

    fun hasResponsibleDelegate(clazz: Class<out Parent>): Boolean {
        return getResponsibleDelegate(clazz) != null
    }

    fun hasResponsibleDelegate(item: Parent): Boolean {
        return getResponsibleDelegate(item) != null
    }

    fun getResponsibleDelegate(clazz: Class<out Parent>): DelegateAdapter<out Parent, Parent, Model>? {
        return delegates.findValue { it.second.isResponsibleForItemClass(clazz) }
    }

    fun getResponsibleDelegate(item: Parent): DelegateAdapter<out Parent, Parent, Model>? {
        return delegates.findValue { it.second.isResponsibleFor(item) }
    }

    fun removeResponsibleDelegate(clazz: Class<out Parent>): Boolean {
        val delegate = getResponsibleDelegate(clazz)
        return delegate?.let {
            onDelegateRemoveCallback(delegate)
            val key = getDelegateKey(delegate)
            delegates.remove(key)
            true
        } ?: false
    }

    fun removeResponsibleDelegate(item: Parent): Boolean {
        return removeResponsibleDelegate(item!!::class.java)
    }

    private fun getResponsibleDelegate(model: Model): DelegateAdapter<out Parent, Parent, Model> {
        return if (count.isNotZero()) {
            requireDelegate { delegates.findValue { it.second.isResponsibleFor(model.item) } }
        } else throw IllegalStateException("Delegates are empty. Please, add delegates before using this!")
    }

    internal fun createModel(item: Parent): Model {
        val delegate = requireDelegate {
            delegates.findValue {
                it.second.isResponsibleFor(item)
            }
        }
         return delegate.createItemViewModel(item)
    }

    internal fun setOnDelegateRemoveCallback(callback: (DelegateAdapter<out Parent, Parent, *>) -> Unit) {
        onDelegateRemoveCallback = callback
    }

    private fun requireDelegate(
        block: () -> DelegateAdapter<out Parent, Parent, Model>?
    ): DelegateAdapter<out Parent, Parent, Model> {
        return block() ?: throw NullPointerException("Delegate was required but have null!")
    }
}

