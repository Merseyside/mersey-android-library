@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.feature.style.Styleable
import com.merseyside.adapters.feature.style.StyleableItem
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.utils.reflection.ReflectionUtils

abstract class DelegateAdapter<Item : Parent, Parent, Model> : HasOnItemClickListener<Item>
    where Model : AdapterParentViewModel<Item, out Parent> {

    override var clickListeners: MutableList<OnItemClickListener<Item>> = ArrayList()

    private val onClick: (Item) -> Unit = { item ->
        clickListeners.forEach { listener -> listener.onItemClicked(item) }
    }

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
        return createItemViewModel(item).also { model -> onModelCreated(model) }
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

    internal open fun onModelCreated(model: Model) {
        model.clickEvent.observe(onClick)
    }

    @CallSuper
    open fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        holder.bind(getBindingVariable(), model)
        if (this is Styleable<*, *>) applyStyleInternal(holder.binding, model.item as StyleableItem<*>)
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