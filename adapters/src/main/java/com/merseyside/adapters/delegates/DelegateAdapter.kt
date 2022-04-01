package com.merseyside.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.utils.reflection.ReflectionUtils

abstract class DelegateAdapter<Item : Parent, Parent, Model : AdapterParentViewModel<Item, Parent>>(
    private val priority: Int = 0
) {

    init {
        checkPriority()
    }

    @LayoutRes
    abstract fun getLayoutIdForItem(viewType: Int): Int
    protected abstract fun getBindingVariable(): Int

    open fun isResponsibleFor(parent: Any): Boolean {
        return persistentClass == parent::class.java
    }

    abstract fun createItemViewModel(item: Item): Model

    @Suppress("UNCHECKED_CAST")
    internal fun createItemViewModel(parent: Parent): Model {
        val item = (parent as? Item) ?: throw IllegalArgumentException(
            "This delegate is not " +
                    "responsible for ${parent!!::class}"
        )
        return createItemViewModel(item).also { it.priority = priority }
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                getLayoutIdForItem(viewType),
                parent,
                false
            )

        return getBindingHolder(binding)
    }

    open fun bindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        bind(holder, model)
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<Model>, model: Model) {
        holder.bind(getBindingVariable(), model)
    }

    open fun getBindingHolder(binding: ViewDataBinding) = TypedBindingHolder<Model>(binding)

    private fun checkPriority() {
        if (priority !in ALWAYS_FIRST_PRIORITY..ALWAYS_LAST_PRIORITY) {
            throw IllegalArgumentException("Wrong priority!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val persistentClass: Class<Item> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            DelegateAdapter::class.java,
            0
        ) as Class<Item>

    companion object {
        const val ALWAYS_FIRST_PRIORITY = -1
        const val ALWAYS_LAST_PRIORITY = Int.MAX_VALUE
    }
}