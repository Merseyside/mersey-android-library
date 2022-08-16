package com.merseyside.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.utils.reflection.ReflectionUtils

abstract class DelegateAdapter<Item : Parent, Parent, Model : AdapterParentViewModel<Item, out Parent>> {

    @LayoutRes
    abstract fun getLayoutIdForItem(viewType: Int): Int
    protected abstract fun getBindingVariable(): Int

    @CallSuper
    open fun isResponsibleFor(parent: Parent): Boolean {
        return parent?.let { isResponsibleForItemClass(it::class.java) }
            ?: throw NullPointerException("Parent is null!")
    }

    internal fun isResponsibleForItemClass(clazz: Class<out Parent>): Boolean {
        return persistentClass == clazz
    }

    abstract fun createItemViewModel(item: Item): Model

    @Suppress("UNCHECKED_CAST")
    internal open fun createItemViewModel(parent: Parent): Model {
        val item = (parent as? Item) ?: throw IllegalArgumentException(
            "This delegate is not " +
                    "responsible for ${parent!!::class}"
        )
        return createItemViewModel(item)
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutIdForItem(viewType),
            parent,
            false
        )

        return getBindingHolder(binding)
    }

    open fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        bind(holder, model)
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<Model>, model: Model) {
        holder.bind(getBindingVariable(), model)
    }

    open fun getBindingHolder(binding: ViewDataBinding) = TypedBindingHolder<Model>(binding)

    @Suppress("UNCHECKED_CAST")
    private val persistentClass: Class<Item> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            DelegateAdapter::class.java,
            0
        ) as Class<Item>
}