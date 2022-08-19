package com.merseyside.adapters.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

@InternalAdaptersApi
abstract class SingleAdapter<Item, Model>: BaseAdapter<Item, Model>()
    where Model : AdapterViewModel<Item> {

    protected abstract fun getLayoutIdForPosition(position: Int): Int
    protected abstract fun getBindingVariable(): Int
    protected abstract fun createItemViewModel(item: Item): Model

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        val model = getModelByPosition(position)
        model.onPositionChanged(position)

        bindItemList.add(model)

        listener?.let { model.setOnItemClickListener(it) }
        bind(holder, model)

        if (!isRecyclable || isRecyclable && !holder.isRecyclable) {
            holder.setIsRecyclable(isRecyclable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getBindingHolder(binding)
    }

    open fun getBindingHolder(binding: ViewDataBinding): TypedBindingHolder<Model> {
        return TypedBindingHolder(binding)
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<Model>, model: Model) {
        holder.bind(getBindingVariable(), model)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }
    
    internal open fun initItemViewModel(item: Item): Model {
        return createItemViewModel(item).apply {
            setItemPositionInterface(this@SingleAdapter)
        }
    }

    override fun createModel(item: Item): Model {
        return initItemViewModel(item)
    }

}